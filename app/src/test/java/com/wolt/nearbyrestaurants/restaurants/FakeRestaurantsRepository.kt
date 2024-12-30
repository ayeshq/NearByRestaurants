package com.wolt.nearbyrestaurants.restaurants

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wolt.nearbyrestaurants.model.Location
import com.wolt.nearbyrestaurants.model.Restaurant
import java.io.InputStreamReader

class FakeRestaurantsRepository : RestaurantsRepository {

    var error = false
    var empty = false

    private val fakeRestaurants = restaurantsListStub()

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

    /**
     * Creates a list of fake restaurants
     */
    private fun restaurantsListStub(): List<Restaurant> {
        val inputStream = this::class.java.classLoader!!.getResourceAsStream("restaurants.json")
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<List<Restaurant>>() {}.type
        val restaurantsList = Gson().fromJson<List<Restaurant>>(
            reader,
            type
        )
        return restaurantsList
    }
}
