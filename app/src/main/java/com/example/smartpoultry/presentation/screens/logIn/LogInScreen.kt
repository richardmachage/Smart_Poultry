package com.example.smartpoultry.presentation.screens.logIn

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartpoultry.R
import com.example.smartpoultry.presentation.NavGraphs
import com.example.smartpoultry.presentation.composables.MyEditTextClear
import com.example.smartpoultry.presentation.composables.MyPasswordEditText
import com.example.smartpoultry.presentation.composables.MyTextButton
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.composables.NormButton
import com.example.smartpoultry.presentation.composables.NormText
import com.example.smartpoultry.presentation.composables.UserTypeDropDownMenu
import com.example.smartpoultry.presentation.destinations.MainScreenDestination
import com.example.smartpoultry.presentation.destinations.SignUpScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo

@Destination(start = true)
@Composable
fun LogInScreen(
    navigator: DestinationsNavigator
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,

            ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                MyVerticalSpacer(height = 50)
                Image(
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp),
                    //.padding(8.dp),
                    painter = painterResource(id = R.drawable.chicken),
                    contentDescription = "chicken",
                    contentScale = ContentScale.Fit
                )

                NormText(text = "SMART POULTRY")
                MyVerticalSpacer(height = 50)

                Column {
                    /*MyEditTextClear( //Input User type
                        label = "User type",
                        keyboardType = KeyboardType.Email,
                        iconLeading = Icons.Default.AccountCircle,
                        iconLeadingDescription = "AccountType",
                        hint = "User type"
                    )*/
                    
                    UserTypeDropDownMenu(
                        onItemClick = {},
                    )
                    
                    MyEditTextClear( //Input email address
                        label = "Email",
                        keyboardType = KeyboardType.Email,
                        iconLeading = Icons.Default.Email,
                        iconLeadingDescription = "Email",
                        hint = "Enter Email"
                    )
                    MyPasswordEditText( //input Password
                        label = "Password",
                        hint = "Enter Password",
                        iconLeading = Icons.Default.Lock,
                        iconLeadingDescription = "Password",
                        keyboardType = KeyboardType.Password
                    )


                    MyTextButton(
                        onButtonClick = { /*TODO*/ },
                        btnText = "Forgotten Password?",
                        modifier = Modifier
                    )
                }

                MyVerticalSpacer(height = 20)

                NormButton( // Log in Button
                    onButtonClick = {
                        navigator.navigate(MainScreenDestination) {
                            popUpTo(NavGraphs.root.startRoute) { inclusive = true }
                        }
                    },
                    btnName = "Log In",
                    modifier = Modifier.fillMaxWidth()
                )

            }

            MyTextButton( //Not registered button
                onButtonClick = { navigator.navigate(SignUpScreenDestination) },
                btnText = "You don't have an account? Click to sign up",
                modifier = Modifier
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLogInScreen() {
  //  LogInScreen(DestinationsNavigator)
}
