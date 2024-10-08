package com.kannan.gallery.feature.album.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kannan.gallery.core.presentation.navigation.NavigationScreen
import com.kannan.gallery.ui.theme.GalleryTheme

@Composable
fun AlbumTimelineScreen(
    modifier: Modifier = Modifier,
    navigateToCallBack: (NavigationScreen) -> Unit,
) {
    Box(
        modifier = modifier
            .then(Modifier.fillMaxSize())
            .clickable { navigateToCallBack.invoke(NavigationScreen.AlbumMediaScreen) },
        contentAlignment = Alignment.Center
    ) {
        Text(text = "AlbumTimelineScreen")
    }
}

@Preview(showBackground = true)
@Composable
private fun AlbumTimelineScreenPreview() {
    GalleryTheme {
        AlbumTimelineScreen(
            navigateToCallBack = {}
        )
    }
}