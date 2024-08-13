package com.forsythe.smartpoultry.presentation.screens.logIn

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.forsythe.smartpoultry.R
import com.forsythe.smartpoultry.presentation.composables.buttons.MyOutlineButton
import com.forsythe.smartpoultry.presentation.composables.buttons.MyTextButton
import com.forsythe.smartpoultry.presentation.composables.buttons.NormButton
import com.forsythe.smartpoultry.presentation.composables.dialogs.MyInputDialog
import com.forsythe.smartpoultry.presentation.composables.progressBars.MyCircularProgressBar
import com.forsythe.smartpoultry.presentation.composables.spacers.MyVerticalSpacer
import com.forsythe.smartpoultry.presentation.composables.text.NormText
import com.forsythe.smartpoultry.presentation.composables.textInputFields.MyEditTextClear
import com.forsythe.smartpoultry.presentation.composables.textInputFields.MyPasswordEditText
import com.forsythe.smartpoultry.presentation.destinations.MainScreenDestination
import com.forsythe.smartpoultry.presentation.destinations.SignUpScreenDestination
import com.forsythe.smartpoultry.utils.isValidEmail
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo

@Destination(start = true)
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
           /*navigator.navigate(MainScreenDestination){
               popUpTo(LogInScreenDestination){inclusive=true}
           }*/
           navigator.navigate(MainScreenDestination) {
               popUpTo(MainScreenDestination) { inclusive = true }
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
        MyCircularProgressBar(
            isLoading = logInViewModel.isLoading.value,
            displayText = logInViewModel.isLoadingDisplayText.value
        )
        
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


                    var showPasswordResetDialog by remember { mutableStateOf(false) }
                    MyInputDialog(
                        showDialog = showPasswordResetDialog,
                        title = "Reset Password" ,
                        onConfirm = {
                            if (logInViewModel.email.value.isNotBlank() && isValidEmail(logInViewModel.email.value)) {
                                logInViewModel.onPasswordReset()
                                showPasswordResetDialog = false
                            }else{
                                logInViewModel.validateError.value = "Invalid email address"
                            }

                        },
                        onDismiss = {
                            showPasswordResetDialog = false
                        }
                    ) {
                        //Dialog body content
                        Column {
                            Text(text = "A password reset link will be sent to the email address you enter below if it is registered")

                            MyVerticalSpacer(height = 10)

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
                            MyVerticalSpacer(height = 10)
                            Text(text = "After clicking okay, follow these steps \n" +
                                    "1.Go to your email inbox \n" +
                                    "2.Click on link sent to reset your password \n" +
                                    "3.Now open Smart Poultry and log in using your new password")
                        }
                    }
                    MyTextButton(
                        onButtonClick = { showPasswordResetDialog = true },
                        btnText = "Forgot Password?",
                        modifier = Modifier
                    )
                }

                MyVerticalSpacer(height = 20)

                NormButton( // Log in Button
                    onButtonClick = {
                        logInViewModel.onLogIn()
                    },
                    btnName = "Log In",
                    modifier = Modifier.fillMaxWidth(),
                    enabled = logInViewModel.email.value.isNotBlank() && logInViewModel.password.value.isNotBlank()
                )

                MyVerticalSpacer(height = 20)

                /*MyTextButton( //Not registered button
                    onButtonClick = { navigator.navigate(SignUpScreenDestination) },
                    btnText = "You don't have an account?\nClick to sign up",
                    modifier = Modifier.fillMaxWidth()
                )*/
                Text(
                    text = "You don't have an account?",
                    textAlign = TextAlign.Left,
                    modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
                    fontStyle = FontStyle.Italic

                )
                MyOutlineButton(
                    modifier = Modifier.fillMaxWidth(),
                    onButtonClick = {navigator.navigate(SignUpScreenDestination)},
                    btnName = "Sign Up"
                )

            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLogInScreen() {
  //  LogInScreen(DestinationsNavigator)
}
