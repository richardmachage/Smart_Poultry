package com.example.smartpoultry.presentation.screens.logIn

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.R
import com.example.smartpoultry.destinations.MainScreenDestination
import com.example.smartpoultry.destinations.SignUpScreenDestination
import com.example.smartpoultry.presentation.composables.MyCircularProgressBar
import com.example.smartpoultry.presentation.composables.MyEditTextClear
import com.example.smartpoultry.presentation.composables.MyPasswordEditText
import com.example.smartpoultry.presentation.composables.MyTextButton
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.composables.NormButton
import com.example.smartpoultry.presentation.composables.NormText
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo

@Destination
@Composable
fun LogInScreen(
    navigator: DestinationsNavigator
) {

    val logInViewModel = hiltViewModel<LogInViewModel>()
    val context = LocalContext.current

    //observing the viewmodel state and show toast when validation error message changes
    LaunchedEffect(key1 = logInViewModel.validateError.value){
        logInViewModel.validateError.value.let {toastMessage->
            if (toastMessage.isNotBlank()){
                Toast.makeText(context,toastMessage,Toast.LENGTH_SHORT).show()
                // Reset the error message in the ViewModel if needed to prevent repeated toasts
                logInViewModel.validateError.value = ""
            }
        }
    }

    //observing the viewmodel state and show toast when validation error message changes
    LaunchedEffect(key1 = logInViewModel.isLogInSuccess){
       if (logInViewModel.isLogInSuccess){
           navigator.navigate(MainScreenDestination){
               popUpTo(MainScreenDestination){inclusive=true}
           }
           // Reset the login success state in the ViewModel if needed to prevent repeated navigation
           logInViewModel.isLogInSuccess = false
       }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        MyCircularProgressBar(isLoading = logInViewModel.isLoading.value)
        
        Column(
            verticalArrangement = Arrangement.SpaceBetween,

            ) {

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                MyVerticalSpacer(height = 70)
                Image(
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp),
                    //.padding(8.dp),
                    painter = painterResource(id = (if (isSystemInDarkTheme())R.drawable.chicken_white else R.drawable.chicken)),
                    contentDescription = "chicken",
                    contentScale = ContentScale.Fit
                )

                NormText(text = "SMART POULTRY")
                MyVerticalSpacer(height = 50)

                Column {
                   /*
                    UserTypeDropDownMenu(
                        onItemClick = {},
                    )*/
                    
                    MyEditTextClear( //Input email address
                        label = "Email",
                        keyboardType = KeyboardType.Email,
                        iconLeading = Icons.Default.Email,
                        iconLeadingDescription = "Email",
                        hint = "Enter Email",
                        onValueChange = {newText->
                            logInViewModel.email.value = newText
                        }
                    )
                    MyPasswordEditText( //input Password
                        label = "Password",
                        hint = "Enter Password",
                        iconLeading = Icons.Default.Lock,
                        iconLeadingDescription = "Password",
                        keyboardType = KeyboardType.Password,
                        onValueChange = { newText->
                            logInViewModel.password.value = newText
                        }
                    )


                    MyTextButton(
                        onButtonClick = { logInViewModel.onPasswordReset() },
                        btnText = "Forgotten Password?",
                        modifier = Modifier
                    )
                }

                MyVerticalSpacer(height = 20)

                NormButton( // Log in Button
                    onButtonClick = {
                        logInViewModel.onLogIn()
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
