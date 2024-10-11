package com.kannan.gallery.feature.album.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import com.kannan.gallery.R

@Composable
fun imagePainter(data: String) =
    if (LocalInspectionMode.current || true)
        painterResource(id = R.drawable.deadpool_primary)
    else
        rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(data)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .placeholderMemoryCacheKey(data)
                .crossfade(true)
                .scale(Scale.FIT)
                .build(),
            contentScale = ContentScale.FillBounds,
            filterQuality = FilterQuality.None
        )
