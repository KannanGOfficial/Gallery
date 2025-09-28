package com.kannan.gallery.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kannan.gallery.domain.model.Media
import com.kannan.gallery.presentation.feature.media.components.Thumbnail
import com.kannan.gallery.ui.theme.GalleryTheme
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TimelineContent(
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState,
    lazyPagingItems: LazyPagingItems<Media>,
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
                count = lazyPagingItems.itemCount,
                key = { index ->
                    val media = lazyPagingItems.peek(index)
                    media?.uri ?: index
                }
            ) { index: Int ->
                val data = lazyPagingItems[index]
                data?.let {
                    Thumbnail(
                        modifier = Modifier
                            .size(200.dp)
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "image/ ${data.id}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                            ),
                        data = data.uri,
                        contentDescription = data.uri,
                        onClick = {
                            onImageClicked.invoke(index, data)
                        },
                        onLongClick = {
                            onImageLongClicked.invoke(data)
                        },
                        isSelected = data.isSelected,
                        isInMediaSelectionMode = isInMediaSelectionMode
                    )
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
                    lazyPagingItems = flowOf(PagingData.empty<Media>()).collectAsLazyPagingItems(),
                    lazyGridState = rememberLazyGridState(),
                    isInMediaSelectionMode = false,
                    selectedItemCount = 2
                )
            }
        }
    }
}