package com.wolt.nearbyrestaurants.usecase

import com.wolt.nearbyrestaurants.model.Location
import com.wolt.nearbyrestaurants.model.Restaurant

interface FetchNearByRestaurantsUseCase {

    suspend operator fun invoke(location: Location): List<Restaurant>
}
