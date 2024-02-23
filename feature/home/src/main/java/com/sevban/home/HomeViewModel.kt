package com.sevban.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sevban.domain.usecase.GetCharacterListUseCase
import com.sevban.domain.usecase.GetCharacterUseCase
import com.sevban.model.Character
import com.sevban.network.Failure
import com.sevban.network.util.ErrorResponse
import com.sevban.ui.util.handleFailures
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCharacterListUseCase: GetCharacterListUseCase,
    private val getCharacterUseCase: GetCharacterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _error = Channel<Failure>()
    val error = _error.receiveAsFlow()

    val characterState: StateFlow<Character?> = getCharacterUseCase.execute("1")
        .handleFailures {
            _error.send(it)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
}