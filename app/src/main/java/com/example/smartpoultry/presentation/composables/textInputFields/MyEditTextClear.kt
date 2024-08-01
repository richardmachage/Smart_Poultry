package com.example.smartpoultry.presentation.composables.textInputFields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.smartpoultry.presentation.composables.spacers.MyHorizontalSpacer

@Composable
fun MyEditTextClear(
    value: String = "",
    label: String = "",
    hint: String = "",
    iconLeading: ImageVector,
    iconLeadingDescription: String,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit = {},
    hasError : Boolean = false,
    singleLine: Boolean = true,
    onClear : () ->Unit = {},
    prefix :  @Composable() (() -> Unit)? = null,
    supportingText :  @Composable (() -> Unit)? = null,
) {
    var text by remember { mutableStateOf(TextFieldValue(value)) }
    val color by animateColorAsState(
        targetValue = if (hasError) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        label = "color",
    )
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (8.dp), end = (8.dp)),
        value = text,
        label = { Text(text = label) },
        placeholder = { Text(text = hint) },
        onValueChange = { newText ->
            text = newText
            onValueChange(newText.text)
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        leadingIcon = {
            Row(
                modifier = Modifier.padding(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(5.dp),
                    imageVector = iconLeading,
                    contentDescription = iconLeadingDescription
                )

                if (prefix != null) {
                    MyHorizontalSpacer(width = 5)
                    prefix()
                }
            }
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = text.toString().isNotEmpty(),
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                IconButton(onClick = {
                    onClear()
                    text = TextFieldValue("")
                }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "clear text",
                        modifier = Modifier.size(24.dp),
                        tint = color
                    )
                }
            }

            /* IconButton(onClick = { text = TextFieldValue("") }) {
                 Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear_input")
             }*/
        },
        singleLine = singleLine,
        supportingText = supportingText
    )
}