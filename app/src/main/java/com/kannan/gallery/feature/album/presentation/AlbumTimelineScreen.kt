package com.kannan.gallery.feature.album.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kannan.gallery.core.presentation.navigation.NavigationScreen
import com.kannan.gallery.core.presentation.sharedViewModel.dummyAlbumMediaList
import com.kannan.gallery.feature.timeline.domain.Media
import com.kannan.gallery.feature.timeline.presentation.components.Thumbnail
import com.kannan.gallery.ui.theme.GalleryTheme
import com.kannan.gallery.utils.ext.CollectAsEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.AlbumTimelineScreen(
    modifier: Modifier = Modifier,
    navigateToCallBack: (NavigationScreen) -> Unit,
    uiState: AlbumTimelineScreenUiState,
    uiEvent: Flow<AlbumTimelineScreenUiEvent>,
    uiAction: ((AlbumTimelineScreenUiAction) -> Unit),
    albumMediaList: List<Media>,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    uiEvent.CollectAsEffect { event ->
        when (event) {
            is AlbumTimelineScreenUiEvent.NavigateTo -> navigateToCallBack.invoke(event.navigationScreen)
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxSize()
    ) {
        items(albumMediaList.size) { index: Int ->

            val data = albumMediaList[index]

            Thumbnail(
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = "image/ ${data.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .size(200.dp),
                data = data.uri,
                contentDescription = data.uri,
                onClick = {
                    uiAction.invoke(AlbumTimelineScreenUiAction.OnImageClicked(index))
                }
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
private fun AlbumTimelineScreenPreview() {
    GalleryTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                AlbumTimelineScreen(
                    uiState = AlbumTimelineScreenUiState(),
                    uiAction = {},
                    uiEvent = emptyFlow(),
                    albumMediaList = dummyAlbumMediaList,
                    navigateToCallBack = {},
                    animatedVisibilityScope = this
                )
            }
        }
    }
}
