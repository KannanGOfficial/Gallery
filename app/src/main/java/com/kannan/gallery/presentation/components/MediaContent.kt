package com.kannan.gallery.presentation.components

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kannan.gallery.domain.model.Media
import com.kannan.gallery.presentation.feature.media.components.ZoomableImage
import com.kannan.gallery.ui.theme.GalleryTheme
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MediaContent(
    modifier: Modifier = Modifier,
    initialPagerPosition: Int,
    lazyPagingItems: LazyPagingItems<Media>,
    onBackPressed: (Int) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onShareClicked: (Media) -> Unit,
    onCopyClicked: (Media) -> Unit,
    onMoveClicked: (Media) -> Unit,
    onTrashClicked: (Media) -> Unit,
) {
    val zoomedPageIndex = remember { mutableStateOf<Int?>(null) }

    val showUi = remember { mutableStateOf(false) }

    var isTransitionComplete by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Wait for the enter transition to finish
        animatedVisibilityScope.transition.apply {
            // suspend until transition reaches its target state
            while (currentState != targetState) {
                awaitFrame()
            }
        }
        isTransitionComplete = true
    }

    val pagerState = rememberPagerState(
        initialPage = initialPagerPosition,
        pageCount = { lazyPagingItems.itemCount }
    )

    BackHandler {
        onBackPressed.invoke(pagerState.currentPage)
    }

    HorizontalPager(
        state = pagerState,
        userScrollEnabled = zoomedPageIndex.value == null,
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) { pageNumber ->

        val data = lazyPagingItems[pageNumber]

        data?.let {
            Box(
                modifier = Modifier
                    .background(Color.Black)
                    .fillMaxSize()
            ) {
                ZoomableImage(
                    isTransitionComplete = isTransitionComplete,
                    uri = data.uri,
                    onScaleChanged = { scale ->
                        zoomedPageIndex.value = if (scale > 1f) pageNumber else null
                    },
                    onDismiss = {
                        showUi.value = !showUi.value
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                    /* .sharedBounds(
                 sharedContentState = rememberSharedContentState(key = "image/ ${data.uniqueId}"),
                 animatedVisibilityScope = animatedVisibilityScope,
                 resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
             )*/
                )

                MediaViewTopBar(
                    showUI = showUi.value,
                    onBackPressed = { onBackPressed(pageNumber) },
                    currentDate = data.dateModified,
                )

                MediaViewBottomBar(
                    showUI = showUi.value,
                    onShareButtonClick = { onShareClicked(it) },
                    onCopyButtonClick = { onCopyClicked(it) },
                    onMoveButtonClick = { onMoveClicked(it) },
                    onTrashButtonClick = { onTrashClicked(it) }
                )
            }
        }
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun DetailContentPreview() {
    GalleryTheme {
        SharedTransitionLayout {
            AnimatedContent(true, label = "") {
                MediaContent(
                    onBackPressed = {},
                    initialPagerPosition = 0,
                    animatedVisibilityScope = this,
                    lazyPagingItems = flowOf(PagingData.empty<Media>()).collectAsLazyPagingItems(),
                    onTrashClicked = {},
                    onCopyClicked = {},
                    onShareClicked = {},
                    onMoveClicked = {}
                )
            }
        }
    }
}