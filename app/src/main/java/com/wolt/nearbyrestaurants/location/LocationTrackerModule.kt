package com.wolt.nearbyrestaurants.location

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationTrackerModule {

    @Provides
    @Singleton
    fun provideLocationTracker(
        locationTracker: LocationTrackerImpl
    ): LocationTracker = locationTracker
}
