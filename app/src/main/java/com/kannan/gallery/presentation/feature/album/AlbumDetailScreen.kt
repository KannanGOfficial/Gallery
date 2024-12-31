package com.kannan.gallery.presentation.feature.album

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kannan.gallery.data.dummyTimelineMediaList
import com.kannan.gallery.domain.model.Media
import com.kannan.gallery.presentation.components.MediaContent
import com.kannan.gallery.presentation.components.TimelineContent
import com.kannan.gallery.presentation.feature.photo.ScreenContentType
import com.kannan.gallery.ui.theme.GalleryTheme
import com.kannan.gallery.utils.ext.CollectAsEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.AlbumDetailScreen(
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

    AnimatedContent(
        uiState.screenContentType,
        label = ""
    ) { targetState ->

        when (targetState) {
            ScreenContentType.TIMELINE -> {
                TimelineContent(
                    modifier = modifier,
                    mediaList = mediaList,
                    currentMediaPosition = uiState.currentMediaPosition,
                    onImageClicked = { uiAction.invoke(AlbumDetailScreenUiAction.OnImageClicked(it)) },
                    onImageLongClicked = {
                        uiAction.invoke(
                            AlbumDetailScreenUiAction.OnImageLongClicked(
                                it
                            )
                        )
                    },
                    onBackPressed = { uiAction.invoke(AlbumDetailScreenUiAction.OnTimelineContentBackPressed) },
                    animatedVisibilityScope = this
                )
            }

            ScreenContentType.MEDIA -> {
                MediaContent(
                    mediaList = mediaList,
                    initialPagerPosition = uiState.currentMediaPosition,
                    modifier = modifier,
                    onBackPressed = {
                        uiAction.invoke(
                            AlbumDetailScreenUiAction.OnMediaContentBackPressed(
                                it
                            )
                        )
                    },
                    animatedVisibilityScope = this
                )
            }
        }
    }


}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
private fun AlbumDetailScreenPreview() {
    GalleryTheme {
        SharedTransitionLayout {
            AnimatedVisibility(true) {
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
    }
}