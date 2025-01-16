package com.kannan.gallery.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.kannan.gallery.domain.model.Media
import com.kannan.gallery.presentation.feature.media.components.Thumbnail
import com.kannan.gallery.ui.theme.GalleryTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TimelineContent(
    modifier: Modifier = Modifier,
    currentMediaPosition: Int,
    mediaListPagedStream: Flow<PagingData<Media>>,
    onImageClicked: (Int) -> Unit,
    onImageLongClicked: (Media) -> Unit,
    onBackPressed: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val lazyPagingItems = mediaListPagedStream.collectAsLazyPagingItems()

    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(currentMediaPosition) {

        val firstVisibleIndex = lazyGridState.firstVisibleItemIndex
        val lastVisibleIndex = lazyGridState.layoutInfo.visibleItemsInfo.size - 1

        val isCurrentMediaItemIsVisible =
            (firstVisibleIndex..lastVisibleIndex).contains(currentMediaPosition)

        if (!isCurrentMediaItemIsVisible) {
            lazyGridState.scrollToItem(currentMediaPosition)
        }
    }

    BackHandler(onBack = onBackPressed)

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        state = lazyGridState,
        columns = GridCells.Fixed(3)
    ) {
        items(
            count = lazyPagingItems.itemCount
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
                        onImageClicked.invoke(index)
                    },
                    onLongClick = {
                        onImageLongClicked.invoke(data)
                    }
                )
            }

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
                    currentMediaPosition = 0,
                    onBackPressed = {},
                    onImageClicked = {},
                    onImageLongClicked = {},
                    animatedVisibilityScope = this,
                    mediaListPagedStream = emptyFlow()
                )
            }
        }
    }
}