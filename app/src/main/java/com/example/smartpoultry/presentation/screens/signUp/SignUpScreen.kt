package com.example.smartpoultry.presentation.screens.signUp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartpoultry.presentation.screens.composables.MyEditTextClear
import com.example.smartpoultry.presentation.screens.composables.MyPasswordEditText
import com.example.smartpoultry.presentation.screens.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.screens.composables.NormButton
import com.example.smartpoultry.presentation.screens.composables.NormText

@Composable
fun SignUpScreen() {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            NormText(text = "Sign Up")
            MyVerticalSpacer(height = 30)

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                MyEditTextClear( //Input User name
                    label = "User Name",
                    keyboardType = KeyboardType.Text,
                    iconLeading = Icons.Default.AccountCircle,
                    iconLeadingDescription = "User",
                    hint = "Enter name"
                )

                MyEditTextClear( // Input Email address
                    label = "Email",
                    hint = "Enter Email",
                    iconLeading = Icons.Default.Email,
                    iconLeadingDescription = "Email",
                    keyboardType = KeyboardType.Email
                )

                MyPasswordEditText( // Input new Password
                    label = "Password",
                    hint = "New Password",
                    iconLeading = Icons.Default.Lock,
                    iconLeadingDescription = "Password",
                    keyboardType = KeyboardType.Password
                )

                MyPasswordEditText( // Confirm Password
                    label = "Confirm Password",
                    hint = "Confirm Password",
                    iconLeading = Icons.Default.Lock,
                    iconLeadingDescription = "Password",
                    keyboardType = KeyboardType.Password
                )
            }

            MyVerticalSpacer(height = 30)

            NormButton(
                onButtonClick = {
                    /*TODO*/
                },
                btnName = "Sign Up",
                modifier = Modifier
            )

        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpPreview(){
    SignUpScreen()
}