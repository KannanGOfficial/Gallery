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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaScreenViewModel @Inject constructor(
    private val repository: GalleryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MediaScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<MediaScreenUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _mediaListPagedStream = MutableStateFlow<PagingData<Media>>(PagingData.empty())
    val mediaListPagedStream = _mediaListPagedStream.asStateFlow()

    init {
        observeAndUpdateMediaList()
        observeAndUpdateIsInMediaSelectionMode()
    }

    private fun observeAndUpdateMediaList() {
        repository.getAllMediaPagedStream()
            .cachedIn(viewModelScope)
            .onEach {
                updateMediaList(it)
            }.launchIn(viewModelScope)
    }

    private fun observeAndUpdateIsInMediaSelectionMode() {
        uiState.map { it.selectedMediaCount }
            .onEach { selectedMediaCount ->
                val isInMediaSelectionMode = selectedMediaCount > 0
                updateIsInMediaSelectionModeUiState(isInMediaSelectionMode)
            }.launchIn(viewModelScope)
    }


    fun onUiAction(action: MediaScreenUiAction) {
        when (action) {
            MediaScreenUiAction.OnTimelineContentBackPressed -> {
                if (uiState.value.isInMediaSelectionMode) {
                    updateAllIsSelectedState(false)
                } else {
                    sendEvent(MediaScreenUiEvent.NavigateUp)
                }
            }

            is MediaScreenUiAction.OnMediaContentBackPressed -> {
                updateCurrentPosition(action.currentMediaPosition)
                updateScreenTypeUiState(ScreenContentType.TIMELINE)
            }

            is MediaScreenUiAction.OnImageClicked -> {

                if (uiState.value.isInMediaSelectionMode) {
                    updateIsSelectedState(
                        id = action.media.id,
                        isSelected = !action.media.isSelected
                    )
                } else {
                    updateCurrentPosition(action.index)
                    updateScreenTypeUiState(ScreenContentType.MEDIA)
                }
            }

            is MediaScreenUiAction.OnImageLongClicked -> {
                updateIsSelectedState(
                    id = action.media.id,
                    isSelected = true
                )
            }

            is MediaScreenUiAction.OnSelectedItemCountChanged -> {
                updateSelectedMediaCount(action.selectedMediaCount)
            }
        }
    }

    private fun updateIsSelectedState(id: Long, isSelected: Boolean) {
        val newData = mediaListPagedStream.value.map {
            if (it.id == id)
                it.copy(isSelected = isSelected)
            else
                it
        }
        updateMediaList(newData)
    }

    private fun updateAllIsSelectedState(isSelected: Boolean) {
        val newData = mediaListPagedStream.value.map {
            it.copy(isSelected = isSelected)
        }
        updateMediaList(newData)
    }

    private fun updateMediaList(mediaList: PagingData<Media>): Unit =
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

    private fun updateIsInMediaSelectionModeUiState(isInMediaSelectionMode: Boolean): Unit =
        _uiState.update {
            it.copy(
                isInMediaSelectionMode = isInMediaSelectionMode
            )
        }

    private fun updateSelectedMediaCount(selectedMediaCount: Int): Unit =
        _uiState.update {
            it.copy(
                selectedMediaCount = selectedMediaCount
            )
        }

    private fun sendEvent(event: MediaScreenUiEvent) = viewModelScope.launch {
        _uiEvent.send(event)
    }
}

data class MediaScreenUiState(
    val screenContentType: ScreenContentType = ScreenContentType.TIMELINE,
    val currentMediaPosition: Int = 0,
    val isInMediaSelectionMode: Boolean = false,
    val selectedMediaCount: Int = 0
)

sealed interface MediaScreenUiAction {
    data class OnImageClicked(val index: Int, val media: Media) : MediaScreenUiAction
    data class OnImageLongClicked(val media: Media) : MediaScreenUiAction
    data object OnTimelineContentBackPressed : MediaScreenUiAction
    data class OnMediaContentBackPressed(val currentMediaPosition: Int) : MediaScreenUiAction
    data class OnSelectedItemCountChanged(val selectedMediaCount: Int) : MediaScreenUiAction
}

sealed interface MediaScreenUiEvent {
    data object NavigateUp : MediaScreenUiEvent
}

enum class ScreenContentType {
    TIMELINE,
    MEDIA
}