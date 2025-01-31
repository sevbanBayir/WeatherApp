package com.sevban.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.sevban.common.model.Failure
import com.sevban.detail.detailScreen
import com.sevban.home.navigation.homeScreen
import com.sevban.weatherapp.AppState

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
            whenErrorOccured = onShowSnackbar
        )
        detailScreen(onBackClick = navController::popBackStack)
    }
}
