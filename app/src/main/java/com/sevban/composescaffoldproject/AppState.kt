package com.sevban.composescaffoldproject

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sevban.composescaffoldproject.navigation.Destination
import com.sevban.composescaffoldproject.navigation.Destination.DETAIL
import com.sevban.composescaffoldproject.navigation.Destination.HOME
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): AppState {

    return remember(
        navController,
        coroutineScope,

        ) {
        AppState(
            navController,
            coroutineScope,
        )
    }
}

@Stable
class AppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: Destination?
        @Composable get() = when (currentDestination?.route) {
            HOME.route -> HOME
            DETAIL.route -> DETAIL
            else -> null
        }
}