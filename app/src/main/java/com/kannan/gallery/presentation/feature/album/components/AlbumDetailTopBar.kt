package com.kannan.gallery.presentation.feature.album.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailTopBar(
    modifier: Modifier = Modifier,
    title: String
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(text = title)
        }
    )
}

@Preview
@Composable
private fun AlbumDetailTopBarPreview() {
    AlbumDetailTopBar(
        title = "DownLoads"
    )
}