package com.forsythe.smartpoultry.presentation.composables.progressBars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun MyCircularProgressBar(
    isLoading: Boolean,
    displayText: String = ""
) {
    if (isLoading) {
        Dialog(onDismissRequest = {}) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ){
                Column {
                    CircularProgressIndicator(modifier = Modifier.padding(10.dp), color = MaterialTheme.colorScheme.tertiary)
                    Text(text = displayText)
                }

            }
        }
    }
}