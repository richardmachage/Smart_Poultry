package com.forsythe.smartpoultry.presentation.composables.buttons

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ToggleButton(
    modifier : Modifier = Modifier,
    isChecked : Boolean,
    onCheckedChange : (Boolean)-> Unit = {},
    isEnabled : Boolean = true
){
    Switch(
        modifier = modifier,
        checked = isChecked,
        onCheckedChange = { onCheckedChange(it)},
        enabled = isEnabled
    )
}