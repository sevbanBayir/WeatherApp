package com.sevban.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sevban.common.model.Failure


fun NavController.navigateToHome() {
    navigate("home/") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.homeScreen(
    whenErrorOccured: suspend (Failure, String?) -> Unit
) {
    composable(
        route = "home"
    ) {
        HomeScreenRoute(
            whenErrorOccured = whenErrorOccured,
        )
    }
}
