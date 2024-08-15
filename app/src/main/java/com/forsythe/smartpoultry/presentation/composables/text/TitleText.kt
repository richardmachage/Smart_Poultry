package com.forsythe.smartpoultry.presentation.composables.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit

@Composable
fun TitleText(
    modifier: Modifier = Modifier,
    text : String,
    fontSize : TextUnit? = null
){
    Text(
        modifier = modifier,
        text = text,
        fontSize = fontSize?:TextUnit.Unspecified,
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary,)
}