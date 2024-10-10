package com.kannan.gallery.feature.setup.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kannan.gallery.R
import com.kannan.gallery.ui.theme.GalleryTheme

@Composable
fun SetupScreenBottomBar(
    onCancelButtonClick: () -> Unit,
    onGetStartedButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface
            )
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(onClick = onCancelButtonClick) {
            Text(text = stringResource(id = R.string.action_cancel))
        }

        Button(onClick = onGetStartedButtonClick) {
            Text(text = stringResource(R.string.get_started))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SetupScreenBottomBarPreview() {
    GalleryTheme {
        SetupScreenBottomBar(
            onCancelButtonClick = {},
            onGetStartedButtonClick = {}
        )
    }
}