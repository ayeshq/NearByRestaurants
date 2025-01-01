package com.wolt.nearbyrestaurants.restaurants

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RestaurantsRepositoryModule::class]
)
@Module
object RestaurantsRepositoryTestModule {

    @Provides
    @Singleton
    fun provideRestaurantsRepository(
        repository: FakeRestaurantsRepository
    ): RestaurantsRepository = repository
}
