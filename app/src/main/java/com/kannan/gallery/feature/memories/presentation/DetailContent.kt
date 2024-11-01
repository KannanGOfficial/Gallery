package com.kannan.gallery.feature.memories.presentation

import androidx.activity.compose.BackHandler
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
fun DetailContent(
    modifier: Modifier = Modifier,
    uiState: MemoriesScreenUiState.DetailUiState,
    uiAction: ((MemoriesDetailUiAction) -> Unit),
    mediaList: List<Media>
) {
    val pagerState = rememberPagerState(
        initialPage = uiState.initialPagerPosition,
        pageCount = { mediaList.size }
    )

    BackHandler {
        uiAction.invoke(MemoriesDetailUiAction.OnBackPressed)
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize()
    ) { pageNumber ->

        val data = mediaList[pageNumber]

        Thumbnail(
            data = data.uri,
            contentDescription = data.uri
        )
    }
}

@Preview
@Composable
private fun DetailContentPreview() {
    GalleryTheme {
        DetailContent(
            uiState = MemoriesScreenUiState.DetailUiState(),
            uiAction = {},
            mediaList = dummyTimelineMediaList
        )
    }
}