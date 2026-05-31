package com.kannan.gallery.presentation.feature.album

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.insertSeparators
import androidx.paging.map
import com.kannan.gallery.domain.GalleryRepository
import com.kannan.gallery.domain.model.Album
import com.kannan.gallery.domain.model.Media
import com.kannan.gallery.domain.model.MediaUiModel
import com.kannan.gallery.presentation.feature.media.MediaActionType
import com.kannan.gallery.presentation.feature.media.ScreenContentType
import com.kannan.gallery.presentation.navigation.NavigationScreen
import com.kannan.gallery.utils.ext.insertLineSeparator
import com.kannan.gallery.utils.ext.mapAsMediaUiModelItem
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

    private val _mediaListUiModelPagedStream =
        MutableStateFlow<PagingData<MediaUiModel>>(PagingData.empty())
    val mediaListUiModelPagedStream = _mediaListUiModelPagedStream.asStateFlow()

    private val _uiEvent = Channel<AlbumDetailScreenUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val albumDetailScreen = savedStateHandle.toRoute<NavigationScreen.AlbumDetailScreen>()

    init {

        updateAlbumName(albumDetailScreen.albumName)
        observeAndUpdateMediaListUiModel()
        observeAndUpdateMediaList(albumDetailScreen.albumId)
        observeAndUpdateIsInMediaSelectionMode()
        getAlbumList()
    }

    private fun getAlbumList() {
        viewModelScope.launch {
            val albumList = repository.getAllAlbum()
            updateAlbumList(albumList)
        }
    }

    private fun observeAndUpdateMediaListUiModel() {
        mediaListPagedStream
            .map { it.mapAsMediaUiModelItem() }
            .map { it.insertSeparators(generator = ::insertLineSeparator) }
            .onEach {
                Log.d("AlbumDetailScreenViewModel", "observeAndUpdateMediaListUiModel: ${it}")
                updateMediaListUiModel(it)
            }
            .launchIn(viewModelScope)
    }

    private fun observeAndUpdateMediaList(albumId: Long) {
        repository.getMediaByAlbumName(albumId)
            .cachedIn(viewModelScope)
            .onEach {
                Log.d(
                    "AlbumDetailScreenViewModel",
                    "observeAndUpdateMediaList: ${it.map { data -> data.id }}"
                )
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
                if (uiState.value.shouldShowAlbumBottomSheet) {
                    updateShouldShowAlbumBottomSheet(false)
                } else if (uiState.value.isInMediaSelectionMode) {
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

            AlbumDetailScreenUiAction.OnAlbumBottomSheetDismissed -> {
                updateShouldShowAlbumBottomSheet(false)
            }

            AlbumDetailScreenUiAction.OnSelectionSheetCloseClicked -> {
                updateAllIsSelectedState(false)
            }

            AlbumDetailScreenUiAction.OnSelectionSheetCopyClicked -> {
                updateMediaActionType(MediaActionType.COPY)
                updateShouldShowAlbumBottomSheet(true)
            }

            AlbumDetailScreenUiAction.OnSelectionSheetMoveClicked -> {
                updateMediaActionType(MediaActionType.MOVE)
                updateShouldShowAlbumBottomSheet(true)
            }

            is AlbumDetailScreenUiAction.OnAlbumPathSelected -> {
                when (uiState.value.mediaActionType) {
                    MediaActionType.COPY -> {
                        copyMediaToPath(action.path)
                    }

                    MediaActionType.MOVE -> {
                        moveMedia(action.path)
                    }

                    null -> Unit
                }
            }

            is AlbumDetailScreenUiAction.OnSelectedMediaListChanged -> {
                updateSelectedMediaList(action.selectedMediaList)
            }

            is AlbumDetailScreenUiAction.OnSelectionSheetTrashClicked -> {
                trashMedia(action.result)
            }
        }
    }

    private fun trashMedia(result: ActivityResultLauncher<IntentSenderRequest>) =
        viewModelScope.launch {
            val selectedMediaList = uiState.value.selectedMediaList
            repository.trashMedia(
                mediaList = selectedMediaList,
                trash = true,
                result = result
            )
            removeMediaFromPagingList()
            updateAllIsSelectedState(false)
        }


    private fun copyMediaToPath(path: String) = viewModelScope.launch {
        val selectedMedia = uiState.value.selectedMediaList
        selectedMedia.forEach { media ->
            repository.copyMedia(
                from = media,
                toPath = path
            )
        }
        updateShouldShowAlbumBottomSheet(false)
        updateAllIsSelectedState(false)
    }

    private fun moveMedia(path: String) = viewModelScope.launch {
        val selectedMediaList = uiState.value.selectedMediaList
        selectedMediaList.forEach { media ->
            repository.moveMedia(
                media = media,
                toPath = path
            )
        }
        removeMediaFromPagingList()
        updateShouldShowAlbumBottomSheet(false)
        updateAllIsSelectedState(false)
    }

    private fun removeMediaFromPagingList() {
        uiState.value.selectedMediaList.forEach { media ->
            removeMediaFromList(media.id)
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

    private fun removeMediaFromList(mediaId: Long) {
        val newData = mediaListPagedStream.value.filter { it.id != mediaId }
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

    private fun updateMediaListUiModel(mediaListUiModel: PagingData<MediaUiModel>): Unit =
        _mediaListUiModelPagedStream.update { mediaListUiModel }

    private fun updateAlbumList(albumList: List<Album>) =
        _uiState.update { it.copy(albumList = albumList) }

    private fun updateShouldShowAlbumBottomSheet(shouldShowAlbumBottomSheet: Boolean) =
        _uiState.update { it.copy(shouldShowAlbumBottomSheet = shouldShowAlbumBottomSheet) }

    private fun updateSelectedMediaList(selectedMediaList: List<Media>) =
        _uiState.update { it.copy(selectedMediaList = selectedMediaList) }

    private fun updateMediaActionType(mediaActionType: MediaActionType) =
        _uiState.update { it.copy(mediaActionType = mediaActionType) }

    private fun sendEvent(event: AlbumDetailScreenUiEvent) = viewModelScope.launch {
        _uiEvent.send(event)
    }
}

data class AlbumDetailScreenUiState(
    val screenContentType: ScreenContentType = ScreenContentType.TIMELINE,
    val currentMediaPosition: Int = 0,
    val albumName: String = "",
    val isInMediaSelectionMode: Boolean = false,
    val selectedMediaCount: Int = 0,
    val albumList: List<Album> = emptyList(),
    val shouldShowAlbumBottomSheet: Boolean = false,
    val selectedMediaList: List<Media> = emptyList(),
    val mediaActionType: MediaActionType? = null
)

sealed interface AlbumDetailScreenUiAction {
    data class OnImageClicked(val index: Int, val media: Media) : AlbumDetailScreenUiAction
    data class OnImageLongClicked(val media: Media) : AlbumDetailScreenUiAction
    data object OnTimelineContentBackPressed : AlbumDetailScreenUiAction
    data class OnMediaContentBackPressed(val currentMediaPosition: Int) : AlbumDetailScreenUiAction
    data class OnSelectedItemCountChanged(val selectedMediaCount: Int) : AlbumDetailScreenUiAction
    data object OnAlbumBottomSheetDismissed : AlbumDetailScreenUiAction
    data object OnSelectionSheetCloseClicked : AlbumDetailScreenUiAction
    data object OnSelectionSheetCopyClicked : AlbumDetailScreenUiAction
    data class OnAlbumPathSelected(val path: String) : AlbumDetailScreenUiAction
    data object OnSelectionSheetMoveClicked : AlbumDetailScreenUiAction
    data class OnSelectionSheetTrashClicked(val result: ActivityResultLauncher<IntentSenderRequest>) :
        AlbumDetailScreenUiAction

    data class OnSelectedMediaListChanged(val selectedMediaList: List<Media>) :
        AlbumDetailScreenUiAction
}

sealed interface AlbumDetailScreenUiEvent {
    data object NavigateUp : AlbumDetailScreenUiEvent
}