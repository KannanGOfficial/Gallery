package com.kannan.gallery.feature.setup.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kannan.gallery.R
import com.kannan.gallery.feature.setup.presentation.component.SetupScreenBottomBar
import com.kannan.gallery.ui.theme.GalleryTheme

@Composable
fun SetupScreenContent(
    modifier: Modifier = Modifier,
    uiState: SetupScreenUiState,
    uiAction: ((SetupScreenUiAction) -> Unit)
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            SetupScreenBottomBar(
                onCancelButtonClick = {
                    uiAction.invoke(SetupScreenUiAction.OnCancelButtonClicked)
                },
                onGetStartedButtonClick = {
                    uiAction.invoke(SetupScreenUiAction.OnGetStartedButtonClicked)
                }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .padding(top = 24.dp)
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.app_name))

            if (uiState.showAllowExternalManageFilesButton) {
                Button(
                    onClick = {
                        uiAction.invoke(SetupScreenUiAction.AllowToManageFilesButtonClicked)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                ) {
                    Text(text = stringResource(R.string.allow_to_manage_files))
                }
            }
        }
    }
}

@Preview
@Composable
private fun SetupScreenContentPreview() {
    GalleryTheme {
        SetupScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = SetupScreenUiState(),
            uiAction = {}
        )
    }
}
