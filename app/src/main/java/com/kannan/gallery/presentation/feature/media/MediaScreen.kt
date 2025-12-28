package com.kannan.gallery.presentation.feature.media

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import com.kannan.gallery.utils.Animation
import com.kannan.gallery.utils.ext.CollectAsEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MediaScreen(
    modifier: Modifier = Modifier,
    uiState: MediaScreenUiState,
    uiEvent: Flow<MediaScreenUiEvent>,
    uiAction: (MediaScreenUiAction) -> Unit,
    mediaListPagedStream: Flow<PagingData<Media>>,
    mediaListUiModel: Flow<PagingData<MediaUiModel>>,
    shouldShowBottomBar: (Boolean) -> Unit,
    navigateUpCallback: () -> Unit
) {

    uiEvent.CollectAsEffect { event ->
        when (event) {
            MediaScreenUiEvent.NavigateUp -> navigateUpCallback.invoke()
        }
    }

    LaunchedEffect(uiState.shouldShowBottomBar) {
        shouldShowBottomBar(uiState.shouldShowBottomBar)
    }

    val lazyPagingItems = mediaListPagedStream.collectAsLazyPagingItems()
    val lazyPagingUiModel = mediaListUiModel.collectAsLazyPagingItems()
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(lazyPagingItems.itemSnapshotList) {
        val selectedMediaCount = lazyPagingItems.itemSnapshotList.count { it?.isSelected == true }
        val selectedMedia =
            lazyPagingItems.itemSnapshotList.filterNotNull().filter { it.isSelected }
        uiAction(MediaScreenUiAction.OnSelectedMediaListChanged(selectedMedia))
        uiAction(MediaScreenUiAction.OnSelectedItemCountChanged(selectedMediaCount))
        /*uiAction(
            MediaScreenUiAction.OnNewMediaListPaged(
                (lazyPagingItems.itemSnapshotList.toList().filterNotNull().toSet())
            )
        )*/
    }

    AnimatedContent(
        targetState = uiState.screenContentType,
        label = "",
        transitionSpec = { Animation.combinedAnimation }
    ) { targetState ->

        when (targetState) {
            ScreenContentType.TIMELINE -> {
                TimelineContent(
                    modifier = modifier,
                    onImageLongClicked = { uiAction.invoke(MediaScreenUiAction.OnImageLongClicked(it)) },
                    onBackPressed = { uiAction.invoke(MediaScreenUiAction.OnTimelineContentBackPressed) },
                    animatedVisibilityScope = this,
                    lazyPagingItems = lazyPagingItems,
                    lazyPagingUiModel = lazyPagingUiModel,
                    lazyGridState = lazyGridState,
                    isInMediaSelectionMode = uiState.isInMediaSelectionMode,
                    selectedItemCount = uiState.selectedMediaCount,
                    onImageClicked = { index, media ->
                        uiAction.invoke(
                            MediaScreenUiAction.OnImageClicked(
                                index = index,
                                media = media
                            )
                        )
                    },
                    onSelectionSheetCopyClicked = {
                        uiAction(MediaScreenUiAction.OnSelectionSheetCopyClicked)
                    },
                    onSelectionSheetCloseClicked = {
                        uiAction(MediaScreenUiAction.OnSelectionSheetCloseClicked)
                    },
                    albumList = uiState.albumList,
                    shouldShowAlbumBottomSheet = uiState.shouldShowAlbumBottomSheet,
                    onAlbumBottomSheetDismissed = { uiAction(MediaScreenUiAction.OnAlbumBottomSheetDismissed) },
                    onAlbumPathSelected = { uiAction(MediaScreenUiAction.OnAlbumPathSelected(it)) }
                )
            }

            ScreenContentType.MEDIA -> {
                MediaContent(
                    initialPagerPosition = uiState.currentMediaPosition,
                    modifier = modifier,
                    onBackPressed = {
                        uiAction.invoke(
                            MediaScreenUiAction.OnMediaContentBackPressed(
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
@Preview
@Composable
private fun MediaScreenPreview() {
    SharedTransitionLayout {
        MediaScreen(
            uiState = MediaScreenUiState(
                screenContentType = ScreenContentType.MEDIA
            ),
            uiEvent = emptyFlow(),
            uiAction = {},
            navigateUpCallback = {},
            shouldShowBottomBar = {},
            mediaListPagedStream = emptyFlow(),
            mediaListUiModel = emptyFlow()
        )
    }
}