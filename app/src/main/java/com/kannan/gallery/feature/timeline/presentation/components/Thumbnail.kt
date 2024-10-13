package com.kannan.gallery.feature.timeline.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.kannan.gallery.feature.album.presentation.components.imagePainter
import com.kannan.gallery.ui.theme.GalleryTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Thumbnail(
    data: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onLongClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Image(
        painter = imagePainter(data = data),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .combinedClickable(
                onLongClick = onLongClick,
                onClick = onClick
            )
    )
}

@Preview
@Composable
private fun ThumbnailPreview() {
    GalleryTheme {
        Thumbnail(
            data = "",
            contentDescription = ""
        )
    }
}