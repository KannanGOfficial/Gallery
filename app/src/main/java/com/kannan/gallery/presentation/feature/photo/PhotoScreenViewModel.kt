package com.kannan.gallery.presentation.feature.photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kannan.gallery.data.dummyTimelineMediaList
import com.kannan.gallery.domain.model.Media
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhotoScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PhotoScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<PhotoScreenUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val mediaList = dummyTimelineMediaList

    fun onUiAction(action: PhotoScreenUiAction) {
        when (action) {
            PhotoScreenUiAction.OnTimelineContentBackPressed -> {
                sendEvent(PhotoScreenUiEvent.NavigateUp)
            }

            is PhotoScreenUiAction.OnMediaContentBackPressed -> {
                updateCurrentPosition(action.currentMediaPosition)
                updateScreenTypeUiState(ScreenContentType.TIMELINE)
            }

            is PhotoScreenUiAction.OnImageClicked -> {
                val currentPosition = mediaList.indexOf(action.media)
                updateCurrentPosition(currentPosition)

                updateScreenTypeUiState(ScreenContentType.MEDIA)
            }

            is PhotoScreenUiAction.OnImageLongClicked -> {

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

    private fun sendEvent(event: PhotoScreenUiEvent) = viewModelScope.launch {
        _uiEvent.send(event)
    }

}

data class PhotoScreenUiState(
    val screenContentType: ScreenContentType = ScreenContentType.TIMELINE,
    val currentMediaPosition: Int = 0
)

sealed interface PhotoScreenUiAction {
    data class OnImageClicked(val media: Media) : PhotoScreenUiAction
    data class OnImageLongClicked(val media: Media) : PhotoScreenUiAction
    data object OnTimelineContentBackPressed : PhotoScreenUiAction
    data class OnMediaContentBackPressed(val currentMediaPosition: Int) : PhotoScreenUiAction
}

sealed interface PhotoScreenUiEvent {
    data object NavigateUp : PhotoScreenUiEvent
}

enum class ScreenContentType {
    TIMELINE,
    MEDIA
}