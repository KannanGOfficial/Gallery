package com.kannan.gallery.feature.timeline.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kannan.gallery.core.presentation.sharedViewModel.dummyTimelineMediaList
import com.kannan.gallery.feature.timeline.domain.Media
import com.kannan.gallery.feature.timeline.presentation.components.Thumbnail
import com.kannan.gallery.ui.theme.GalleryTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TimelineMediaScreen(
    modifier: Modifier = Modifier,
    uiState: TimelineMediaScreenUiState,
    timelineMediaList: List<Media>,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val pagerState = rememberPagerState(
        initialPage = uiState.initialPagerPosition,
        pageCount = { timelineMediaList.size }
    )

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize()
    ) { pageNumber ->

        val data = timelineMediaList[pageNumber]

        Thumbnail(
            data = data.uri,
            contentDescription = data.uri,
            modifier = Modifier.sharedElement(
                state = rememberSharedContentState(key = "image/ ${data.id}"),
                animatedVisibilityScope = animatedVisibilityScope
            )
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
private fun TimelineMediaScreenPreview() {
    GalleryTheme {
        SharedTransitionLayout {
            AnimatedVisibility(true) {
                TimelineMediaScreen(
                    uiState = TimelineMediaScreenUiState(),
                    timelineMediaList = dummyTimelineMediaList,
                    animatedVisibilityScope = this
                )
            }
        }
    }
}
