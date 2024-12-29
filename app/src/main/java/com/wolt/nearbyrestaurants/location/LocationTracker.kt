package com.wolt.nearbyrestaurants.location

import com.wolt.nearbyrestaurants.model.Location
import kotlinx.coroutines.flow.SharedFlow

interface LocationTracker {

    val latestLocation: SharedFlow<Location>
}
