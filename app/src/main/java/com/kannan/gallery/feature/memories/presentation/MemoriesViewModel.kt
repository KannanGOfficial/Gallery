package com.kannan.gallery.feature.memories.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kannan.gallery.core.presentation.sharedViewModel.dummyTimelineMediaList
import com.kannan.gallery.feature.timeline.domain.Media
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MemoriesViewModel : ViewModel() {

    private val _memoriesUiState = MutableStateFlow(MemoriesScreenUiState.MemoriesUiState())
    val memoriesUiState = _memoriesUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = MemoriesScreenUiState.MemoriesUiState()
    )

    private val _timelineUiState = MutableStateFlow(MemoriesScreenUiState.TimelineUiState())
    val timelineUiState = _timelineUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = MemoriesScreenUiState.TimelineUiState()
    )

    private val _detailUiState = MutableStateFlow(MemoriesScreenUiState.DetailUiState())
    val detailUiState = _detailUiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = MemoriesScreenUiState.DetailUiState()
    )

    private val _uiEvent = Channel<MemoriesUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        updateMediaList(dummyTimelineMediaList)
    }


    fun onTimelineUiAction(action: MemoriesTimelineUiAction) {
        when (action) {

            MemoriesTimelineUiAction.OnBackPressed -> {
                sendEvent(MemoriesUiEvent.NavigateUp)
            }

            is MemoriesTimelineUiAction.OnImageClicked -> {
                updateInitialPagerPosition(action.position)
                updateMemoriesScreenState(MemoriesScreenState.DETAIL)
            }

            is MemoriesTimelineUiAction.OnImageLongClicked -> TODO()
        }
    }

    fun onDetailUiAction(action: MemoriesDetailUiAction) {
        when (action) {
            MemoriesDetailUiAction.OnBackPressed -> {
                updateMemoriesScreenState(MemoriesScreenState.TIMELINE)
            }
        }
    }

    private fun sendEvent(event: MemoriesUiEvent) = viewModelScope.launch {
        _uiEvent.send(event)
    }

    private fun updateMemoriesScreenState(memoriesScreenState: MemoriesScreenState): Unit =
        _memoriesUiState.update {
            it.copy(
                memoriesScreenState = memoriesScreenState
            )
        }

    private fun updateMediaList(mediaList: List<Media>): Unit =
        _memoriesUiState.update {
            it.copy(
                mediaList = mediaList
            )
        }

    private fun updateAlbumName(albumName: String?): Unit =
        _memoriesUiState.update {
            it.copy(
                albumName = albumName
            )
        }

    private fun updateInitialPagerPosition(initialPagerPosition: Int): Unit =
        _detailUiState.update {
            it.copy(
                initialPagerPosition = initialPagerPosition
            )
        }
}

sealed interface MemoriesScreenUiState {
    data class MemoriesUiState(
        val memoriesScreenState: MemoriesScreenState = MemoriesScreenState.TIMELINE,
        val mediaList: List<Media> = emptyList(),
        val albumName: String? = null
    ) : MemoriesScreenUiState

    data class TimelineUiState(
        val isInMediaSelectionMode: Boolean = false
    ) : MemoriesScreenUiState

    data class DetailUiState(
        val initialPagerPosition: Int = 0
    )
}

sealed interface MemoriesTimelineUiAction {
    data class OnImageClicked(val position: Int) : MemoriesTimelineUiAction
    data class OnImageLongClicked(val media: Media) : MemoriesTimelineUiAction
    data object OnBackPressed : MemoriesTimelineUiAction
}

sealed interface MemoriesDetailUiAction {
    data object OnBackPressed : MemoriesDetailUiAction
}

sealed interface MemoriesUiEvent {
    data object NavigateUp : MemoriesUiEvent
}

enum class MemoriesScreenState {
    TIMELINE,
    DETAIL
}