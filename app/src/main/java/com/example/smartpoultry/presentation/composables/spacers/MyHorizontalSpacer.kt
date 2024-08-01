package com.example.smartpoultry.presentation.composables.spacers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyHorizontalSpacer(width: Int) {
    Spacer(
        modifier = Modifier
            .width(width.dp)
    )
}