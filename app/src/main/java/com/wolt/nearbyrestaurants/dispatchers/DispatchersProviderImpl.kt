package com.wolt.nearbyrestaurants.dispatchers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import javax.inject.Inject

class DispatchersProviderImpl @Inject constructor() : DispatchersProvider {

    override val mainDispatcher = Dispatchers.Main

    override val ioDispatcher = Dispatchers.IO

    override val defaultDispatcher = Dispatchers.Default

    override val unConfinedDispatcher = Dispatchers.Unconfined

    override val workerThreadDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
}
