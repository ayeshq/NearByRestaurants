package com.wolt.nearbyrestaurants.ui.restaurants

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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantsViewModel @Inject constructor(
    private val fetchNearByRestaurants: FetchNearByRestaurantsUseCase,
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
            _state.update {
                state.value.copy(
                    uiState = UiState.Loading
                )
            }
        }

        val errorHandler = CoroutineExceptionHandler { _, error ->
            _state.update {
                _state.value.copy(
                    uiState = UiState.Error,
                    error = error
                )
            }
        }

        viewModelScope.launch(errorHandler) {
            latestLocation
                .filterNotNull()
                .collectLatest { location ->
                    val nearByRestaurants = fetchNearByRestaurants(location)
                    previewRestaurants(nearByRestaurants)
                    delay(10 * 1000L)
                }
        }
    }

    private fun previewRestaurants(restaurants: List<Restaurant>) {
        _state.update {
            _state.value.copy(
                uiState = if (restaurants.isEmpty()) {
                    UiState.Empty
                } else {
                    UiState.Preview
                },

                restaurants = restaurants
            )
        }
    }
}

data class NearByRestaurantsState(
    val uiState: UiState = UiState.Loading,
    val error: Throwable? = null,
    val restaurants: List<Restaurant> = listOf()
)

enum class UiState {

    // Shows a progressbar when loading if there were no cached restaurants
    Loading,

    Empty,

    Error,

    Preview
}
