package com.kannan.gallery.presentation.feature.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kannan.gallery.domain.model.Media
import com.kannan.gallery.presentation.feature.photo.ScreenContentType
import com.kannan.gallery.presentation.main.dummyAlbumMediaList
import com.kannan.gallery.presentation.navigation.NavigationScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlbumDetailScreenViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumDetailScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<AlbumDetailScreenUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val mediaList = dummyAlbumMediaList

    init {
        val albumName = savedStateHandle.toRoute<NavigationScreen.AlbumDetailScreen>().albumName

        updateAlbumName(albumName)
    }

    fun onUiAction(action: AlbumDetailScreenUiAction) {
        when (action) {
            AlbumDetailScreenUiAction.OnTimelineContentBackPressed -> {
                sendEvent(AlbumDetailScreenUiEvent.NavigateUp)
            }

            is AlbumDetailScreenUiAction.OnMediaContentBackPressed -> {
                updateCurrentPosition(action.currentMediaPosition)
                updateScreenTypeUiState(ScreenContentType.TIMELINE)
            }

            is AlbumDetailScreenUiAction.OnImageClicked -> {
                val currentPosition = mediaList.indexOf(action.media)
                updateCurrentPosition(currentPosition)

                updateScreenTypeUiState(ScreenContentType.MEDIA)
            }

            is AlbumDetailScreenUiAction.OnImageLongClicked -> {

            }
        }
    }

    private fun updateScreenTypeUiState(screenContentType: ScreenContentType): Unit =
        _uiState.update {
            it.copy(
                screenContentType = screenContentType
            )
        }

    private fun updateCurrentPosition(currentPosition: Int): Unit =
        _uiState.update {
            it.copy(
                currentMediaPosition = currentPosition
            )
        }

    private fun updateAlbumName(albumName: String): Unit =
        _uiState.update {
            it.copy(
                albumName = albumName
            )
        }

    private fun sendEvent(event: AlbumDetailScreenUiEvent) = viewModelScope.launch {
        _uiEvent.send(event)
    }
}

data class AlbumDetailScreenUiState(
    val screenContentType: ScreenContentType = ScreenContentType.TIMELINE,
    val currentMediaPosition: Int = 0,
    val albumName: String = ""
)

sealed interface AlbumDetailScreenUiAction {
    data class OnImageClicked(val media: Media) : AlbumDetailScreenUiAction
    data class OnImageLongClicked(val media: Media) : AlbumDetailScreenUiAction
    data object OnTimelineContentBackPressed : AlbumDetailScreenUiAction
    data class OnMediaContentBackPressed(val currentMediaPosition: Int) : AlbumDetailScreenUiAction
}

sealed interface AlbumDetailScreenUiEvent {
    data object NavigateUp : AlbumDetailScreenUiEvent
}