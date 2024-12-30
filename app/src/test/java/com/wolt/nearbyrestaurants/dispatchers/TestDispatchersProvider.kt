package com.wolt.nearbyrestaurants.dispatchers

import kotlinx.coroutines.test.StandardTestDispatcher

class TestDispatchersProvider : DispatchersProvider {

    val testDispatcher = StandardTestDispatcher()

    override val mainDispatcher = testDispatcher

    override val ioDispatcher = testDispatcher

    override val defaultDispatcher = testDispatcher

}
