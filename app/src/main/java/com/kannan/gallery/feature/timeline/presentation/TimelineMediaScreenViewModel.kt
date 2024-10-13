package com.kannan.gallery.feature.timeline.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.kannan.gallery.core.presentation.navigation.NavigationScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TimelineMediaScreenViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _uiState = MutableStateFlow(TimelineMediaScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val timelineMediaScreen: NavigationScreen.TimelineMediaScreen = savedStateHandle.toRoute()
        updateScrollToPositionUiState(timelineMediaScreen.scrollToPosition)
    }

    private fun updateScrollToPositionUiState(scrollToPosition: Int) =
        _uiState.update {
            it.copy(
                scrollToPosition = scrollToPosition
            )
        }
}

data class TimelineMediaScreenUiState(
    val scrollToPosition: Int = 0
)