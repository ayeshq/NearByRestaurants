package com.wolt.nearbyrestaurants.restaurants

import com.wolt.nearbyrestaurants.model.Location
import com.wolt.nearbyrestaurants.model.Restaurant

class FakeRestaurantsRepository : RestaurantsRepository {

    var error = false
    var empty = false

    private val fakeRestaurants = listOf(
        Restaurant(
            id = "",
            name = "Fake",
            imageUrl = "",
            shortDescription = "",
            isSaved = false
        )
    )

    override suspend fun fetchNearByRestaurants(
        location: Location
    ) = if (error) {
        throw Exception()
    } else if (empty) {
        listOf()
    } else {
        fakeRestaurants
    }

    override fun toggleFavouriteRestaurant(restaurantId: String) {
//        TODO("Not yet implemented")
    }
}
