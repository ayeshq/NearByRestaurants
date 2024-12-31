package com.wolt.nearbyrestaurants.ui.restaurants

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wolt.nearbyrestaurants.dispatchers.TestDispatchersProvider
import com.wolt.nearbyrestaurants.location.LocationTrackerImpl
import com.wolt.nearbyrestaurants.model.Location
import com.wolt.nearbyrestaurants.restaurants.FakeRestaurantsRepository
import com.wolt.nearbyrestaurants.usecase.FetchNearByRestaurantsUseCaseImpl
import kotlinx.coroutines.test.runTest
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
        locationTracker = LocationTrackerImpl(dispatchersProvider),
        dispatchersProvider = dispatchersProvider
    )

    @Test
    fun `fetching at least one restaurant will change the state to Preview`() = runTest {
        repository.error = false
        repository.empty = false

        viewModel.state.test {
            viewModel.fetchRestaurantsNear(fakeLocation)
            dispatchersProvider.testDispatcher.scheduler.advanceUntilIdle()
            var state = awaitItem()
            assertThat(state.uiState).isEqualTo(UiState.Loading)

            state = awaitItem()
            assertThat(state.uiState).isEqualTo(UiState.Preview)
        }
    }

    @Test
    fun `throwing an error while fetching restaurants will change the state to Error`() = runTest {
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

    @Test
    fun `when there are no restaurants near provided location, state will change the to Empty`() = runTest {
        repository.error = false
        repository.empty = true

        viewModel.state.test {
            viewModel.fetchRestaurantsNear(fakeLocation)
            dispatchersProvider.testDispatcher.scheduler.advanceUntilIdle()
            var state = awaitItem()
            assertThat(state.uiState).isEqualTo(UiState.Loading)

            state = awaitItem()
            assertThat(state.uiState).isEqualTo(UiState.Empty)
        }
    }

    @Test
    fun `adding or removing a restaurant from favourites will properly mark the restaurant as saved or not`() = runTest {
        //First load none empty list of restaurants
        repository.error = false
        repository.empty = false

        viewModel.state.test {
            viewModel.fetchRestaurantsNear(fakeLocation)
            dispatchersProvider.testDispatcher.scheduler.advanceUntilIdle()
            var state = awaitItem()
            assertThat(state.uiState).isEqualTo(UiState.Loading)

            state = awaitItem()
            assertThat(state.uiState).isEqualTo(UiState.Preview)

            //Add the first restaurant in the list to favourites
            viewModel.onSaveRestaurantToggled(state.restaurants[0].id)
            //Assert the restaurant is saved
            assertThat(state.restaurants[0].isSaved).isTrue()

            //Now remove the same restaurant from favourites
            viewModel.onSaveRestaurantToggled(state.restaurants[0].id)
            //Assert the restaurant is not saved anymore
            assertThat(state.restaurants[0].isSaved).isFalse()
        }
    }
}
