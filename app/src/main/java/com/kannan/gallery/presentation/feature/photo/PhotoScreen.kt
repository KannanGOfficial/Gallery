package com.kannan.gallery.presentation.feature.photo

import androidx.compose.animation.AnimatedContent
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
import com.kannan.gallery.utils.ext.CollectAsEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PhotoScreen(
    modifier: Modifier = Modifier,
    uiState: PhotoScreenUiState,
    uiEvent: Flow<PhotoScreenUiEvent>,
    uiAction: (PhotoScreenUiAction) -> Unit,
    mediaList: List<Media>,
    navigateUpCallback: () -> Unit
) {

    uiEvent.CollectAsEffect { event ->
        when (event) {
            PhotoScreenUiEvent.NavigateUp -> navigateUpCallback.invoke()
        }
    }

    AnimatedContent(
        targetState = uiState.screenContentType,
        label = ""
    ) { targetState ->

        when (targetState) {
            ScreenContentType.TIMELINE -> {
                TimelineContent(
                    modifier = modifier,
                    mediaList = mediaList,
                    currentMediaPosition = uiState.currentMediaPosition,
                    onImageClicked = { uiAction.invoke(PhotoScreenUiAction.OnImageClicked(it)) },
                    onImageLongClicked = { uiAction.invoke(PhotoScreenUiAction.OnImageLongClicked(it)) },
                    onBackPressed = { uiAction.invoke(PhotoScreenUiAction.OnTimelineContentBackPressed) },
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
                            PhotoScreenUiAction.OnMediaContentBackPressed(
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
@Preview
@Composable
private fun PhotoScreenPreview() {
    SharedTransitionLayout {
        PhotoScreen(
            uiState = PhotoScreenUiState(
                screenContentType = ScreenContentType.MEDIA
            ),
            uiEvent = emptyFlow(),
            uiAction = {},
            mediaList = dummyTimelineMediaList,
            navigateUpCallback = {}
        )
    }
}