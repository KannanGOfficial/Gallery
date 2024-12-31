package com.kannan.gallery.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.kannan.gallery.presentation.navigation.bottomnav.BottomNavigationItem
import com.kannan.gallery.utils.ext.getRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    fun onUiAction(action: MainUiAction) {
        when (action) {
            is MainUiAction.OnNavDestinationChanged -> setBottomBarVisibility(action.route)
        }
    }

    private fun setBottomBarVisibility(route: String) {
        val shouldShowBottomBar = BottomNavigationItem.entries.any {
            it.screen::class.getRoute() == route
        }

        Log.d("NavDestination :", "")
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