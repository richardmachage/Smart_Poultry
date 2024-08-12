package com.forsythe.smartpoultry.presentation.composables.buttons

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign


@Composable
fun MyTextButton(onButtonClick: () -> Unit, btnText: String, modifier: Modifier = Modifier) {
    TextButton(onClick = onButtonClick) {
        Text(
            text = btnText,
            textAlign = TextAlign.Center
        )
    }
}