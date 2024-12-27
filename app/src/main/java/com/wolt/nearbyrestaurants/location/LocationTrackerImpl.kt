package com.wolt.nearbyrestaurants.location

import com.wolt.nearbyrestaurants.model.Location
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocationTrackerImpl @Inject constructor(

) : LocationTracker {

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
            if (index == locations.size -1) {
                index = 0
            }

            emit(locations[index])
            delay(3 * 1000L)
            index++
        }
    }
}
