package com.wolt.nearbyrestaurants.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideFetchNearByRestaurantsUseCase(
        useCase: FetchNearByRestaurantsUseCaseImpl
    ): FetchNearByRestaurantsUseCase = useCase
}
