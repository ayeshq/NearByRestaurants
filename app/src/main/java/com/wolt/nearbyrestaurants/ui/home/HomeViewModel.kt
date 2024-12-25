package com.wolt.nearbyrestaurants.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wolt.nearbyrestaurants.location.LocationTracker
import com.wolt.nearbyrestaurants.model.Restaurant
import com.wolt.nearbyrestaurants.usecase.FetchNearByRestaurantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchNearByRestaurantsUseCase: FetchNearByRestaurantsUseCase,
    locationTracker: LocationTracker
) : ViewModel() {

    private val latestLocation = locationTracker
        .latestLocation
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private val _state = MutableStateFlow(NearByRestaurantsState())
    val state = _state.asStateFlow()

    init {
        observeLatestLocation()
    }

    private fun observeLatestLocation() {
        //Load silently if there were previously rendered restaurants
        if (_state.value.restaurants.isEmpty()) {
            _state.value = _state.value.copy(
                isLoading = true,
                isError = false
            )
        }

        val errorHandler = CoroutineExceptionHandler { _, error ->
            _state.value = _state.value.copy(
                isLoading = false,
                isError = true,
                error = error
            )
        }

        viewModelScope.launch(errorHandler) {
            latestLocation
                .filterNotNull()
                .collectLatest { location ->
                    val nearByRestaurants = fetchNearByRestaurantsUseCase(location)
                    previewRestaurants(nearByRestaurants)
                    delay(10 * 1000L)
                }
        }
    }

    private fun previewRestaurants(restaurants: List<Restaurant>) {
        _state.value = _state.value.copy(
            isLoading = false,
            isError = false,
            restaurants = restaurants
        )
    }
}

data class NearByRestaurantsState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val error: Throwable? = null,
    val restaurants: List<Restaurant> = listOf()
)
