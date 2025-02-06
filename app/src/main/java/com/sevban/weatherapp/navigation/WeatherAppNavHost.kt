package com.sevban.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.sevban.detail.detailScreen
import com.sevban.detail.navigateToDetail
import com.sevban.home.navigation.Home
import com.sevban.home.navigation.homeScreen
import com.sevban.home.navigation.navigateToHome
import com.sevban.location.navigation.locationScreen
import com.sevban.location.navigation.navigateToLocationScreen
import com.sevban.weatherapp.AppState

@Composable
fun WeatherAppNavHost(
    appState: AppState,
    onShowSnackbar: suspend (Throwable, String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = Home(),
        modifier = modifier,
    ) {
        homeScreen(
            whenErrorOccured = onShowSnackbar,
            onLocationClick = navController::navigateToLocationScreen,
            onFutureDaysForecastClick = { lat, long ->
                navController.navigateToDetail(lat, long)
            }
        )

        detailScreen(
            whenErrorOccured = onShowSnackbar,
        )

        locationScreen(
            whenErrorOccured = onShowSnackbar,
            onClickWeather = { lat, long -> navController.navigateToHome(lat, long) }
        )
    }
}
