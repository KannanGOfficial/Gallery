package com.kannan.gallery.feature.album.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kannan.gallery.feature.album.domain.Album
import com.kannan.gallery.ui.theme.GalleryTheme

@Composable
fun AlbumCard(
    album: Album,
    modifier: Modifier = Modifier,
    onAlbumClicked: ((Long) -> Unit)
) {
    val painter = imagePainter(data = album.coverImage)

    OutlinedCard(
        modifier = modifier.then(
            Modifier
                .height(220.dp)
                .width(150.dp)
        ),
        shape = RoundedCornerShape(12.dp),
        onClick = { onAlbumClicked.invoke(album.id) }
    ) {
        Image(
            painter = painter,
            contentDescription = album.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = album.name,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AlbumCardPreview() {
    GalleryTheme {
        AlbumCard(
            album = Album(
                id = 0,
                name = "Camera",
                coverImage = ""
            ),
            onAlbumClicked = {}
        )
    }
}