package com.wolt.nearbyrestaurants.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.wolt.nearbyrestaurants.R
import com.wolt.nearbyrestaurants.ui.restaurants.RestaurantsScreen
import com.wolt.nearbyrestaurants.ui.theme.NearByRestaurantsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearByRestaurantsApp() {
    NearByRestaurantsTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.near_by_restaurants),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        ) { innerPadding: PaddingValues ->
            RestaurantsScreen(
                innerPadding = innerPadding,
            )
        }
    }
}
