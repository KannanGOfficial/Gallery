package com.kannan.gallery.presentation.feature.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.kannan.gallery.domain.GalleryRepository
import com.kannan.gallery.domain.model.Media
import com.kannan.gallery.presentation.feature.media.ScreenContentType
import com.kannan.gallery.presentation.navigation.NavigationScreen
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
class AlbumDetailScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: GalleryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumDetailScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _mediaListPagedStream = MutableStateFlow<PagingData<Media>>(PagingData.empty())
    val mediaListPagedStream = _mediaListPagedStream.asStateFlow()

    private val _uiEvent = Channel<AlbumDetailScreenUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val albumDetailScreen = savedStateHandle.toRoute<NavigationScreen.AlbumDetailScreen>()

        updateAlbumName(albumDetailScreen.albumName)
        observeAndUpdateMediaList(albumDetailScreen.albumId)
        observeAndUpdateIsInMediaSelectionMode()
    }

    private fun observeAndUpdateMediaList(albumId: Long) {
        repository.getMediaByAlbumName(albumId)
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

    fun onUiAction(action: AlbumDetailScreenUiAction) {
        when (action) {
            AlbumDetailScreenUiAction.OnTimelineContentBackPressed -> {
                if (uiState.value.isInMediaSelectionMode) {
                    updateAllIsSelectedState(false)
                } else {
                    sendEvent(AlbumDetailScreenUiEvent.NavigateUp)
                }
            }

            is AlbumDetailScreenUiAction.OnMediaContentBackPressed -> {
                updateCurrentPosition(action.currentMediaPosition)
                updateScreenTypeUiState(ScreenContentType.TIMELINE)
            }

            is AlbumDetailScreenUiAction.OnImageClicked -> {

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

            is AlbumDetailScreenUiAction.OnImageLongClicked -> {
                updateIsSelectedState(
                    id = action.media.id,
                    isSelected = true
                )
            }

            is AlbumDetailScreenUiAction.OnSelectedItemCountChanged -> {
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

    private fun updateAlbumName(albumName: String): Unit =
        _uiState.update {
            it.copy(
                albumName = albumName
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

    private fun sendEvent(event: AlbumDetailScreenUiEvent) = viewModelScope.launch {
        _uiEvent.send(event)
    }
}

data class AlbumDetailScreenUiState(
    val screenContentType: ScreenContentType = ScreenContentType.TIMELINE,
    val currentMediaPosition: Int = 0,
    val albumName: String = "",
    val isInMediaSelectionMode: Boolean = false,
    val selectedMediaCount: Int = 0
)

sealed interface AlbumDetailScreenUiAction {
    data class OnImageClicked(val index: Int, val media: Media) : AlbumDetailScreenUiAction
    data class OnImageLongClicked(val media: Media) : AlbumDetailScreenUiAction
    data object OnTimelineContentBackPressed : AlbumDetailScreenUiAction
    data class OnMediaContentBackPressed(val currentMediaPosition: Int) : AlbumDetailScreenUiAction
    data class OnSelectedItemCountChanged(val selectedMediaCount: Int) : AlbumDetailScreenUiAction
}

sealed interface AlbumDetailScreenUiEvent {
    data object NavigateUp : AlbumDetailScreenUiEvent
}