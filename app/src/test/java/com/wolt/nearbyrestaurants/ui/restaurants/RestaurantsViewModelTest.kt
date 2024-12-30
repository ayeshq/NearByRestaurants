package com.wolt.nearbyrestaurants.ui.restaurants

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wolt.nearbyrestaurants.dispatchers.TestDispatchersProvider
import com.wolt.nearbyrestaurants.location.LocationTrackerImpl
import com.wolt.nearbyrestaurants.model.Location
import com.wolt.nearbyrestaurants.restaurants.FakeRestaurantsRepository
import com.wolt.nearbyrestaurants.usecase.FetchNearByRestaurantsUseCaseImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RestaurantsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val fakeLocation = Location(
        latitude = 60.169418,
        longitude = 24.931618
    )

    private val repository = FakeRestaurantsRepository()
    private val fetchUseCase = FetchNearByRestaurantsUseCaseImpl(repository)
    private val dispatchersProvider = TestDispatchersProvider()
    private val viewModel = RestaurantsViewModel(
        fetchNearByRestaurants = fetchUseCase,
        restaurantsRepository = repository,
        locationTracker = LocationTrackerImpl(),
        dispatchersProvider = dispatchersProvider
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `throwing an error while fetching restaurants will change the state to error`() = runTest {
        repository.error = true
        repository.empty = false

        viewModel.state.test {
            viewModel.fetchRestaurantsNear(fakeLocation)
            dispatchersProvider.testDispatcher.scheduler.advanceUntilIdle()
            var state = awaitItem()
            assertThat(state.uiState).isEqualTo(UiState.Loading)

            state = awaitItem()
            assertThat(state.uiState).isEqualTo(UiState.Error)
        }
    }
}
