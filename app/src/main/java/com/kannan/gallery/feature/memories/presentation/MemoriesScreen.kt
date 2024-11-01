package com.kannan.gallery.feature.memories.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kannan.gallery.utils.ext.CollectAsEffect
import kotlinx.coroutines.flow.Flow

@Composable
fun MemoriesScreen(
    modifier: Modifier = Modifier,
    uiState: MemoriesScreenUiState.MemoriesUiState,
    uiEvent: Flow<MemoriesUiEvent>,
    timelineUiState: MemoriesScreenUiState.TimelineUiState,
    timelineUiAction: ((MemoriesTimelineUiAction) -> Unit),
    detailUiAction: ((MemoriesDetailUiAction) -> Unit),
    detailUiState: MemoriesScreenUiState.DetailUiState,
    navigateUp: (() -> Unit)
) {

    uiEvent.CollectAsEffect { event ->
        when (event) {
            MemoriesUiEvent.NavigateUp -> navigateUp.invoke()
        }
    }

    when (uiState.memoriesScreenState) {
        MemoriesScreenState.TIMELINE -> {
            TimelineContent(
                uiState = timelineUiState,
                uiAction = timelineUiAction,
                mediaList = uiState.mediaList
            )
        }

        MemoriesScreenState.DETAIL -> {
            DetailContent(
                uiState = detailUiState,
                uiAction = detailUiAction,
                mediaList = uiState.mediaList
            )
        }
    }
}