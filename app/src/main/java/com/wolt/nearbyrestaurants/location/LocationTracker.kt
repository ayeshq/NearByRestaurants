package com.wolt.nearbyrestaurants.location

import com.wolt.nearbyrestaurants.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationTracker {

    val latestLocation: Flow<Location>
}
