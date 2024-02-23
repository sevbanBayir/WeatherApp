package com.sevban.composescaffoldproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.sevban.composescaffoldproject.AppState
import com.sevban.detail.detailScreen
import com.sevban.detail.navigateToDetail
import com.sevban.home.homeScreen
import com.sevban.network.Failure
import kotlinx.coroutines.launch

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
