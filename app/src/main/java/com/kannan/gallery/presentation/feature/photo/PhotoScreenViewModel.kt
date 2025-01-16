package com.kannan.gallery.presentation.feature.photo

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
class PhotoScreenViewModel @Inject constructor(
    repository: GalleryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PhotoScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<PhotoScreenUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _mediaListPagedStream = MutableStateFlow<PagingData<Media>>(PagingData.empty())
    val mediaListPagedStream = _mediaListPagedStream.asStateFlow()

    init {
        repository.getAllMediaPagedStream()
            .cachedIn(viewModelScope)
            .onEach {
                updateMediaList(it)
            }
            .launchIn(viewModelScope)
    }


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

                updateCurrentPosition(action.index)

                updateScreenTypeUiState(ScreenContentType.MEDIA)
            }

            is PhotoScreenUiAction.OnImageLongClicked -> {
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

    private fun sendEvent(event: PhotoScreenUiEvent) = viewModelScope.launch {
        _uiEvent.send(event)
    }

}

data class PhotoScreenUiState(
    val screenContentType: ScreenContentType = ScreenContentType.TIMELINE,
    val currentMediaPosition: Int = 0
)

sealed interface PhotoScreenUiAction {
    data class OnImageClicked(val index: Int) : PhotoScreenUiAction
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