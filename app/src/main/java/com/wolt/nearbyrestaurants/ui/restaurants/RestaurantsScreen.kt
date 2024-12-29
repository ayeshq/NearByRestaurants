package com.wolt.nearbyrestaurants.ui.restaurants

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.wolt.nearbyrestaurants.R
import com.wolt.nearbyrestaurants.model.Restaurant
import com.wolt.nearbyrestaurants.ui.theme.NearByRestaurantsTheme

@Composable
fun RestaurantsScreen(
    modifier: Modifier = Modifier,
    viewModel: RestaurantsViewModel = viewModel(),
    innerPadding: PaddingValues = PaddingValues(8.dp),
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.uiState) {
        UiState.Loading -> LoadingScreen()
        UiState.Empty -> EmptyScreen()
        UiState.Error -> ErrorScreen()
        UiState.Preview -> {
            RestaurantsList(
                modifier = modifier,
                innerPadding = innerPadding,
                restaurants = state.restaurants
            ) { id ->
                viewModel.onSaveRestaurantToggled(id)
            }
        }
    }
}

@Composable
fun ErrorScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.something_went_wrong),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.error,
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun EmptyScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.no_restaurants_near_you),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun LoadingScreen() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        verticalArrangement = Arrangement.spacedBy(42.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.loading_nearby_restaurants),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
            ),
        )
        CircularProgressIndicator()
    }
}

@Composable
fun RestaurantsList(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(8.dp),
    restaurants: List<Restaurant> = listOf(),
    onSaveToggled: (id : String) -> Unit
) {
    LazyColumn (
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(start = 8.dp, end = 8.dp),
    ) {
        items(
            count = restaurants.size,
            key = { index ->
                restaurants[index].id
            }
        ) { index: Int ->
            RestaurantCard(
                modifier = Modifier.animateItem(),
                id = restaurants[index].id,
                name = restaurants[index].name,
                imageUrl = restaurants[index].imageUrl,
                description = restaurants[index].shortDescription,
                isInFavourites = restaurants[index].isSaved,
                onSaveToggled = onSaveToggled
            )
        }
    }
}

@Composable
fun RestaurantCard(
    id: String,
    name: String,
    description: String,
    imageUrl: String,
    isInFavourites: Boolean = false,
    modifier: Modifier = Modifier,
    onSaveToggled: (id : String) -> Unit = {}
) {

    var inFavourites by rememberSaveable { mutableStateOf(isInFavourites) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.CenterVertically),
        shape = CardDefaults.elevatedShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterStart)
                )

                IconButton(
                    onClick = {
                        onSaveToggled(id)
                        inFavourites = !inFavourites
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = if (inFavourites) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (inFavourites) {
                            stringResource(R.string.remove_from_favourites)
                        } else {
                            stringResource(R.string.add_to_favourites)
                        },
                    )
                }
            }

            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                )
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Preview
@Composable
fun RestaurantCardPreview() {
    NearByRestaurantsTheme {
        RestaurantCard(
            id = "",
            name = "Dummy Restaurant",
            description = "Details....",
            imageUrl = "",
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun RestaurantCardPreviewNight() {
    NearByRestaurantsTheme {
        RestaurantCard(
            id = "",
            name = "Dummy Restaurant",
            description = "Details....",
            imageUrl = "",
        )
    }
}

@Preview
@Composable
fun RestaurantsListPreview() {
    NearByRestaurantsTheme {
        RestaurantsList(
            restaurants = listOf(
                Restaurant(
                    "1",
                    "Dummy Restaurant",
                    "Details....",
                    "",
                ),
                Restaurant(
                    "2",
                    "Dummy Restaurant",
                    "Details....",
                    ""
                )
            ),
            onSaveToggled = {}
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun LoadingScreenPreview() {
    NearByRestaurantsTheme {
        LoadingScreen()
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun EmptyScreenPreview() {
    NearByRestaurantsTheme {
        EmptyScreen()
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ErrorScreenPreview() {
    NearByRestaurantsTheme {
        ErrorScreen()
    }
}