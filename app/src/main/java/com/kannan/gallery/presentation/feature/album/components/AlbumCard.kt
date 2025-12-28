package com.kannan.gallery.presentation.feature.album.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import com.kannan.gallery.R
import com.kannan.gallery.domain.model.Album
import com.kannan.gallery.ui.theme.GalleryTheme
import com.kannan.gallery.utils.Font.JosefinRegular
import com.kannan.gallery.utils.MediaEqualityDelegate

@Composable
fun AlbumCard(
    album: Album,
    modifier: Modifier = Modifier,
    onAlbumClicked: ((Long) -> Unit)
) {

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(album.coverImage)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .placeholderMemoryCacheKey(album.coverImage)
            .crossfade(true)
            .scale(Scale.FIT)
            .build(),
        modelEqualityDelegate = MediaEqualityDelegate(),
        contentScale = ContentScale.FillBounds,
        filterQuality = FilterQuality.None
    )
    Surface(
        modifier = modifier.then(
            Modifier
                .height(220.dp)
                .width(150.dp)
        ),
        color = colorResource(R.color.eerie_black),
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 8.dp,   // Material 3
        shadowElevation = 8.dp,
        onClick = { onAlbumClicked.invoke(album.id) }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
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
                    textAlign = TextAlign.Center,
                    color = colorResource(R.color.white),
                    fontFamily = JosefinRegular,
                    fontSize = 13.sp
                )
            }
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
                coverImage = "",
                relativePath = ""
            ),
            onAlbumClicked = {}
        )
    }
}