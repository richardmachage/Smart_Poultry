package com.example.smartpoultry.presentation.composables.textInputFields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.smartpoultry.R

@Composable
fun MyPasswordEditText(
    label: String,
    hint: String = "",
    iconLeading: ImageVector,
    iconLeadingDescription: String,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit = {},
    hasError: Boolean = false,
    supportingText :  @Composable (() -> Unit)? = null,
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var showPassword by remember { mutableStateOf(value = false) }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (8.dp), end = (8.dp)),
        value = text,
        label = { Text(label) },
        placeholder = { Text(hint) },
        onValueChange = { newText ->
            text = newText
            onValueChange(newText.text)
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        leadingIcon = {
            Icon(
                imageVector = iconLeading,
                contentDescription = iconLeadingDescription
            )
        },
        trailingIcon = {
            if (showPassword) {
                IconButton(onClick = { showPassword = false }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.visibility_off),
                        contentDescription = "hide_password"
                    )
                }
            } else {
                IconButton(onClick = { showPassword = true }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.visibility),
                        contentDescription = "show_password"
                    )
                }
            }
        },
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        singleLine = true,
        isError = hasError,
        supportingText = supportingText
    )
}