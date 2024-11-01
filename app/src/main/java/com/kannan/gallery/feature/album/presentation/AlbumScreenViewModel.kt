package com.kannan.gallery.feature.album.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kannan.gallery.core.presentation.navigation.NavigationScreen
import com.kannan.gallery.feature.album.domain.Album
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlbumScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumScreenUiState())
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = AlbumScreenUiState()
    )

    init {
        updateAlbumListUiState(albumList1)
    }

    private val _uiEvent = Channel<AlbumScreenUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onUiAction(action: AlbumScreenUiAction) {
        when (action) {
            is AlbumScreenUiAction.OnAlbumClicked -> sendEvent(
                AlbumScreenUiEvent.NavigateTo(
                    NavigationScreen.MemoriesScreen(action.albumName)
                )
            )
        }
    }

    private fun updateAlbumListUiState(albumList: List<Album>) =
        _uiState.update {
            it.copy(
                albumList = albumList
            )
        }

    private fun sendEvent(event: AlbumScreenUiEvent) = viewModelScope.launch {
        _uiEvent.send(event)
    }

}

data class AlbumScreenUiState(
    val albumList: List<Album> = emptyList()
)

sealed interface AlbumScreenUiAction {
    data class OnAlbumClicked(val albumName: String) : AlbumScreenUiAction
}

sealed interface AlbumScreenUiEvent {
    data class NavigateTo(val navigationScreen: NavigationScreen) : AlbumScreenUiEvent
}

val albumList1 = (0..10).map { index ->
    Album(
        id = index.toLong(),
        name = "Camera",
        coverImage = ""
    )
}

