package com.kannan.gallery.presentation.feature.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.kannan.gallery.domain.GalleryRepository
import com.kannan.gallery.domain.model.Media
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaScreenViewModel @Inject constructor(
    repository: GalleryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MediaScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<MediaScreenUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _mediaListPagedStream = MutableStateFlow<PagingData<Media>>(PagingData.empty())
    val mediaListPagedStream = _mediaListPagedStream.asStateFlow()

    init {
        repository.getAllMediaPagedStream()
            .cachedIn(viewModelScope)
            .onEach {
                updateMediaList(it)
            }.launchIn(viewModelScope)
    }


    fun onUiAction(action: MediaScreenUiAction) {
        when (action) {
            MediaScreenUiAction.OnTimelineContentBackPressed -> {
                sendEvent(MediaScreenUiEvent.NavigateUp)
            }

            is MediaScreenUiAction.OnMediaContentBackPressed -> {
                updateCurrentPosition(action.currentMediaPosition)
                updateScreenTypeUiState(ScreenContentType.TIMELINE)
            }

            is MediaScreenUiAction.OnImageClicked -> {

                updateCurrentPosition(action.index)

                updateScreenTypeUiState(ScreenContentType.MEDIA)
            }

            is MediaScreenUiAction.OnImageLongClicked -> {
                val newData = mediaListPagedStream.value.map {
                    if (it.id == action.media.id)
                        it.copy(isSelected = true)
                    else
                        it
                }

                updateMediaList(newData)

            }
        }
    }

    private fun updateMediaList(mediaList: PagingData<Media>) =
        _mediaListPagedStream.update {
            mediaList
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

    private fun sendEvent(event: MediaScreenUiEvent) = viewModelScope.launch {
        _uiEvent.send(event)
    }

}

data class MediaScreenUiState(
    val screenContentType: ScreenContentType = ScreenContentType.TIMELINE,
    val currentMediaPosition: Int = 0
)

sealed interface MediaScreenUiAction {
    data class OnImageClicked(val index: Int) : MediaScreenUiAction
    data class OnImageLongClicked(val media: Media) : MediaScreenUiAction
    data object OnTimelineContentBackPressed : MediaScreenUiAction
    data class OnMediaContentBackPressed(val currentMediaPosition: Int) : MediaScreenUiAction
}

sealed interface MediaScreenUiEvent {
    data object NavigateUp : MediaScreenUiEvent
}

enum class ScreenContentType {
    TIMELINE,
    MEDIA
}