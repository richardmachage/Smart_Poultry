package com.example.smartpoultry.presentation.screens.signUp

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.R
import com.example.smartpoultry.presentation.composables.buttons.MyOutlineButton
import com.example.smartpoultry.presentation.composables.buttons.NormButton
import com.example.smartpoultry.presentation.composables.progressBars.MyCircularProgressBar
import com.example.smartpoultry.presentation.composables.spacers.MyVerticalSpacer
import com.example.smartpoultry.presentation.composables.text.NormText
import com.example.smartpoultry.presentation.composables.text.TitleText
import com.example.smartpoultry.presentation.screens.signUp.components.ContactDetails
import com.example.smartpoultry.presentation.screens.signUp.components.FarmDetails
import com.example.smartpoultry.presentation.screens.signUp.components.PersonalDetails
import com.example.smartpoultry.presentation.screens.signUp.components.SetPassword
import com.example.smartpoultry.presentation.screens.signUp.models.ContactDetailsResponse
import com.example.smartpoultry.presentation.screens.signUp.models.FarmDetailsResponse
import com.example.smartpoultry.presentation.screens.signUp.models.PersonalDetailsResponse
import com.example.smartpoultry.presentation.screens.signUp.models.SignUpParts
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SignUpScreen(
    navigator: DestinationsNavigator
) {
    val singUpViewModel = hiltViewModel<SignUpViewModel>()
    val context = LocalContext.current

    val isLoading = singUpViewModel.isLoading.value


    //For All Toasts in this screen
    LaunchedEffect(singUpViewModel.toastMessage.value) {
        if (singUpViewModel.toastMessage.value.isNotBlank()) {
            Toast.makeText(context, singUpViewModel.toastMessage.value, Toast.LENGTH_SHORT).show()
            singUpViewModel.toastMessage.value = ""
        }
    }

    //validation error toast
    LaunchedEffect(key1 = singUpViewModel.validationError.value) {
        singUpViewModel.validationError.value.let { toastMessage ->
            if (toastMessage.isNotBlank()) {
                Toast.makeText(context, singUpViewModel.validationError.value, Toast.LENGTH_LONG)
                    .show()
                singUpViewModel.validationError.value = ""
            }
        }
    }

    LaunchedEffect(key1 = singUpViewModel.isCreateAccountSuccess) {
        if (singUpViewModel.isCreateAccountSuccess) {
            navigator.navigateUp()
            singUpViewModel.isCreateAccountSuccess = false
        }
    }

    Scaffold(
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MyCircularProgressBar(
                isLoading = isLoading,
                displayText = stringResource(id = R.string.signing_in_loading_text)//"Signing In..."
            )


            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.padding(top = 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp),
                        //.padding(8.dp),
                        painter = painterResource(id = (if (isSystemInDarkTheme()) R.drawable.chicken_white else R.drawable.chicken)),
                        contentDescription = "chicken",
                        contentScale = ContentScale.Fit
                    )
                    NormText(text = stringResource(id = R.string.app_name_caps))//"SMART POULTRY")

                    Column(
                        modifier = Modifier.padding(6.dp),
                    ) {
                        MyVerticalSpacer(height = 20)
                        //Header
                        TitleText(
                            modifier = Modifier.fillMaxWidth().padding(10.dp),
                            text = singUpViewModel.signUpScreenState.currentPart.title
                        )
                       /* Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            text = singUpViewModel.signUpScreenState.currentPart.title,//"Hello, lets start with your name",
                            style = MaterialTheme.typography.headlineMedium,
                            fontStyle = FontStyle.Italic
                        )*/

                        //Part
                        AnimatedContent(
                            targetState = singUpViewModel.signUpScreenState.currentPart,
                            label = "signUpContentAnime"
                        ) { currentPart ->
                            when (currentPart) {
                                SignUpParts.PERSONAL_DETAILS -> {
                                    PersonalDetails(
                                        personalDetailsResponse = PersonalDetailsResponse(
                                            firstName = singUpViewModel.signUpScreenData.firstName,
                                            lastName = singUpViewModel.signUpScreenData.lastName,
                                            gender = singUpViewModel.signUpScreenData.gender
                                        ),
                                        onResponse = {
                                            //update data
                                            singUpViewModel.onPersonalDetailsResponse(
                                                personalDetailsResponse = it
                                            )
                                        }
                                    )
                                }

                                SignUpParts.CONTACT_DETAILS -> {
                                    ContactDetails(
                                        contactDetailResponse = ContactDetailsResponse(
                                            phone = singUpViewModel.signUpScreenData.phone,
                                            email = singUpViewModel.signUpScreenData.email
                                        ),
                                        onResponse = {
                                            singUpViewModel.onContactDetailsResponse(
                                                contactDetailsResponse = it
                                            )
                                        }
                                    )
                                }

                                SignUpParts.FARM_DETAILS -> {
                                    FarmDetails(
                                        farmDetailsResponse = FarmDetailsResponse(
                                            farmName = singUpViewModel.signUpScreenData.farmName,
                                            country = singUpViewModel.signUpScreenData.country
                                        ),
                                        onResponse = {
                                            singUpViewModel.onFarmDetailsResponse(
                                                farmDetailsResponse = it
                                            )
                                        })
                                }

                                SignUpParts.SET_PASSWORD -> {
                                    SetPassword(
                                        onResponse = { password, hasError ->
                                            singUpViewModel.onSetPasswordResponse(
                                                password,
                                                hasError
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        Box(
                            modifier =
                            if (!singUpViewModel.signUpScreenState.showPrevious) Modifier
                                .fillMaxWidth()
                                .animateContentSize() else Modifier
                                .fillMaxSize()
                                .animateContentSize(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                AnimatedVisibility(visible = singUpViewModel.signUpScreenState.showPrevious) {
                                    MyOutlineButton(//Previous button
                                        modifier = Modifier,
                                        //.fillMaxWidth(),
                                        //.weight(1f),
                                        onButtonClick = { singUpViewModel.onPrevious() },
                                        btnName = stringResource(id = R.string.previous_btn),
                                        leadingIcon = Icons.AutoMirrored.Default.ArrowBack
                                    )
                                }

                                NormButton(

                                    modifier = if (!singUpViewModel.signUpScreenState.showPrevious) Modifier.weight(
                                        1f
                                    ) else Modifier,
                                    onButtonClick = {
                                        if (singUpViewModel.signUpScreenState.showContinue) {
                                            singUpViewModel.onContinue()
                                        } else {
                                            singUpViewModel.onDone()
                                        }
                                    },
                                    btnName = if (singUpViewModel.signUpScreenState.showContinue) stringResource(
                                        id = R.string.continue_btn
                                    ) else stringResource(id = R.string.done_btn),
                                    enabled = singUpViewModel.signUpScreenState.continueEnabled,
                                    trailingIcon = if (singUpViewModel.signUpScreenState.showContinue) Icons.AutoMirrored.Default.ArrowForward else null

                                )
                            }
                        }

                        AnimatedVisibility(visible = !singUpViewModel.signUpScreenState.showPrevious) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Column {
                                    Text(
                                        text = stringResource(id = R.string.already_registered_sign_up),//"Already registered?",
                                        textAlign = TextAlign.Left,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 10.dp),
                                        fontStyle = FontStyle.Italic

                                    )
                                    MyOutlineButton(
                                        modifier = Modifier.fillMaxWidth(),
                                        onButtonClick = { navigator.navigateUp() },
                                        btnName = stringResource(id = R.string.log_in_btn)
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}


@Composable
fun MyText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = text,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
@Destination
fun SignUpPreview(
) {
    //SignUpScreen()
    // SignUpContent()
}