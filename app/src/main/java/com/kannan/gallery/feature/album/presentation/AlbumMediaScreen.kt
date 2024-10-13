package com.kannan.gallery.feature.album.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kannan.gallery.core.presentation.sharedViewModel.dummyAlbumMediaList
import com.kannan.gallery.feature.timeline.domain.Media
import com.kannan.gallery.feature.timeline.presentation.components.Thumbnail
import com.kannan.gallery.ui.theme.GalleryTheme

@Composable
fun AlbumMediaScreen(
    modifier: Modifier = Modifier,
    uiState: AlbumMediaScreenUiState,
    albumMediaList: List<Media>
) {
    val pagerState = rememberPagerState(
        initialPage = uiState.initialPagerPosition,
        pageCount = { albumMediaList.size }
    )

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize()
    ) { pageNumber ->

        val data = albumMediaList[pageNumber]

        Thumbnail(
            data = data.uri,
            contentDescription = data.uri
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AlbumMediaScreenPreview() {
    GalleryTheme {
        AlbumMediaScreen(
            uiState = AlbumMediaScreenUiState(),
            albumMediaList = dummyAlbumMediaList
        )
    }
}
