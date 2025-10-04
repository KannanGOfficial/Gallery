package com.kannan.gallery.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kannan.gallery.R
import com.kannan.gallery.utils.Font

@Composable
fun DateChip(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.TopStart
    ) {
        Box(
            modifier = modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(10.dp))
                .background(colorResource(R.color.eerie_black)),
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp),
                text = text,
                textAlign = TextAlign.Center,
                color = colorResource(R.color.white),
                fontFamily = Font.JosefinRegular,
                fontSize = 10.sp
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun DateChipPreview() {

    Box(modifier = Modifier.fillMaxWidth()) {
        DateChip(
            text = "Sun, 9 September"
        )
    }
}