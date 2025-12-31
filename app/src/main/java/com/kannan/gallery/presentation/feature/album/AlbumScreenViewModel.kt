package com.kannan.gallery.presentation.feature.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kannan.gallery.domain.GalleryRepository
import com.kannan.gallery.domain.model.Album
import com.kannan.gallery.presentation.navigation.NavigationScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumScreenViewModel @Inject constructor(
    private val repository: GalleryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumScreenUiState())
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = AlbumScreenUiState()
    )

    init {
        viewModelScope.launch {
            val albumList = repository.getAllAlbum()
            updateAlbumListUiState(albumList)
        }
    }

    private val _uiEvent = Channel<AlbumScreenUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onUiAction(action: AlbumScreenUiAction) {
        when (action) {
            is AlbumScreenUiAction.OnAlbumClicked -> sendEvent(
                AlbumScreenUiEvent.NavigateTo(
                    NavigationScreen.AlbumDetailScreen(
                        albumName = action.album.name,
                        albumId = action.album.id
                    )
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
    data class OnAlbumClicked(val album: Album) : AlbumScreenUiAction
}

sealed interface AlbumScreenUiEvent {
    data class NavigateTo(val navigationScreen: NavigationScreen) : AlbumScreenUiEvent
}

val albumList1 = (0..10).map { index ->
    Album(
        id = index.toLong(),
        name = "Camera",
        coverImage = "",
        relativePath = ""
    )
}

