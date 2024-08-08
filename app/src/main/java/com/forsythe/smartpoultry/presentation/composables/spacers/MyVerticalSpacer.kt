package com.forsythe.smartpoultry.presentation.composables.spacers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyVerticalSpacer(height: Int) {
    Spacer(
        modifier = Modifier
            .height(height.dp)
    )
}