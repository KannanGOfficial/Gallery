package com.kannan.gallery.feature.timeline.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kannan.gallery.ui.theme.GalleryTheme

@Composable
fun TimelineMediaScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.then(Modifier.fillMaxSize()), contentAlignment = Alignment.Center) {
        Text(text = "TimelineMediaScreen")
    }
}

@Preview(showBackground = true)
@Composable
private fun TimelineMediaScreenPreview() {
    GalleryTheme {
        TimelineMediaScreen()
    }
}