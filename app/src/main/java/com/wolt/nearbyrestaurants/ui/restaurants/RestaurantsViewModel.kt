package com.wolt.nearbyrestaurants.ui.restaurants

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wolt.nearbyrestaurants.dispatchers.DispatchersProvider
import com.wolt.nearbyrestaurants.dispatchers.DispatchersProviderImpl
import com.wolt.nearbyrestaurants.location.LocationTracker
import com.wolt.nearbyrestaurants.model.Location
import com.wolt.nearbyrestaurants.model.Restaurant
import com.wolt.nearbyrestaurants.restaurants.RestaurantsRepository
import com.wolt.nearbyrestaurants.usecase.FetchNearByRestaurantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantsViewModel @Inject constructor(
    private val fetchNearByRestaurants: FetchNearByRestaurantsUseCase,
    private val restaurantsRepository: RestaurantsRepository,
    private val dispatchersProvider: DispatchersProvider = DispatchersProviderImpl(),
    locationTracker: LocationTracker
) : ViewModel() {

    private val _state = MutableStateFlow(NearByRestaurantsState())
    val state = _state.asStateFlow()

    private var fetchRestaurantsJob: Job? = null

    init {
        viewModelScope
            .launch(dispatchersProvider.mainDispatcher) {
                locationTracker
                    .latestLocation
                    .collectLatest(::fetchRestaurantsNear)
            }
    }

    fun onSaveRestaurantToggled(restaurantId: String) {
        _state.update {
            _state.value.copy(
                restaurants = _state.value.restaurants.also { list ->
                    list.find {
                        it.id == restaurantId
                    }?.let {
                        it.isSaved = !it.isSaved
                    }
                }
            )
        }
        restaurantsRepository.toggleFavouriteRestaurant(restaurantId)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun fetchRestaurantsNear(location: Location) {
        fetchRestaurantsJob?.cancel()

        //Load silently if there were previously rendered restaurants
        if (_state.value.restaurants.isEmpty()) {
            _state.update {
                _state.value.copy(
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

        fetchRestaurantsJob = viewModelScope.launch(errorHandler + dispatchersProvider.mainDispatcher) {
            val nearByRestaurants = fetchNearByRestaurants(location)
            previewRestaurantsOrEmpty(nearByRestaurants)
        }
    }

    private fun previewRestaurantsOrEmpty(restaurants: List<Restaurant>) {
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
