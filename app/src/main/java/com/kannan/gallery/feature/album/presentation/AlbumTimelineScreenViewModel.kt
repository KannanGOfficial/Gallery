package com.kannan.gallery.feature.album.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kannan.gallery.core.presentation.navigation.NavigationScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlbumTimelineScreenViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumTimelineScreenUiState())
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = AlbumTimelineScreenUiState()
    )

    init {
        val albumTimelineScreen: NavigationScreen.AlbumTimeLineScreen = savedStateHandle.toRoute()
        updateAlbumNameUiState(albumName = albumTimelineScreen.albumName)
    }

    private val _uiEvent = Channel<AlbumTimelineScreenUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onUiAction(action: AlbumTimelineScreenUiAction) {
        when (action) {
            is AlbumTimelineScreenUiAction.OnImageClicked -> {
                sendEvent(
                    AlbumTimelineScreenUiEvent.NavigateTo(
                        NavigationScreen.AlbumMediaScreen(
                            action.position
                        )
                    )
                )
            }
        }
    }

    private fun sendEvent(event: AlbumTimelineScreenUiEvent) = viewModelScope.launch {
        _uiEvent.send(event)
    }


    private fun updateAlbumNameUiState(albumName: String) =
        _uiState.update {
            it.copy(
                albumName = albumName
            )
        }


}

sealed interface AlbumTimelineScreenUiAction {
    data class OnImageClicked(val position: Int) : AlbumTimelineScreenUiAction
}

sealed interface AlbumTimelineScreenUiEvent {
    data class NavigateTo(val navigationScreen: NavigationScreen) : AlbumTimelineScreenUiEvent
}

data class AlbumTimelineScreenUiState(
    val albumName: String = ""
)