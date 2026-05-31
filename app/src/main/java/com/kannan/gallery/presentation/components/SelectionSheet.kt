package com.kannan.gallery.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DriveFileMove
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.CopyAll
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kannan.gallery.R

@Composable
fun SelectionSheet(
    modifier: Modifier = Modifier,
    selectedItemCount: Int,
    onCloseButtonClick: () -> Unit,
    onShareButtonClick: () -> Unit,
    onCopyButtonClick: () -> Unit,
    onMoveButtonClick: () -> Unit,
    onTrashButtonClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .navigationBarsPadding()
            .wrapContentWidth()
            .wrapContentHeight()
            .shadow(
                elevation = 4.dp
            )
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Selected $selectedItemCount")

            Image(
                imageVector = Icons.Outlined.Close,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.clickable {
                    onCloseButtonClick.invoke()
                },
                contentDescription = null
            )
        }



        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(
                    color = MaterialTheme.colorScheme.surface,
                )
                .padding(10.dp)

                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            SelectionColumn(
                imageVector = Icons.Outlined.Share,
                title = stringResource(R.string.share),
                onClick = onShareButtonClick
            )

            SelectionColumn(
                imageVector = Icons.Outlined.CopyAll,
                title = stringResource(R.string.copy),
                onClick = onCopyButtonClick
            )

            SelectionColumn(
                imageVector = Icons.AutoMirrored.Outlined.DriveFileMove,
                title = stringResource(R.string.move),
                onClick = onMoveButtonClick
            )

            SelectionColumn(
                imageVector = Icons.Outlined.DeleteOutline,
                title = stringResource(R.string.trash),
                onClick = onTrashButtonClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectionSheetPreview() {
    SelectionSheet(
        selectedItemCount = 1,
        onCloseButtonClick = {},
        onShareButtonClick = {},
        onCopyButtonClick = {},
        onMoveButtonClick = {},
        onTrashButtonClick = {},
    )
}