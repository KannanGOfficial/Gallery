package com.kannan.gallery.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.RestoreFromTrash
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kannan.gallery.R
import com.kannan.gallery.ui.theme.BlackScrim

@Composable
fun BoxScope.TrashedViewBottomBar(
    showUI: Boolean,
    onRestoreButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
) {
    AnimatedVisibility(
        visible = showUI,
        enter = fadeIn(tween()),
        exit = fadeOut(tween()),
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
    ) {
        Row(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, BlackScrim)
                    )
                )
                .padding(
                    top = 24.dp,
                    bottom = 24.dp
                )
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            SelectionColumn(
                imageVector = Icons.Outlined.RestoreFromTrash,
                tintColor = Color.White,
                title = stringResource(R.string.restore),
                onClick = onRestoreButtonClicked
            )

            SelectionColumn(
                imageVector = Icons.Outlined.DeleteOutline,
                tintColor = Color.White,
                title = stringResource(R.string.trash),
                onClick = onDeleteButtonClicked
            )
        }
    }
}

@Preview
@Composable
private fun TrashedViewBottomBarPreview() {
    MaterialTheme {
        Box {
            TrashedViewBottomBar(
                showUI = true,
                onDeleteButtonClicked = {},
                onRestoreButtonClicked = {}
            )
        }
    }
}