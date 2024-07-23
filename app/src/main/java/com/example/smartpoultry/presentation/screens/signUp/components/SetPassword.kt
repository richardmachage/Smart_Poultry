package com.example.smartpoultry.presentation.screens.signUp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.smartpoultry.presentation.composables.MyPasswordEditText

@Composable
fun SetPassword(
    onResponse: (String, Boolean) -> Unit = {_,_ ->}
){
    var password by remember{ mutableStateOf("") }
    var confirmPassword by remember{ mutableStateOf("") }
    var error by remember{ mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        MyPasswordEditText( // Input new Password
            label = "Password",
            // hint = "New Password",
            iconLeading = Icons.Default.Lock,
            iconLeadingDescription = "Password",
            keyboardType = KeyboardType.Password,
            onValueChange = { text ->
                password = text
            },
        )

        MyPasswordEditText( // Confirm Password
            label = "Confirm Password",
            //hint = "Confirm Password",
            iconLeading = Icons.Default.Lock,
            iconLeadingDescription = "Password",
            keyboardType = KeyboardType.Password,
            onValueChange = { text ->
                confirmPassword = text
                if (confirmPassword != password){
                    error = true
                    onResponse(confirmPassword,true)
                }else{
                    error = false
                    onResponse(confirmPassword, false)
                }
            },
            hasError = error,
            supportingText = {if (error){
                Text(text = "passwords don't match")
            } }
        )
    }
}