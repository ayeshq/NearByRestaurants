package com.wolt.nearbyrestaurants.restaurants

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RestaurantsRepositoryModule {

    @Provides
    @Singleton
    fun provideRestaurantsRepository(
        repository: RestaurantsRepositoryImpl
    ): RestaurantsRepository = repository
}
