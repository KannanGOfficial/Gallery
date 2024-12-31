package com.kannan.gallery.presentation.feature.album

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
import com.kannan.gallery.domain.model.Media
import com.kannan.gallery.presentation.feature.photo.components.Thumbnail
import com.kannan.gallery.presentation.main.dummyAlbumMediaList
import com.kannan.gallery.ui.theme.GalleryTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.AlbumMediaScreen(
    modifier: Modifier = Modifier,
    uiState: AlbumMediaScreenUiState,
    albumMediaList: List<Media>,
    animatedVisibilityScope: AnimatedVisibilityScope
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
private fun AlbumMediaScreenPreview() {
    GalleryTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                AlbumMediaScreen(
                    uiState = AlbumMediaScreenUiState(),
                    albumMediaList = dummyAlbumMediaList,
                    animatedVisibilityScope = this
                )
            }
        }
    }
}
