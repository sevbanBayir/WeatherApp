package com.sevban.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sevban.model.Character
import com.sevban.network.Failure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreenRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onListItemClicked: (String) -> Unit,     //-> hoist navigation actions to appState's navController.
    whenErrorOccured: suspend (Failure, String?) -> Unit,
) {
    val homeUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val character by viewModel.characterState.collectAsStateWithLifecycle()
    val error = viewModel.error
    HomeScreen(
        homeUiState = homeUiState,
        onListItemClicked = onListItemClicked,
        character = character,
        error = error,
        whenErrorOccured = whenErrorOccured
    )
}

@Composable
fun HomeScreen(
    homeUiState: UiState,
    character: Character? = null,
    onListItemClicked: (String) -> Unit,
    error: Flow<Failure>,
    whenErrorOccured: suspend (Failure, String?) -> Unit
) {
    LaunchedEffect(key1 = true) {
        error.collectLatest {
            whenErrorOccured(it, null)
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Home Feature Screen")
        Button(onClick = { onListItemClicked("5") }) {
            Text(text = "Navigate to Detail")
        }
        Text(text = character?.name ?: "Empty")
    }
}