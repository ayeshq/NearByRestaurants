package com.wolt.nearbyrestaurants.ui.restaurants

import androidx.activity.viewModels
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.wolt.nearbyrestaurants.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class RestaurantsScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var viewModel: RestaurantsViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.setContent {
            viewModel = composeTestRule.activity.viewModels<RestaurantsViewModel>().value
            RestaurantsScreen(viewModel)
        }
    }

    @Test
    fun app_displays_restaurants_list() {
        //assert the list is displayed
        composeTestRule.onNodeWithTag("RestaurantsScreenTag").assertIsDisplayed()

        //assert all items exist within the tree
//        dummyItems.forEach { item ->
//            composeTestRule.onNodeWithText(item.name).assertExists()
//        }
    }
}
