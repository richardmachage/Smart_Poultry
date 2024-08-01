package com.example.smartpoultry.presentation.composables.textInputFields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun MyOutlineTextFiled(
    modifier: Modifier = Modifier,
    label: String,
    keyboardType: KeyboardType,
    maxLines: Int = 1,
    initialText: String,
    onValueChange: (String) -> Unit,
) {
    var text by remember {
        mutableStateOf(initialText)
    }
    OutlinedTextField(
        modifier = modifier,
        label = { Text(text = label) },
        value = text,
        onValueChange = {
            text = it
            onValueChange(text)
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        maxLines = maxLines,
    )
}