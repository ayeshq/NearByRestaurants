package com.wolt.nearbyrestaurants.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    viewModel: HomeViewModel = viewModel()
) {
//    viewModel.fetch()

    LazyColumn(
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        items(15) {

        }
    }
}
