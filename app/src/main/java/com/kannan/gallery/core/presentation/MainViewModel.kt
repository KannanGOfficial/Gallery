package com.kannan.gallery.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kannan.gallery.core.presentation.navigation.bottomnav.BottomNavigationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = MainUiState()
    )

    fun onUiAction(action: MainUiAction) {
        when (action) {
            is MainUiAction.OnNavDestinationChanged -> setBottomBarVisibility(action.route)
        }
    }

    private fun setBottomBarVisibility(route: String) {
        val shouldShowBottomBar = BottomNavigationItem.entries.any {
            it.screen::class.qualifiedName == route
        }
        updateShouldShowBottomBarUiState(shouldShowBottomBar)
    }

    private fun updateShouldShowBottomBarUiState(shouldShowBottomBar: Boolean): Unit =
        _uiState.update {
            it.copy(
                shouldShowBottomBar = shouldShowBottomBar
            )
        }

}

data class MainUiState(
    val shouldShowBottomBar: Boolean = true
)

sealed interface MainUiAction {
    data class OnNavDestinationChanged(val route: String) : MainUiAction
}