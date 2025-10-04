package com.kannan.gallery.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kannan.gallery.domain.model.Media
import com.kannan.gallery.domain.model.MediaUiModel
import com.kannan.gallery.presentation.feature.media.components.Thumbnail
import com.kannan.gallery.ui.theme.GalleryTheme
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TimelineContent(
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState,
    lazyPagingItems: LazyPagingItems<Media>,
    lazyPagingUiModel: LazyPagingItems<MediaUiModel>,
    onImageClicked: (Int, Media) -> Unit,
    onImageLongClicked: (Media) -> Unit,
    onBackPressed: () -> Unit,
    isInMediaSelectionMode: Boolean,
    selectedItemCount: Int,
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    BackHandler(onBack = onBackPressed)

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
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
                                    .size(200.dp)
                                    .sharedBounds(
                                        sharedContentState = rememberSharedContentState(key = "image/ ${data.item.id}"),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                                    ),
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
                            Text(
                                text = data.title,
                                modifier = Modifier.fillMaxWidth()
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
                onCopyButtonClick = {},
                onMoveButtonClick = {},
                onCloseButtonClick = {},
                onShareButtonClick = {}
            )
        }
    }


}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
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
                    selectedItemCount = 2
                )
            }
        }
    }
}