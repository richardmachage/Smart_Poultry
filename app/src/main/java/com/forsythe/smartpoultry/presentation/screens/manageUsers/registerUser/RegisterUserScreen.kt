package com.forsythe.smartpoultry.presentation.screens.manageUsers.registerUser

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.forsythe.smartpoultry.R
import com.forsythe.smartpoultry.presentation.composables.buttons.MyOutlineButton
import com.forsythe.smartpoultry.presentation.composables.buttons.NormButton
import com.forsythe.smartpoultry.presentation.composables.progressBars.MyCircularProgressBar
import com.forsythe.smartpoultry.presentation.composables.spacers.MyVerticalSpacer
import com.forsythe.smartpoultry.presentation.composables.text.TitleText
import com.forsythe.smartpoultry.presentation.destinations.ManageUsersScreenDestination
import com.forsythe.smartpoultry.presentation.destinations.RegisterUserScreenDestination
import com.forsythe.smartpoultry.presentation.screens.manageUsers.registerUser.components.AccessLevelDetails
import com.forsythe.smartpoultry.presentation.screens.manageUsers.registerUser.components.AccessLevelDetailsResponse
import com.forsythe.smartpoultry.presentation.screens.manageUsers.registerUser.components.RegisterUserParts
import com.forsythe.smartpoultry.presentation.screens.signUp.components.ContactDetails
import com.forsythe.smartpoultry.presentation.screens.signUp.components.PersonalDetails
import com.forsythe.smartpoultry.presentation.screens.signUp.models.ContactDetailsResponse
import com.forsythe.smartpoultry.presentation.screens.signUp.models.PersonalDetailsResponse
import com.forsythe.smartpoultry.presentation.ui.theme.SmartPoultryTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo

@OptIn(ExperimentalMaterial3Api::class)
@Destination
//@Preview(showSystemUi = true)
@Composable
fun RegisterUserScreen(
    navigator: DestinationsNavigator

) {
    val registerUserViewModel = hiltViewModel<RegisterUserViewModel>()
    val context = LocalContext.current
    LaunchedEffect(key1 = registerUserViewModel.registerUserScreenState.toastMessage) {
        val message = registerUserViewModel.registerUserScreenState.toastMessage
        if (message.isNotBlank()){
            Toast.makeText(context, message,Toast.LENGTH_SHORT).show()
            registerUserViewModel.clearToastMessageValue()
        }

    }

    LaunchedEffect(key1 = registerUserViewModel.registerUserScreenState.navigateToManageUsers) {
        if (registerUserViewModel.registerUserScreenState.navigateToManageUsers){
            navigator.navigate(ManageUsersScreenDestination){
                popUpTo(RegisterUserScreenDestination){inclusive = true}
            }
           // navigator.navigateUp() //navigate(ManageUsersScreenDestination)
            registerUserViewModel.registerUserScreenState.navigateToManageUsers = false
        }
    }
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
            },

        ) { paddingValues ->

            MyCircularProgressBar(
                isLoading = registerUserViewModel.registerUserScreenState.isLoading,
                displayText = stringResource(id = R.string.registering)
            )

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
                                    onResponse = {personalDetails->
                                        registerUserViewModel.onPersonalDetailsResponse(personalDetails)
                                    })
                            }

                            RegisterUserParts.CONTACT_DETAILS -> {
                                ContactDetails(
                                    contactDetailResponse = ContactDetailsResponse(
                                        email = registerUserViewModel.registerUserScreenData.email,
                                        phone = registerUserViewModel.registerUserScreenData.phone
                                    ),
                                    onResponse = {contactDetails->
                                        registerUserViewModel.onContactDetailsResponse(contactDetails)
                                    },
                                    country = registerUserViewModel.getCountry()
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
                                    onResponse = {accessLevelDetails->
                                        registerUserViewModel.onAccessLevelResponse(accessLevelDetails)
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
                            .imePadding()
                        ,
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            AnimatedVisibility(
                                visible = registerUserViewModel.registerUserScreenState.showPrevious,
                            ) {
                                MyOutlineButton(

                                    onButtonClick = { registerUserViewModel.onPrevious() },
                                    btnName = stringResource(id = R.string.previous_btn),
                                    leadingIcon = Icons.AutoMirrored.Default.ArrowBack
                                )
                            }

                            NormButton(
                                modifier = Modifier.weight(0.8f),
                                onButtonClick = {
                                    if (registerUserViewModel.registerUserScreenState.showContinue) {
                                        registerUserViewModel.onContinue()
                                    } else {
                                        registerUserViewModel.onDone()
                                    }
                                },
                                btnName = stringResource(
                                    id =
                                    if (registerUserViewModel.registerUserScreenState.showContinue) R.string.continue_btn else R.string.done_btn,
                                ),
                                trailingIcon = if (registerUserViewModel.registerUserScreenState.showContinue) Icons.AutoMirrored.Default.ArrowForward else null,
                                enabled = registerUserViewModel.registerUserScreenState.isContinueEnabled
                            )
                        }
                    }
                }
            }
        }
    }
}

