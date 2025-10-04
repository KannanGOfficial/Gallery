package com.kannan.gallery.presentation.feature.album

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.kannan.gallery.domain.model.Media
import com.kannan.gallery.domain.model.MediaUiModel
import com.kannan.gallery.presentation.components.MediaContent
import com.kannan.gallery.presentation.components.TimelineContent
import com.kannan.gallery.presentation.feature.album.components.AlbumDetailTopBar
import com.kannan.gallery.presentation.feature.media.ScreenContentType
import com.kannan.gallery.ui.theme.GalleryTheme
import com.kannan.gallery.utils.Animation
import com.kannan.gallery.utils.ext.CollectAsEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.AlbumDetailScreen(
    modifier: Modifier = Modifier,
    uiState: AlbumDetailScreenUiState,
    uiEvent: Flow<AlbumDetailScreenUiEvent>,
    mediaListPagedStream: Flow<PagingData<Media>>,
    mediaListUiModel: Flow<PagingData<MediaUiModel>>,
    uiAction: (AlbumDetailScreenUiAction) -> Unit,
    navigateUpCallback: () -> Unit
) {
    uiEvent.CollectAsEffect { event ->
        when (event) {
            AlbumDetailScreenUiEvent.NavigateUp -> navigateUpCallback.invoke()
        }
    }

    val lazyPagingItems = mediaListPagedStream.collectAsLazyPagingItems()
    val lazyPagingUiModel = mediaListUiModel.collectAsLazyPagingItems()
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(lazyPagingItems.itemSnapshotList) {
        val selectedMediaCount = lazyPagingItems.itemSnapshotList.count { it?.isSelected == true }
        uiAction(AlbumDetailScreenUiAction.OnSelectedItemCountChanged(selectedMediaCount))
    }

    AnimatedContent(
        uiState.screenContentType,
        transitionSpec = { Animation.combinedAnimation },
        label = "",
        modifier = modifier
    ) { targetState ->

        when (targetState) {
            ScreenContentType.TIMELINE -> {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AlbumDetailTopBar(
                            title = uiState.albumName
                        )
                    }
                ) { paddingValues ->
                    TimelineContent(
                        modifier = Modifier.padding(paddingValues),
                        onImageClicked = { index, media ->
                            uiAction.invoke(
                                AlbumDetailScreenUiAction.OnImageClicked(
                                    index = index,
                                    media = media
                                )
                            )
                        },
                        onImageLongClicked = {
                            uiAction.invoke(
                                AlbumDetailScreenUiAction.OnImageLongClicked(
                                    it
                                )
                            )
                        },
                        onBackPressed = { uiAction.invoke(AlbumDetailScreenUiAction.OnTimelineContentBackPressed) },
                        animatedVisibilityScope = this,
                        lazyPagingUiModel = lazyPagingUiModel,
                        lazyPagingItems = lazyPagingItems,
                        lazyGridState = lazyGridState,
                        isInMediaSelectionMode = uiState.isInMediaSelectionMode,
                        selectedItemCount = uiState.selectedMediaCount
                    )
                }
            }

            ScreenContentType.MEDIA -> {
                MediaContent(
                    initialPagerPosition = uiState.currentMediaPosition,
                    onBackPressed = {
                        uiAction.invoke(
                            AlbumDetailScreenUiAction.OnMediaContentBackPressed(
                                it
                            )
                        )
                    },
                    animatedVisibilityScope = this,
                    lazyPagingItems = lazyPagingItems,
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
                        screenContentType = ScreenContentType.TIMELINE,
                        albumName = "Downloads"
                    ),
                    uiEvent = emptyFlow(),
                    uiAction = {},
                    navigateUpCallback = {},
                    mediaListPagedStream = emptyFlow(),
                    mediaListUiModel = emptyFlow()
                )
            }
        }
    }
}