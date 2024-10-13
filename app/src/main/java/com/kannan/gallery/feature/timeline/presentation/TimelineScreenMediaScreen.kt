package com.kannan.gallery.feature.timeline.presentation

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

@Composable
fun TimelineMediaScreen(
    modifier: Modifier = Modifier,
    uiState: TimelineMediaScreenUiState,
    timelineMediaList: List<Media>
) {
    val pagerState = rememberPagerState(
        initialPage = uiState.scrollToPosition,
        pageCount = { timelineMediaList.size }
    )

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize()
    ) { pageNumber ->

        val data = timelineMediaList[pageNumber]

        Thumbnail(
            data = data.uri,
            contentDescription = data.uri
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TimelineMediaScreenPreview() {
    GalleryTheme {
        TimelineMediaScreen(
            uiState = TimelineMediaScreenUiState(),
            timelineMediaList = dummyTimelineMediaList
        )
    }
}
