package com.kannan.gallery.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kannan.gallery.domain.model.Album
import com.kannan.gallery.domain.model.Media
import com.kannan.gallery.domain.model.MediaUiModel
import com.kannan.gallery.presentation.feature.album.components.AlbumScreen
import com.kannan.gallery.presentation.feature.media.components.Thumbnail
import com.kannan.gallery.ui.theme.GalleryTheme
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.TimelineContent(
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState,
    lazyPagingItems: LazyPagingItems<Media>,
    lazyPagingUiModel: LazyPagingItems<MediaUiModel>,
    onImageClicked: (Int, Media) -> Unit,
    onImageLongClicked: (Media) -> Unit,
    onBackPressed: () -> Unit,
    onSelectionSheetCloseClicked: () -> Unit,
    onSelectionSheetCopyClicked: () -> Unit,
    onSelectionSheetMoveClicked: () -> Unit,
    isInMediaSelectionMode: Boolean,
    selectedItemCount: Int,
    animatedVisibilityScope: AnimatedVisibilityScope,
    albumList: List<Album>,
    shouldShowAlbumBottomSheet: Boolean,
    onAlbumBottomSheetDismissed: () -> Unit,
    onAlbumPathSelected: (String) -> Unit
) {

    BackHandler(onBack = onBackPressed)

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            state = lazyGridState,
            columns = GridCells.Fixed(3)
        ) {
            items(
                count = lazyPagingUiModel.itemCount,
                key = { index ->
                    val uiModel = lazyPagingUiModel.peek(index)
                    uiModel?.key ?: index
                },
                span = { index ->
                    val uiModel = lazyPagingUiModel.peek(index)
                    GridItemSpan(
                        when (uiModel) {
                            is MediaUiModel.Header -> maxLineSpan
                            else -> 1
                        }
                    )
                }
            ) { index: Int ->
                val data = lazyPagingUiModel[index]
                data?.let {
                    when (data) {
                        is MediaUiModel.Item -> {
                            Thumbnail(
                                modifier = Modifier
                                    .size(130.dp, 150.dp)
                                    .sharedBounds(
                                        sharedContentState = rememberSharedContentState(key = "image/ ${data.item.uniqueId}"),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                                    ),
                                crossFade = true,
                                contentScale = ContentScale.Crop,
                                data = data.item.uri,
                                contentDescription = data.item.uri,
                                onClick = {
                                    val position =
                                        lazyPagingItems.itemSnapshotList.indexOf(data.item)
                                    onImageClicked.invoke(position, data.item)
                                },
                                onLongClick = {
                                    onImageLongClicked.invoke(data.item)
                                },
                                isSelected = data.item.isSelected,
                                isInMediaSelectionMode = isInMediaSelectionMode
                            )
                        }

                        is MediaUiModel.Header -> {
                            DateChip(
                                text = data.title
                            )
                        }
                    }
                }
            }
        }

        if (isInMediaSelectionMode) {
            SelectionSheet(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                selectedItemCount = selectedItemCount,
                onCopyButtonClick = onSelectionSheetCopyClicked,
                onMoveButtonClick = onSelectionSheetMoveClicked,
                onCloseButtonClick = onSelectionSheetCloseClicked,
                onShareButtonClick = {}
            )
        }

        if (shouldShowAlbumBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = onAlbumBottomSheetDismissed
            ) {
                AlbumScreen(
                    albumList = albumList,
                    onAlbumClicked = {
                        onAlbumPathSelected(it.relativePath)
                    }
                )
            }
        }
    }


}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
private fun TimelineContentPreview() {
    GalleryTheme {
        SharedTransitionLayout {
            AnimatedVisibility(true) {
                TimelineContent(
                    onBackPressed = {},
                    onImageClicked = { _, _ -> },
                    onImageLongClicked = {},
                    animatedVisibilityScope = this,
                    lazyPagingUiModel = flowOf(PagingData.empty<MediaUiModel>()).collectAsLazyPagingItems(),
                    lazyPagingItems = flowOf(PagingData.empty<Media>()).collectAsLazyPagingItems(),
                    lazyGridState = rememberLazyGridState(),
                    isInMediaSelectionMode = false,
                    selectedItemCount = 2,
                    onSelectionSheetCloseClicked = {},
                    onSelectionSheetCopyClicked = {},
                    onSelectionSheetMoveClicked = {},
                    albumList = emptyList(),
                    shouldShowAlbumBottomSheet = false,
                    onAlbumBottomSheetDismissed = {},
                    onAlbumPathSelected = {}
                )
            }
        }
    }
}