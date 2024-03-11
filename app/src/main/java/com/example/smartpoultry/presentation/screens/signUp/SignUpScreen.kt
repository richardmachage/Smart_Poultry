package com.example.smartpoultry.presentation.screens.signUp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.presentation.NavGraphs
import com.example.smartpoultry.presentation.composables.MyEditTextClear
import com.example.smartpoultry.presentation.composables.MyPasswordEditText
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.composables.NormButton
import com.example.smartpoultry.presentation.composables.UserTypeDropDownMenu
import com.example.smartpoultry.presentation.destinations.LogInScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SignUpScreen(
    navigator: DestinationsNavigator
) {

    val singUpViewModel = hiltViewModel<SignUpViewModel>()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Sign Up") },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                navigationIcon = {
                    IconButton(onClick = {
                        navigator.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
/*
                NormText(text = "Sign Up")
                MyVerticalSpacer(height = 30)
*/        MyVerticalSpacer(height = 10)


                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    UserTypeDropDownMenu(
                        onItemClick = {},
                    )

                    MyEditTextClear( // Input Email address
                        label = "Email",
                        hint = "Enter Email",
                        iconLeading = Icons.Default.Email,
                        iconLeadingDescription = "Email",
                        keyboardType = KeyboardType.Email,
                        onValueChange = { text ->
                            singUpViewModel.email.value = text
                        }
                    )

                    MyPasswordEditText( // Input new Password
                        label = "Password",
                        hint = "New Password",
                        iconLeading = Icons.Default.Lock,
                        iconLeadingDescription = "Password",
                        keyboardType = KeyboardType.Password,
                        onValueChange = { text ->
                            singUpViewModel.password.value = text
                        }
                    )

                    MyPasswordEditText( // Confirm Password
                        label = "Confirm Password",
                        hint = "Confirm Password",
                        iconLeading = Icons.Default.Lock,
                        iconLeadingDescription = "Password",
                        keyboardType = KeyboardType.Password,
                        onValueChange = { text ->
                            singUpViewModel.confirmPassword.value = text
                        }
                    )
                }

                MyVerticalSpacer(height = 30)

                NormButton( //The sign up Button
                    onButtonClick = {
                        navigator.navigate(LogInScreenDestination) {
                            popUpTo(NavGraphs.root.startRoute) { inclusive = true }
                        }

                    },
                    btnName = "Sign Up",
                    modifier = Modifier.fillMaxWidth()
                )

            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpPreview() {
    //SignUpScreen()
}