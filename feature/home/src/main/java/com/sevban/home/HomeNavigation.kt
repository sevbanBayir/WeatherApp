package com.sevban.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sevban.network.Failure
import java.net.URLDecoder
import java.net.URLEncoder


fun NavController.navigateToHome() {
    navigate("home/") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.homeScreen(
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit,   // -> keep hoisting...
    whenErrorOccured: suspend (Failure, String?) -> Unit
) {
    composable(
        route = "home"
    ) {
        HomeScreenRoute(
            whenErrorOccured = whenErrorOccured,
            onListItemClicked = onItemClick
        )
    }
}
