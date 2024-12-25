package com.wolt.nearbyrestaurants.restaurants

import com.wolt.nearbyrestaurants.model.Location
import com.wolt.nearbyrestaurants.model.Restaurant

interface RestaurantsRepository {

    suspend fun fetchNearByRestaurants(location: Location): List<Restaurant>
}
