package com.kannan.gallery.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kannan.gallery.R
import com.kannan.gallery.ui.theme.BlackScrim

@Composable
fun MediaViewTopBar(
    showUI: Boolean,
    currentDate: String,
    onBackPressed: () -> Unit,
) {
    AnimatedVisibility(
        visible = showUI,
        enter = fadeIn(tween()),
        exit = fadeOut(tween())
    ) {
        Row(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(BlackScrim, Color.Transparent)
                    )
                )
                .padding(start = 5.dp, end = 16.dp)
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(onClick = onBackPressed) {
                Image(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    colorFilter = ColorFilter.tint(Color.White),
                    contentDescription = stringResource(R.string.go_back),
                    modifier = Modifier
                        .height(48.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = currentDate.uppercase(),
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Preview
@Composable
private fun MediaViewTopBarPreview() {
    MaterialTheme {
        MediaViewTopBar(
            showUI = true,
            currentDate = "12 Jan 2024",
            onBackPressed = { }
        )
    }
}