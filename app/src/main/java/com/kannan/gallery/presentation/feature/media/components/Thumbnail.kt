package com.kannan.gallery.presentation.feature.media.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.kannan.gallery.R
import com.kannan.gallery.presentation.components.CheckBox
import com.kannan.gallery.ui.theme.GalleryTheme
import com.kannan.gallery.utils.MediaEqualityDelegate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Thumbnail(
    data: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onLongClick: () -> Unit = {},
    onClick: () -> Unit = {},
    isSelected: Boolean = false,
    contentScale: ContentScale = ContentScale.FillWidth,
    crossFade: Boolean = false,
    isInMediaSelectionMode: Boolean = false
) {

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(data)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .crossfade(crossFade)
            .placeholder(R.color.eerie_black)
            .placeholderMemoryCacheKey(data)
            .build(),
        modelEqualityDelegate = MediaEqualityDelegate(),
        contentScale = contentScale,
        filterQuality = FilterQuality.None
    )
    Image(
        painter = painter,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .combinedClickable(
                onLongClick = onLongClick,
                onClick = onClick
            )
    )

    if (isInMediaSelectionMode) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
            CheckBox(
                isChecked = isSelected,
                modifier = Modifier
                    .padding(14.dp)
            )
        }
    }
}

@Preview
@Composable
private fun ThumbnailPreview() {
    GalleryTheme {
        Thumbnail(
            data = "",
            contentDescription = "",
            isSelected = true,
            isInMediaSelectionMode = true
        )
    }
}