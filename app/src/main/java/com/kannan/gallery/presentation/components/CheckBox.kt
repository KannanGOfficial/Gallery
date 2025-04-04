package com.kannan.gallery.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CheckBox(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
) {
    val image = if (isChecked) Icons.Filled.CheckCircle else Icons.Outlined.Circle
    val color = if (isChecked) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.onSurface

    Image(
        imageVector = image,
        colorFilter = ColorFilter.tint(color),
        modifier = modifier,
        contentDescription = null
    )
}

@Preview(showBackground = true)
@Composable
private fun CheckBoxPreview() {
    CheckBox(isChecked = false)
}