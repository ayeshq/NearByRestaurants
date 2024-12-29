package com.wolt.nearbyrestaurants.ui.restaurants

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
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
    viewModel: RestaurantsViewModel = viewModel(),
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.uiState) {
        UiState.Loading -> LoadingScreen()
        UiState.Empty -> EmptyScreen()
        UiState.Error -> ErrorScreen() // A snackbar would be more appropriate!
        UiState.Preview -> {
            RestaurantsList(
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

        Row(
            modifier = Modifier
                .align(Alignment.Center)
                // Merge elements for accessibility purposes
                .semantics(mergeDescendants = true) {}
        ) {
            Icon(
                imageVector = Icons.Outlined.Warning,
                contentDescription = null, // decorative
                modifier = Modifier
                    .size(42.dp)
                    .padding(end = 8.dp),
                tint = MaterialTheme.colorScheme.onError,
            )
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(R.string.something_went_wrong),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onError,
                ),
            )
        }
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
    restaurants: List<Restaurant> = listOf(),
    onSaveToggled: (id : String) -> Unit
) {
    LazyColumn (
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            count = restaurants.size,
            key = { index ->
                restaurants[index].id
            }
        ) { index: Int ->
            RestaurantCard(
                modifier = Modifier.animateItem(),
                restaurantId = restaurants[index].id,
                restaurantName = restaurants[index].name,
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
    restaurantId: String,
    restaurantName: String,
    description: String,
    imageUrl: String,
    isInFavourites: Boolean = false,
    modifier: Modifier = Modifier,
    onSaveToggled: (id : String) -> Unit = {}
) {

    var inFavourites by remember { mutableStateOf(isInFavourites) }

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
                    contentDescription = restaurantName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterStart)
                )

                IconToggleButton(
                    checked = inFavourites,
                    onCheckedChange = {
                        inFavourites = !inFavourites
                        onSaveToggled(restaurantId)
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterEnd)
                ) {
                    if (inFavourites) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = stringResource(
                                R.string.remove_from_favourites,
                                restaurantName
                            )
                        )
                    } else {
                        Icon(
                            Icons.Filled.FavoriteBorder,
                            contentDescription = stringResource(
                                R.string.add_to_favourites,
                                restaurantName
                            )
                        )
                    }
                }
            }

            Text(
                text = restaurantName,
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
            restaurantId = "",
            restaurantName = "Dummy Restaurant",
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
            restaurantId = "",
            restaurantName = "Dummy Restaurant",
            description = "Details....",
            imageUrl = "",
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun RestaurantsListPreview() {
    NearByRestaurantsTheme {
        RestaurantsList(
            restaurants = listOf(
                Restaurant(
                    id = "1",
                    name = "First Restaurant",
                    shortDescription = "Details....",
                    imageUrl = "",
                ),
                Restaurant(
                    id = "2",
                    name = "Second Restaurant",
                    shortDescription = "Details....",
                    imageUrl = "",
                    isSaved = true
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