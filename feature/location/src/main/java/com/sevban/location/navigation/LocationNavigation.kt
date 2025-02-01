package com.sevban.location.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sevban.location.LocationScreenRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToLocationScreen() {
    navigate(Location) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.locationScreen(
    whenErrorOccured: suspend (Throwable, String?) -> Unit,
) {
    composable<Location> {
        LocationScreenRoute(
            whenErrorOccurred = whenErrorOccured,
        )
    }
}

@Serializable
data object Location
