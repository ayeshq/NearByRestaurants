package com.wolt.nearbyrestaurants.restaurants

import com.google.gson.reflect.TypeToken
import com.wolt.nearbyrestaurants.model.Location
import com.wolt.nearbyrestaurants.model.Restaurant
import com.wolt.nearbyrestaurants.model.VenuesResponse
import com.wolt.nearbyrestaurants.store.DataStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

private const val KEY_FAVOURITE_RESTAURANTS = "KEY_FAVOURITE_RESTAURANTS"
private val TYPE_FAVOURITES_LIST = object : TypeToken<List<String>>() {}.type

class RestaurantsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore
) : RestaurantsRepository {

    private val favourites = dataStore.readRecord<MutableList<String>>(KEY_FAVOURITE_RESTAURANTS, TYPE_FAVOURITES_LIST) ?: mutableListOf()

    private val service: RestaurantsService = initService()

    override suspend fun fetchNearByRestaurants(location: Location): List<Restaurant> {
        val response = service.fetchNearByRestaurants(
            location.latitude,
            location.longitude
        )

        val nearbyRestaurants: List<Restaurant> = response
            .sections
            .flatMap {
                it.items
            }
            .filter {
                it.venue != null
            }
            .take(15)
            .map { sectionItem ->
                Restaurant(
                    sectionItem,
                    favourites.contains(sectionItem.venue!!.id)
                )
            }

        return nearbyRestaurants
    }

    override fun toggleFavouriteRestaurant(restaurantId: String) {
        if (!favourites.remove(restaurantId)) {
            favourites.add(restaurantId)
        }
        dataStore.storeRecord(
            key = KEY_FAVOURITE_RESTAURANTS,
            record = favourites,
            recordType = TYPE_FAVOURITES_LIST
        )
    }

    private fun initService(): RestaurantsService {
        val baseUrl = "https://restaurant-api.wolt.com/"

        val httpClient: OkHttpClient =
            OkHttpClient
                .Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RestaurantsService::class.java)
    }

    private interface RestaurantsService {

        @GET("v1/pages/restaurants")
        suspend fun fetchNearByRestaurants(
            @Query("lat") latitude: Double,
            @Query("lon") longitude: Double
        ): VenuesResponse
    }
}
