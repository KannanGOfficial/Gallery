package com.kannan.gallery.feature.setup.presentation

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kannan.gallery.core.presentation.navigation.NavigationScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SetupScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SetupScreenUiState())
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = SetupScreenUiState()
    )

    private val _uiEvent = Channel<SetupScreenUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        observeChangesOfFullFileAccess()
    }

    fun onUiAction(action: SetupScreenUiAction) {
        when (action) {

            SetupScreenUiAction.AllowToManageFilesButtonClicked -> sendEvent(SetupScreenUiEvent.LaunchManageFiles)

            SetupScreenUiAction.AllowToManageMediaButtonClicked -> sendEvent(SetupScreenUiEvent.LaunchManageMedia)

            SetupScreenUiAction.OnCancelButtonClicked -> sendEvent(SetupScreenUiEvent.NavigateUp)

            SetupScreenUiAction.OnGetStartedButtonClicked -> sendEvent(SetupScreenUiEvent.LaunchMultiplePermissionRequest)

            SetupScreenUiAction.OnResume -> {
                updateDoesHaveFullFileAccessUiState(
                    doesHaveFullFileAccess = Environment.isExternalStorageManager()
                )
            }

            is SetupScreenUiAction.PermissionCallback -> {
                if (action.isPermissionGranted)
                    sendEvent(SetupScreenUiEvent.NavigateTo(NavigationScreen.TimelineScreen))
            }
        }
    }

    private fun sendEvent(event: SetupScreenUiEvent) = viewModelScope.launch {
        _uiEvent.send(event)
    }

    private fun observeChangesOfFullFileAccess() {
        uiState.map {
            it.doesHaveFullFileAccess
        }.onEach { doesHaveFullFileAccess ->
            updateShowAllowExternalMangeFilesButton(!doesHaveFullFileAccess)
        }.launchIn(viewModelScope)
    }

    private fun updateDoesHaveFullFileAccessUiState(doesHaveFullFileAccess: Boolean): Unit =
        _uiState.update {
            it.copy(
                doesHaveFullFileAccess = doesHaveFullFileAccess
            )
        }

    private fun updateShowAllowExternalMangeFilesButton(showAllowExternalManageFilesButton: Boolean): Unit =
        _uiState.update {
            it.copy(
                showAllowExternalManageFilesButton = showAllowExternalManageFilesButton
            )
        }
}

data class SetupScreenUiState(
    val doesHaveFullFileAccess: Boolean = false,
    val showAllowExternalManageFilesButton: Boolean = true
)

sealed interface SetupScreenUiAction {
    data object OnCancelButtonClicked : SetupScreenUiAction
    data object OnGetStartedButtonClicked : SetupScreenUiAction
    data object AllowToManageMediaButtonClicked : SetupScreenUiAction
    data object AllowToManageFilesButtonClicked : SetupScreenUiAction
    data object OnResume : SetupScreenUiAction
    data class PermissionCallback(val isPermissionGranted: Boolean) : SetupScreenUiAction
}

sealed interface SetupScreenUiEvent {
    data object LaunchMultiplePermissionRequest : SetupScreenUiEvent
    data object LaunchManageMedia : SetupScreenUiEvent
    data object LaunchManageFiles : SetupScreenUiEvent
    data object NavigateUp : SetupScreenUiEvent
    data class NavigateTo(val navigationScreen: NavigationScreen) : SetupScreenUiEvent
}
