package com.kannan.gallery.presentation.feature.album.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kannan.gallery.R
import com.kannan.gallery.domain.Const
import com.kannan.gallery.ui.theme.GalleryTheme
import com.kannan.gallery.utils.Font.JosefinRegular

@Composable
fun Card(
    title: String,
    key: String,
    modifier: Modifier = Modifier,
    onCardClicked: ((String) -> Unit)
) {
    Surface(
        modifier = modifier,
        color = colorResource(R.color.eerie_black),
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 8.dp,   // Material 3
        shadowElevation = 8.dp,
        onClick = { onCardClicked.invoke(key) }
    ) {

        Box(
            modifier = Modifier.padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                color = colorResource(R.color.white),
                fontFamily = JosefinRegular,
                fontSize = 13.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardPreview() {
    GalleryTheme {
        Card(
            title = "Trashed",
            key = Const.TRASHED,
            onCardClicked = {}
        )
    }
}