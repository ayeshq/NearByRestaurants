package com.wolt.nearbyrestaurants.dispatchers

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatchersProviderModule {

    @Provides
    @Singleton
    fun provideDispatchersProvider(
        dispatchersProvider: DispatchersProviderImpl
    ): DispatchersProvider = dispatchersProvider
}
