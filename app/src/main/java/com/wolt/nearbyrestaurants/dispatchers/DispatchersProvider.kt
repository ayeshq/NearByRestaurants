package com.wolt.nearbyrestaurants.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersProvider {

    val mainDispatcher: CoroutineDispatcher

    val ioDispatcher: CoroutineDispatcher

    val defaultDispatcher: CoroutineDispatcher

    val unConfinedDispatcher: CoroutineDispatcher

    val workerThreadDispatcher: CoroutineDispatcher
}
