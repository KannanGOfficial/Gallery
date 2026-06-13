package com.kannan.gallery.presentation.feature.album

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kannan.gallery.presentation.feature.album.components.AlbumScreen
import com.kannan.gallery.presentation.navigation.NavigationScreen
import com.kannan.gallery.utils.ext.CollectAsEffect

@Composable
fun AlbumScreenRoot(
    navigateToCallBack: (NavigationScreen) -> Unit,
) {

    val viewModel = hiltViewModel<AlbumScreenViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiAction = viewModel::onUiAction

    viewModel.uiEvent.CollectAsEffect { event ->
        when (event) {
            is AlbumScreenUiEvent.NavigateTo -> navigateToCallBack.invoke(event.navigationScreen)
        }
    }

    AlbumScreen(
        albumList = uiState.albumList,
        shouldShowTrashedAndFavourites = true,
        onAlbumClicked = {
            uiAction(
                AlbumScreenUiAction.OnAlbumClicked(
                    id = it.id,
                    name = it.name
                )
            )
        },
        onSectionClicked = {
            uiAction(
                AlbumScreenUiAction.OnAlbumClicked(
                    id = 0L,
                    name = it
                )
            )
        }
    )
}