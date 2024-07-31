package com.example.smartpoultry.presentation.screens.manageUsers.registerUser

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.R
import com.example.smartpoultry.presentation.composables.MyOutlineButton
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.composables.NormButton
import com.example.smartpoultry.presentation.composables.text.TitleText
import com.example.smartpoultry.presentation.screens.manageUsers.registerUser.components.AccessLevelDetails
import com.example.smartpoultry.presentation.screens.manageUsers.registerUser.components.AccessLevelDetailsResponse
import com.example.smartpoultry.presentation.screens.manageUsers.registerUser.components.RegisterUserParts
import com.example.smartpoultry.presentation.screens.signUp.components.ContactDetails
import com.example.smartpoultry.presentation.screens.signUp.components.PersonalDetails
import com.example.smartpoultry.presentation.screens.signUp.models.ContactDetailsResponse
import com.example.smartpoultry.presentation.screens.signUp.models.PersonalDetailsResponse
import com.example.smartpoultry.presentation.ui.theme.SmartPoultryTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
//@Preview(showSystemUi = true)
@Composable
fun RegisterUserScreen(
    navigator: DestinationsNavigator

) {
    val registerUserViewModel = hiltViewModel<RegisterUserViewModel>()
    SmartPoultryTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.register_user_screen_title)) },
                    navigationIcon = {
                        IconButton(onClick = {
                            /*TODO*/
                             navigator.navigateUp()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "back"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                ) {

                    TitleText(
                        modifier = Modifier.padding(5.dp),
                        text = registerUserViewModel.registerUserScreenState.currentPart.title
                    )
                    //var currentComponent by remember { mutableStateOf("") }
                    AnimatedContent(
                        targetState = registerUserViewModel.registerUserScreenState.currentPart,
                        label = "registerNewUserAnime"
                    ) { targetState ->
                        when (targetState) {
                            RegisterUserParts.PERSONAL_DETAILS -> {
                                PersonalDetails(
                                    personalDetailsResponse = PersonalDetailsResponse(
                                        firstName = registerUserViewModel.registerUserScreenData.firstName,
                                        lastName = registerUserViewModel.registerUserScreenData.lastName,
                                        gender = registerUserViewModel.registerUserScreenData.gender
                                    ),
                                    onResponse = {

                                    })
                            }

                            RegisterUserParts.CONTACT_DETAILS -> {
                                ContactDetails(
                                    contactDetailResponse = ContactDetailsResponse(
                                        email = registerUserViewModel.registerUserScreenData.email,
                                        phone = registerUserViewModel.registerUserScreenData.phone
                                    ),
                                    onResponse = {

                                    }
                                )
                            }

                            RegisterUserParts.ACCESS_LEVEL_DETAILS -> {
                                AccessLevelDetails(
                                    accessLevelDetailsResponse = AccessLevelDetailsResponse(
                                        manageUsers = registerUserViewModel.registerUserScreenData.manageUsers,
                                        editHenCount = registerUserViewModel.registerUserScreenData.editHenCountAccess,
                                        eggCollection = registerUserViewModel.registerUserScreenData.eggCollectionAccess,
                                        manageBlocksCells = registerUserViewModel.registerUserScreenData.manageBlockCells,
                                    ),
                                    onResponse = {

                                    }
                                )
                            }
                        }
                    }

                    MyVerticalSpacer(height = 10)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp)
                            .animateContentSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            AnimatedVisibility(
                                modifier = Modifier.weight(1f),
                                visible = registerUserViewModel.registerUserScreenState.showPrevious) {
                                MyOutlineButton(
                                    onButtonClick = { registerUserViewModel.onPrevious() },
                                    btnName = stringResource(id = R.string.previous_btn),
                                    leadingIcon = Icons.AutoMirrored.Default.ArrowBack
                                )
                            }

                            NormButton(
                                modifier = Modifier.weight(1f),
                                onButtonClick = {
                                    if (registerUserViewModel.registerUserScreenState.showContinue) {
                                        registerUserViewModel.onContinue()
                                    } else {
                                        registerUserViewModel.onDone()
                                    }
                                },
                                btnName = stringResource(
                                    id =
                                    if (registerUserViewModel.registerUserScreenState.showContinue) R.string.continue_btn else R.string.done_btn
                                ),
                                trailingIcon = if (registerUserViewModel.registerUserScreenState.showContinue) Icons.AutoMirrored.Default.ArrowForward else null
                            )
                        }
                    }
                }
            }
        }
    }
}

