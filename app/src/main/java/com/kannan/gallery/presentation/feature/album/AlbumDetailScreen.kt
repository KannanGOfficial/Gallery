package com.kannan.gallery.presentation.feature.album

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kannan.gallery.domain.model.Media
import com.kannan.gallery.presentation.components.MediaContent
import com.kannan.gallery.presentation.components.TimelineContent
import com.kannan.gallery.presentation.feature.photo.ScreenContentType
import com.kannan.gallery.presentation.main.dummyTimelineMediaList
import com.kannan.gallery.ui.theme.GalleryTheme
import com.kannan.gallery.utils.ext.CollectAsEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun AlbumDetailScreen(
    modifier: Modifier = Modifier,
    uiState: AlbumDetailScreenUiState,
    uiEvent: Flow<AlbumDetailScreenUiEvent>,
    uiAction: (AlbumDetailScreenUiAction) -> Unit,
    mediaList: List<Media>,
    navigateUpCallback: () -> Unit
) {
    uiEvent.CollectAsEffect { event ->
        when (event) {
            AlbumDetailScreenUiEvent.NavigateUp -> navigateUpCallback.invoke()
        }
    }

    TimelineContent(
        modifier = modifier,
        mediaList = mediaList,
        currentMediaPosition = uiState.currentMediaPosition,
        onImageClicked = { uiAction.invoke(AlbumDetailScreenUiAction.OnImageClicked(it)) },
        onImageLongClicked = { uiAction.invoke(AlbumDetailScreenUiAction.OnImageLongClicked(it)) },
        onBackPressed = { uiAction.invoke(AlbumDetailScreenUiAction.OnTimelineContentBackPressed) },
    )

    if (uiState.screenContentType == ScreenContentType.MEDIA) {
        MediaContent(
            mediaList = mediaList,
            initialPagerPosition = uiState.currentMediaPosition,
            modifier = modifier,
            onBackPressed = { uiAction.invoke(AlbumDetailScreenUiAction.OnMediaContentBackPressed(it)) }
        )
    }


}

@Preview(showBackground = true)
@Composable
private fun AlbumDetailScreenPreview() {
    GalleryTheme {
        AlbumDetailScreen(
            uiState = AlbumDetailScreenUiState(
                screenContentType = ScreenContentType.MEDIA
            ),
            uiEvent = emptyFlow(),
            uiAction = {},
            mediaList = dummyTimelineMediaList,
            navigateUpCallback = {}
        )
    }
}