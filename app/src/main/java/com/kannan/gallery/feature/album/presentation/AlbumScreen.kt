package com.kannan.gallery.feature.album.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kannan.gallery.core.presentation.navigation.NavigationScreen
import com.kannan.gallery.feature.album.presentation.components.AlbumCard
import com.kannan.gallery.ui.theme.GalleryTheme
import com.kannan.gallery.utils.ext.CollectAsEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun AlbumScreen(
    modifier: Modifier = Modifier,
    uiState: AlbumScreenUiState,
    uiEvent: Flow<AlbumScreenUiEvent>,
    uiAction: ((AlbumScreenUiAction) -> Unit),
    navigateToCallBack: (NavigationScreen) -> Unit,
) {

    uiEvent.CollectAsEffect { event ->
        when (event) {
            is AlbumScreenUiEvent.NavigateTo -> navigateToCallBack.invoke(event.navigationScreen)
        }
    }


    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(uiState.albumList.size) { index ->
            val album = uiState.albumList[index]
            AlbumCard(
                album = album,
                modifier = Modifier.padding(12.dp),
                onAlbumClicked = {
                    uiAction.invoke(AlbumScreenUiAction.OnAlbumClicked(it))
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AlbumScreenPreview() {
    GalleryTheme {
        AlbumScreen(
            uiAction = {},
            uiState = AlbumScreenUiState(albumList1),
            uiEvent = emptyFlow(),
            navigateToCallBack = {}
        )
    }
}
