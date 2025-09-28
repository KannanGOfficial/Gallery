package com.kannan.gallery.presentation.components

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kannan.gallery.domain.model.Media
import com.kannan.gallery.presentation.feature.media.components.Thumbnail
import com.kannan.gallery.ui.theme.GalleryTheme
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MediaContent(
    modifier: Modifier = Modifier,
    initialPagerPosition: Int,
    lazyPagingItems: LazyPagingItems<Media>,
    onBackPressed: (Int) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {

    val pagerState = rememberPagerState(
        initialPage = initialPagerPosition,
        pageCount = { lazyPagingItems.itemCount }
    )

    BackHandler {
        onBackPressed.invoke(pagerState.currentPage)
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) { pageNumber ->

        val data = lazyPagingItems[pageNumber]

        data?.let {
            Thumbnail(
                data = data.uri,
                contentDescription = data.uri,
                modifier = Modifier
                    .fillMaxWidth()
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(key = "image/ ${data.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                    )
            )
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
                )
            }
        }
    }
}