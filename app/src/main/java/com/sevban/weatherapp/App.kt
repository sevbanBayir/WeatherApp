package com.sevban.weatherapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.sevban.weatherapp.navigation.WeatherAppNavHost

@Composable
fun App(
    appState: AppState = rememberAppState()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold (
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { scaffoldPadding ->
        WeatherAppNavHost(
            appState = appState,
            onShowSnackbar = { failure, actionLabel ->
                snackbarHostState.showSnackbar(failure.message ?: "", actionLabel)
            },
            modifier = Modifier.padding(scaffoldPadding)
        )
    }
}