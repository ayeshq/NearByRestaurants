package com.wolt.nearbyrestaurants.dispatchers

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatchersProviderModule::class]
)
object DispatchersProviderTestModule {

    @Provides
    @Singleton
    fun provideDispatchersProvider(
        dispatchersProvider: TestDispatchersProvider
    ): DispatchersProvider = dispatchersProvider
}
