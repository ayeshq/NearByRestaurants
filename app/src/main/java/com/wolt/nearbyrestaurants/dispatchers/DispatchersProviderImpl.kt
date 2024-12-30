package com.wolt.nearbyrestaurants.dispatchers

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DispatchersProviderImpl @Inject constructor() : DispatchersProvider {

    override val mainDispatcher = Dispatchers.Main

    override val ioDispatcher = Dispatchers.IO

    override val defaultDispatcher = Dispatchers.Default
}
