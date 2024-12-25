package com.wolt.nearbyrestaurants.usecase

import com.wolt.nearbyrestaurants.model.Location
import com.wolt.nearbyrestaurants.restaurants.RestaurantsRepository
import javax.inject.Inject

class FetchNearByRestaurantsUseCaseImpl @Inject constructor(
    private val restaurantsRepository: RestaurantsRepository
) : FetchNearByRestaurantsUseCase {

    override suspend operator fun invoke(location: Location) = restaurantsRepository.fetchNearByRestaurants(location)
}
