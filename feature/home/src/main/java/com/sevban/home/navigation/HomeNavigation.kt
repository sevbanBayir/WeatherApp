package com.sevban.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sevban.home.HomeScreenRoute
import kotlinx.serialization.Serializable

fun NavController.navigateToHome(
    latitude: Double? = null,
    longitude: Double? = null,
) {
    navigate(Home(latitude, longitude)) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.homeScreen(
    whenErrorOccured: suspend (Throwable, String?) -> Unit,
    onLocationClick: () -> Unit,
    onFutureDaysForecastClick: (Double, Double) -> Unit
) {
    composable<Home> {
        HomeScreenRoute(
            whenErrorOccurred = whenErrorOccured,
            onLocationClick = onLocationClick,
            onFutureDaysForecastClick = onFutureDaysForecastClick
        )
    }
}

@Serializable
data class Home(val latitude: Double? = null, val longitude: Double? = null)

