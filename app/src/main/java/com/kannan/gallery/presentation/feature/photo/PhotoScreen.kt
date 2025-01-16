package com.kannan.gallery.presentation.feature.photo

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
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
    mediaListPagedStream: Flow<PagingData<Media>>,
    shouldShowBottomBar: (Boolean) -> Unit,
    navigateUpCallback: () -> Unit
) {

    uiEvent.CollectAsEffect { event ->
        when (event) {
            PhotoScreenUiEvent.NavigateUp -> navigateUpCallback.invoke()
        }
    }

    LaunchedEffect(uiState.screenContentType) {
        when (uiState.screenContentType) {
            ScreenContentType.TIMELINE -> shouldShowBottomBar.invoke(true)
            ScreenContentType.MEDIA -> shouldShowBottomBar.invoke(false)
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
                    currentMediaPosition = uiState.currentMediaPosition,
                    onImageClicked = { uiAction.invoke(PhotoScreenUiAction.OnImageClicked(it)) },
                    onImageLongClicked = { uiAction.invoke(PhotoScreenUiAction.OnImageLongClicked(it)) },
                    onBackPressed = { uiAction.invoke(PhotoScreenUiAction.OnTimelineContentBackPressed) },
                    animatedVisibilityScope = this,
                    mediaListPagedStream = mediaListPagedStream
                )
            }

            ScreenContentType.MEDIA -> {
                MediaContent(
                    initialPagerPosition = uiState.currentMediaPosition,
                    modifier = modifier,
                    onBackPressed = {
                        uiAction.invoke(
                            PhotoScreenUiAction.OnMediaContentBackPressed(
                                it
                            )
                        )
                    },
                    animatedVisibilityScope = this,
                    mediaListPagedStream = mediaListPagedStream
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
            navigateUpCallback = {},
            shouldShowBottomBar = {},
            mediaListPagedStream = emptyFlow()
        )
    }
}