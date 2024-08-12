package com.forsythe.smartpoultry.presentation.composables.text

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NormText(
    modifier: Modifier = Modifier,
    text: String,
    maxLines : Int = 1

) {
    Text(
        modifier = modifier.padding(4.dp),
        text = text,
        maxLines = maxLines,
    )
}
