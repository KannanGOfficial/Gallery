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

val albumList1 = listOf(
    Album(
        id = 0,
        name = "Camera",
        coverImage = ""
    ),
    Album(
        id = 1,
        name = "Camera",
        coverImage = ""
    ),
    Album(
        id = 2,
        name = "Camera",
        coverImage = ""
    ),
    Album(
        id = 3,
        name = "Camera",
        coverImage = ""
    ),
    Album(
        id = 4,
        name = "Camera",
        coverImage = ""
    ),
    Album(
        id = 5,
        name = "Camera",
        coverImage = ""
    ),
    Album(
        id = 6,
        name = "Camera",
        coverImage = ""
    ),
    Album(
        id = 7,
        name = "Camera",
        coverImage = ""
    ),
    Album(
        id = 8,
        name = "Camera",
        coverImage = ""
    ),
    Album(
        id = 9,
        name = "Camera",
        coverImage = ""
    ),
)

class AlbumScreenViewModel : ViewModel() {

    init {
        /*val albumList = listOf(Album(
            id = 1,
            name = "Album",
            coverImage = ""
        ))
        updateAlbumListUiState(albumList)*/
    }

    private val _uiState = MutableStateFlow(AlbumScreenUiState())
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = AlbumScreenUiState()
    )

    private val _uiEvent = Channel<AlbumScreenUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onUiAction(action: AlbumScreenUiAction) {
        when (action) {
            is AlbumScreenUiAction.OnAlbumClicked -> sendEvent(
                AlbumScreenUiEvent.NavigateTo(
                    NavigationScreen.AlbumTimeLineScreen
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
    val albumList: List<Album> = albumList1
)

sealed interface AlbumScreenUiAction {
    data class OnAlbumClicked(val albumId: Long) : AlbumScreenUiAction
}

sealed interface AlbumScreenUiEvent {
    data class NavigateTo(val navigationScreen: NavigationScreen) : AlbumScreenUiEvent
}

