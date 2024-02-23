package com.sevban.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.sevban.weatherapp.AppState
import com.sevban.detail.detailScreen
import com.sevban.detail.navigateToDetail
import com.sevban.home.homeScreen
import com.sevban.network.Failure

@Composable
fun NavHost(
    appState: AppState,
    onShowSnackbar: suspend (Failure, String?) -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = Destination.HOME.route,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        homeScreen(
            onItemClick = navController::navigateToDetail,
            onBackClick = navController::popBackStack,
            whenErrorOccured = onShowSnackbar
        )
        detailScreen(onBackClick = navController::popBackStack)
    }
}
