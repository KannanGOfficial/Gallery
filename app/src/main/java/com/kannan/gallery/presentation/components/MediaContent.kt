package com.kannan.gallery.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.kannan.gallery.domain.model.Media
import com.kannan.gallery.presentation.feature.photo.components.Thumbnail
import com.kannan.gallery.presentation.main.dummyTimelineMediaList
import com.kannan.gallery.ui.theme.GalleryTheme

@Composable
fun MediaContent(
    modifier: Modifier = Modifier,
    mediaList: List<Media>,
    initialPagerPosition: Int,
    onBackPressed: (Int) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = initialPagerPosition,
        pageCount = { mediaList.size }
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

        val data = mediaList[pageNumber]

        Thumbnail(
            data = data.uri,
            contentDescription = data.uri,
        )
    }
}

@Preview
@Composable
private fun DetailContentPreview() {
    GalleryTheme {
        MediaContent(
            mediaList = dummyTimelineMediaList,
            onBackPressed = {},
            initialPagerPosition = 0
        )
    }
}