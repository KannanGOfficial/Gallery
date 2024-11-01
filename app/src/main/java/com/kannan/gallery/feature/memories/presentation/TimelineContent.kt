package com.kannan.gallery.feature.memories.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kannan.gallery.core.presentation.sharedViewModel.dummyTimelineMediaList
import com.kannan.gallery.feature.timeline.domain.Media
import com.kannan.gallery.feature.timeline.presentation.components.Thumbnail
import com.kannan.gallery.ui.theme.GalleryTheme

@Composable
fun TimelineContent(
    modifier: Modifier = Modifier,
    uiState: MemoriesScreenUiState.TimelineUiState,
    uiAction: ((MemoriesTimelineUiAction) -> Unit),
    mediaList: List<Media>
) {

    BackHandler {
        uiAction.invoke(MemoriesTimelineUiAction.OnBackPressed)
    }

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(3)
    ) {
        items(
            count = mediaList.size
        ) { index: Int ->
            val data = mediaList[index]
            Thumbnail(
                modifier = Modifier
                    .size(200.dp),
                data = data.uri,
                contentDescription = data.uri,
                onClick = {
                    uiAction.invoke(MemoriesTimelineUiAction.OnImageClicked(index))
                }
            )
        }
    }
}

@Preview
@Composable
private fun TimelineContentPreview() {
    GalleryTheme {
        TimelineContent(
            uiState = MemoriesScreenUiState.TimelineUiState(),
            uiAction = {},
            mediaList = dummyTimelineMediaList
        )
    }
}