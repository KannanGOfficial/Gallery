package com.kannan.gallery.presentation.feature.media

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.insertSeparators
import androidx.paging.map
import com.kannan.gallery.domain.GalleryRepository
import com.kannan.gallery.domain.model.Album
import com.kannan.gallery.domain.model.Media
import com.kannan.gallery.domain.model.MediaUiModel
import com.kannan.gallery.utils.ext.insertLineSeparator
import com.kannan.gallery.utils.ext.mapAsMediaUiModelItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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

    private val _mediaListUiModelPagedStream =
        MutableStateFlow<PagingData<MediaUiModel>>(PagingData.empty())
    val mediaListUiModelPagedStream = _mediaListUiModelPagedStream.asStateFlow()

    init {
        observeAndUpdateMediaListUiModel()
        observeAndUpdateMediaList()
        observeAndUpdateIsInMediaSelectionMode()
        observeAndUpdateShouldShowBottomBar()
        getAlbumList()
//        observeAndUpdateSelectionMediaCount()
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
                updateMediaListUiModel(it)
            }
            .launchIn(viewModelScope)
    }


    private fun observeAndUpdateMediaList() {
        repository.getAllMediaPagedStream()
            .cachedIn(viewModelScope)
            .onEach {
                updateMediaListPaged(it)
            }.launchIn(viewModelScope)
    }

    private fun observeAndUpdateIsInMediaSelectionMode() {
        uiState.map { it.selectedMediaCount }
            .onEach { selectedMediaCount ->
                val isInMediaSelectionMode = selectedMediaCount > 0
                updateIsInMediaSelectionModeUiState(isInMediaSelectionMode)
            }.launchIn(viewModelScope)
    }

    private fun observeAndUpdateShouldShowBottomBar() {
        combine(
            uiState.map { it.screenContentType },
            uiState.map { it.isInMediaSelectionMode }
        ) { screenContentType, isInMediaSelectionMode ->
            val shouldShowBottomBar = when {
                screenContentType == ScreenContentType.MEDIA ||
                        isInMediaSelectionMode -> false

                else -> true
            }
            updateShouldShowBottomBar(shouldShowBottomBar)

        }.launchIn(viewModelScope)
    }

    private fun observeAndUpdateSelectionMediaCount() {
        uiState.map { it.mediaList }
            .onEach { mediaList ->
                val selectedMediaCount = mediaList.count { it.isSelected }
                updateSelectedMediaCount(selectedMediaCount)
            }.launchIn(viewModelScope)
    }


    fun onUiAction(action: MediaScreenUiAction) {
        when (action) {
            MediaScreenUiAction.OnTimelineContentBackPressed -> {
                if (uiState.value.shouldShowAlbumBottomSheet) {
                    updateShouldShowAlbumBottomSheet(false)
                } else if (uiState.value.isInMediaSelectionMode) {
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

            is MediaScreenUiAction.OnNewMediaListPaged -> {
                updateMediaList(action.mediaList)
            }

            MediaScreenUiAction.OnSelectionSheetCloseClicked -> {
                updateAllIsSelectedState(false)
            }

            MediaScreenUiAction.OnSelectionSheetCopyClicked -> {
                updateMediaActionType(MediaActionType.COPY)
                updateShouldShowAlbumBottomSheet(true)
            }

            MediaScreenUiAction.OnSelectionSheetMoveClicked -> {
                updateMediaActionType(MediaActionType.MOVE)
                updateShouldShowAlbumBottomSheet(true)
            }

            MediaScreenUiAction.OnAlbumBottomSheetDismissed -> {
                updateShouldShowAlbumBottomSheet(false)
            }

            is MediaScreenUiAction.OnAlbumPathSelected -> {
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

            is MediaScreenUiAction.OnSelectedMediaListChanged -> {
                updateSelectedMediaList(action.selectedMediaList)
            }

            is MediaScreenUiAction.OnSelectionSheetTrashClicked -> {
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

    private fun removeMediaFromPagingList() {
        uiState.value.selectedMediaList.forEach { media ->
            removeMediaFromList(media.id)
        }
    }

    private fun removeMediaFromList(mediaId: Long) {
        val newData = mediaListPagedStream.value.filter { it.id != mediaId }
        updateMediaListPaged(newData)
    }

    private fun toggleFavorite(result: ActivityResultLauncher<IntentSenderRequest>) =
        viewModelScope.launch {
            val selectedMediaList = uiState.value.selectedMediaList
            repository.toggleFavorite(
                mediaList = selectedMediaList,
                favorite = true,
                result = result
            )
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
        updateShouldShowAlbumBottomSheet(false)
        updateAllIsSelectedState(false)
    }


    private fun updateIsSelectedState(id: Long, isSelected: Boolean) {
        val newData = mediaListPagedStream.value.map {
            if (it.id == id)
                it.copy(isSelected = isSelected)
            else
                it
        }
        updateMediaListPaged(newData)
    }

    private fun updateAllIsSelectedState(isSelected: Boolean) {
        val newData = mediaListPagedStream.value.map {
            it.copy(isSelected = isSelected)
        }
        updateMediaListPaged(newData)
    }

    private fun updateMediaListPaged(mediaList: PagingData<Media>): Unit =
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

    private fun updateShouldShowBottomBar(shouldShowBottomBar: Boolean): Unit =
        _uiState.update {
            it.copy(
                shouldShowBottomBar = shouldShowBottomBar
            )
        }

    private fun updateMediaList(mediaList: Set<Media>): Unit =
        _uiState.update {
            it.copy(
                mediaList = mediaList
            )
        }


    private fun updateMediaListUiModel(mediaListUiModel: PagingData<MediaUiModel>): Unit =
        _mediaListUiModelPagedStream.update { mediaListUiModel }

    private fun updateShouldShowAlbumBottomSheet(shouldShowAlbumBottomSheet: Boolean): Unit =
        _uiState.update {
            it.copy(
                shouldShowAlbumBottomSheet = shouldShowAlbumBottomSheet
            )
        }

    private fun updateSelectedMediaList(selectedMediaList: List<Media>) =
        _uiState.update { it.copy(selectedMediaList = selectedMediaList) }

    private fun updateAlbumList(albumList: List<Album>) =
        _uiState.update { it.copy(albumList = albumList) }

    private fun updateMediaActionType(mediaActionType: MediaActionType) =
        _uiState.update { it.copy(mediaActionType = mediaActionType) }

    private fun sendEvent(event: MediaScreenUiEvent) = viewModelScope.launch {
        _uiEvent.send(event)
    }
}

data class MediaScreenUiState(
    val screenContentType: ScreenContentType = ScreenContentType.TIMELINE,
    val currentMediaPosition: Int = 0,
    val isInMediaSelectionMode: Boolean = false,
    val selectedMediaCount: Int = 0,
    val shouldShowBottomBar: Boolean = true,
    val shouldShowAlbumBottomSheet: Boolean = false,
    val mediaList: Set<Media> = emptySet(),
    val albumList: List<Album> = emptyList(),
    val selectedMediaList: List<Media> = emptyList(),
    val mediaActionType: MediaActionType? = null
)

sealed interface MediaScreenUiAction {
    data class OnImageClicked(val index: Int, val media: Media) : MediaScreenUiAction
    data class OnImageLongClicked(val media: Media) : MediaScreenUiAction
    data object OnTimelineContentBackPressed : MediaScreenUiAction
    data class OnMediaContentBackPressed(val currentMediaPosition: Int) : MediaScreenUiAction
    data class OnSelectedItemCountChanged(val selectedMediaCount: Int) : MediaScreenUiAction
    data class OnNewMediaListPaged(val mediaList: Set<Media>) : MediaScreenUiAction
    data object OnSelectionSheetCloseClicked : MediaScreenUiAction
    data object OnSelectionSheetCopyClicked : MediaScreenUiAction
    data object OnAlbumBottomSheetDismissed : MediaScreenUiAction
    data object OnSelectionSheetMoveClicked : MediaScreenUiAction
    data class OnSelectionSheetTrashClicked(val result: ActivityResultLauncher<IntentSenderRequest>) :
        MediaScreenUiAction
    data class OnAlbumPathSelected(val path: String) : MediaScreenUiAction
    data class OnSelectedMediaListChanged(val selectedMediaList: List<Media>) : MediaScreenUiAction
}

sealed interface MediaScreenUiEvent {
    data object NavigateUp : MediaScreenUiEvent
}

enum class ScreenContentType {
    TIMELINE,
    MEDIA
}

enum class MediaActionType {
    COPY,
    MOVE
}