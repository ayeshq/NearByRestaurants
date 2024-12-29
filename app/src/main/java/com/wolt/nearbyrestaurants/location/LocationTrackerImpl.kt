package com.wolt.nearbyrestaurants.location

import com.wolt.nearbyrestaurants.model.Location
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import java.util.concurrent.Executors
import javax.inject.Inject

class LocationTrackerImpl @Inject constructor(

) : LocationTracker {

    // A custom single thread dispatcher dedicated to only emitting the latest location
    private val locationsDispatcher = Executors
        .newSingleThreadExecutor()
        .asCoroutineDispatcher()

    private val locations = listOf(
        Location(60.169418, 24.931618),
        Location(60.169818, 24.932906),
        Location(60.170005, 24.935105),
        Location(60.169108, 24.936210),
        Location(60.168355, 24.934869),
        Location(60.167560, 24.932562),
        Location(60.168254, 24.931532),
        Location(60.169012, 24.930341),
        Location(60.170085, 24.929569)
    )

    override val latestLocation = flow {
        var index = 0
        while (true) {
            if (index == locations.size - 1) {
                index = 0
            }

            emit(locations[index])
            delay(10 * 1000L)
            index++
        }
    }.buffer(0) //Discard all emissions when there are no subscribers without stopping to simulate a walking user
        .shareIn(
            scope = CoroutineScope(locationsDispatcher),
            started = SharingStarted.Eagerly,
            replay = 0
        )
}
