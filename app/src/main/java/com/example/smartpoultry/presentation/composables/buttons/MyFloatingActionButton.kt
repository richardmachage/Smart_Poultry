package com.example.smartpoultry.presentation.composables.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit ,
    icon: @Composable () -> Unit ,
    text: @Composable () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            //.padding(8.dp)
            .wrapContentSize()
    ) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            icon()
        }
        Spacer(modifier = Modifier.height(4.dp))
        text()
    }
}