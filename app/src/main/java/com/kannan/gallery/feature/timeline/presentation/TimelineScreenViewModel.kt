package com.kannan.gallery.feature.timeline.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kannan.gallery.core.presentation.navigation.NavigationScreen
import com.kannan.gallery.feature.timeline.domain.Media
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimelineScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TimelineScreenUiState())
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500L),
        initialValue = TimelineScreenUiState()
    )

    private val _uiEvent = Channel<TimelineScreenUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onUiAction(action: TimelineScreenUiAction) {
        when (action) {
            TimelineScreenUiAction.OnBackPressed -> Unit

            TimelineScreenUiAction.OnCopyButtonClicked -> Unit

            is TimelineScreenUiAction.OnImageClicked -> {
                sendEvent(
                    event = TimelineScreenUiEvent.NavigateTo(
                        NavigationScreen.TimelineMediaScreen(
                            action.position
                        )
                    )
                )
            }

            is TimelineScreenUiAction.OnImageLongClicked -> Unit

            TimelineScreenUiAction.OnMoveButtonClicked -> Unit

            TimelineScreenUiAction.OnSelectionSheetCancelButtonClicked -> Unit

            TimelineScreenUiAction.OnShareButtonClicked -> Unit
        }
    }

    private fun sendEvent(event: TimelineScreenUiEvent) = viewModelScope.launch {
        _uiEvent.send(event)
    }

    private fun updateIsInMediaSelectionModeUiState(isInMediaSelectionMode: Boolean): Unit =
        _uiState.update {
            it.copy(
                isInMediaSelectionMode = isInMediaSelectionMode
            )
        }
}

data class TimelineScreenUiState(
    val isInMediaSelectionMode: Boolean = false
)

sealed interface TimelineScreenUiAction {
    data class OnImageClicked(val position: Int) : TimelineScreenUiAction
    data class OnImageLongClicked(val media: Media) : TimelineScreenUiAction
    data object OnCopyButtonClicked : TimelineScreenUiAction
    data object OnMoveButtonClicked : TimelineScreenUiAction
    data object OnShareButtonClicked : TimelineScreenUiAction
    data object OnSelectionSheetCancelButtonClicked : TimelineScreenUiAction
    data object OnBackPressed : TimelineScreenUiAction
}

sealed interface TimelineScreenUiEvent {
    data class NavigateTo(val navigationScreens: NavigationScreen) : TimelineScreenUiEvent

}