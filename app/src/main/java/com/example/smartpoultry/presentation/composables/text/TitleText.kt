package com.example.smartpoultry.presentation.composables.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun TitleText(
    modifier: Modifier = Modifier,
    text : String
){
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary,)
}