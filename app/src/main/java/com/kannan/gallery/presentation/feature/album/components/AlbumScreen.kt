package com.kannan.gallery.presentation.feature.album.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kannan.gallery.R
import com.kannan.gallery.domain.model.Album
import com.kannan.gallery.presentation.feature.album.albumList1

@Composable
fun AlbumScreen(
    modifier: Modifier = Modifier,
    albumList: List<Album>,
    onAlbumClicked: ((Album) -> Unit)
) {
    LazyVerticalGrid(
        modifier = modifier
            .background(colorResource(R.color.night)),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(albumList.size) { index ->
            val album = albumList[index]
            AlbumCard(
                album = album,
                modifier = Modifier.padding(12.dp),
                onAlbumClicked = {
                    onAlbumClicked(album)
                }
            )
        }
    }
}

@Preview
@Composable
private fun AlbumScreenPreview() {
    AlbumScreen(
        albumList = albumList1,
        onAlbumClicked = {}
    )
}