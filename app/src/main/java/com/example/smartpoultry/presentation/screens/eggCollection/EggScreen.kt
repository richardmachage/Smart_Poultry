package com.example.smartpoultry.presentation.screens.eggCollection

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EggScreen( modifier: Modifier){
    Surface (
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ){

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PrevEgg(){
    EggScreen(modifier = Modifier)
}