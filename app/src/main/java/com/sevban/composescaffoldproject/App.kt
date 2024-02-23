package com.sevban.composescaffoldproject

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sevban.composescaffoldproject.navigation.NavHost

@Composable
fun App(
    appState: AppState = rememberAppState()
) {
    var showSnackbar by remember {
        mutableStateOf(false)
    }

    var showSnackbarData by remember {
        mutableStateOf("")
    }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold (
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { scaffoldPadding ->
        NavHost(
            appState = appState,
            onShowSnackbar = { failure, actionLabel ->
                snackbarHostState.showSnackbar(failure.message ?: "", actionLabel)
            },
            modifier = Modifier.padding(scaffoldPadding)
        )

        if (showSnackbar)
            Snackbar(Modifier.padding(16.dp)) {
                Text(text = showSnackbarData)
            }
    }
}