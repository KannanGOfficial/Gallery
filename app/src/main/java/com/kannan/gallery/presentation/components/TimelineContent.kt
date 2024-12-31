package com.kannan.gallery.presentation.components

import androidx.activity.compose.BackHandler
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
import com.kannan.gallery.data.dummyTimelineMediaList
import com.kannan.gallery.domain.model.Media
import com.kannan.gallery.presentation.feature.photo.components.Thumbnail
import com.kannan.gallery.ui.theme.GalleryTheme

@Composable
fun TimelineContent(
    modifier: Modifier = Modifier,
    mediaList: List<Media>,
    currentMediaPosition: Int,
    onImageClicked: (Media) -> Unit,
    onImageLongClicked: (Media) -> Unit,
    onBackPressed: () -> Unit
) {

    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(currentMediaPosition) {
        lazyGridState.scrollToItem(currentMediaPosition)
    }

    BackHandler(onBack = onBackPressed)

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        state = lazyGridState,
        columns = GridCells.Fixed(3)
    ) {
        items(
            count = mediaList.size
        ) { index: Int ->
            val data = mediaList[index]
            Thumbnail(
                modifier = Modifier.size(200.dp),
                data = data.uri,
                contentDescription = data.uri,
                onClick = {
                    onImageClicked.invoke(data)
                },
                onLongClick = {
                    onImageLongClicked.invoke(data)
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
            mediaList = dummyTimelineMediaList,
            currentMediaPosition = 0,
            onBackPressed = {},
            onImageClicked = {},
            onImageLongClicked = {}
        )
    }
}