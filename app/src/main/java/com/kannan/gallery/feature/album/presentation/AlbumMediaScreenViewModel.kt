package com.kannan.gallery.feature.album.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.kannan.gallery.core.presentation.navigation.NavigationScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AlbumMediaScreenViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumMediaScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val albumMediaScreen: NavigationScreen.AlbumMediaScreen = savedStateHandle.toRoute()

        Log.d("AlbumMediaScreenViewModel", "${albumMediaScreen.initialPagerPosition}")

        updateInitialPagerPositionUiState(albumMediaScreen.initialPagerPosition)
    }

    private fun updateInitialPagerPositionUiState(initialPagerPosition: Int) =
        _uiState.update {
            it.copy(
                initialPagerPosition = initialPagerPosition
            )
        }
}

data class AlbumMediaScreenUiState(
    val initialPagerPosition: Int = 0
)
