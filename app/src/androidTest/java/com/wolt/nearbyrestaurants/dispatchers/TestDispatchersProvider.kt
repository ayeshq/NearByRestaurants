package com.wolt.nearbyrestaurants.dispatchers

import kotlinx.coroutines.test.StandardTestDispatcher
import javax.inject.Inject

class TestDispatchersProvider @Inject constructor() : DispatchersProvider {

    val testDispatcher = StandardTestDispatcher()

    override val mainDispatcher = testDispatcher

    override val ioDispatcher = testDispatcher

    override val defaultDispatcher = testDispatcher

    override val unConfinedDispatcher = testDispatcher

    //Using a different instance of the StandardTestDispatcher here,
    //Since we're calling: [dispatchersProvider.testDispatcher.scheduler.advanceUntilIdle()] in our unite tests!
    //This will suspend the test coroutine until all emissions are sent which in the case of LocationTracker,
    //will lead to suspending the test coroutine indefinitely, because the location tracker will never stop emitting new locations.
    override val workerThreadDispatcher = StandardTestDispatcher()
}
