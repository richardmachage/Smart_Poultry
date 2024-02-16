package com.example.smartpoultry.presentation.screens.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CircularProgressBar(isLoading: Boolean) {
    if (isLoading) {
        Dialog(onDismissRequest = {}) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}