package com.kannan.gallery.presentation.feature.album.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kannan.gallery.R
import com.kannan.gallery.utils.Font

@Composable
fun AlbumDetailTopBar(
    modifier: Modifier = Modifier,
    title: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            modifier = Modifier.padding(20.dp),
            fontFamily = Font.JosefinSemiBold,
            fontSize = 18.sp,
            color = colorResource(R.color.white),
            text = title
        )
    }
}

@Preview
@Composable
private fun AlbumDetailTopBarPreview() {
    AlbumDetailTopBar(
        title = "DownLoads"
    )
}