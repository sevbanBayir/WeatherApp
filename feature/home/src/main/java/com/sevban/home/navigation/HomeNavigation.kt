package com.sevban.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sevban.home.HomeScreenRoute


fun NavController.navigateToHome() {
    navigate("home/") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.homeScreen(
    whenErrorOccured: suspend (Throwable, String?) -> Unit
) {
    composable(
        route = "home"
    ) {
        HomeScreenRoute(
            whenErrorOccurred = whenErrorOccured,
        )
    }
}
