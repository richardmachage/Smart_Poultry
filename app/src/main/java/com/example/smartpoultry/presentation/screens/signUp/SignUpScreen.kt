package com.example.smartpoultry.presentation.screens.signUp

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smartpoultry.R
import com.example.smartpoultry.presentation.composables.MyCircularProgressBar
import com.example.smartpoultry.presentation.composables.MyEditTextClear
import com.example.smartpoultry.presentation.composables.MyOutlineButton
import com.example.smartpoultry.presentation.composables.MyPasswordEditText
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.composables.NormButton
import com.example.smartpoultry.presentation.screens.signUp.components.ContactDetails
import com.example.smartpoultry.presentation.screens.signUp.components.FarmDetails
import com.example.smartpoultry.presentation.screens.signUp.components.PersonalDetails
import com.example.smartpoultry.presentation.screens.signUp.components.SetPassword
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
        if (singUpViewModel.toastMessage.value.isNotBlank()){
            Toast.makeText(context,singUpViewModel.toastMessage.value, Toast.LENGTH_SHORT).show()
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
            MyCircularProgressBar(
                isLoading = isLoading,
                displayText = "Signing In..."
            )

           Column(
               modifier = Modifier.fillMaxSize(),
               verticalArrangement = Arrangement.SpaceBetween

           ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Card( //Personal details
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(16.dp)
                        ) {
                            MyText(text = "Name and Contact details")
                            MyEditTextClear( // Input user name
                                label = "Name",
                                hint = "eg. smartPoultry@gmail.com",
                                iconLeading = Icons.Default.AccountCircle,
                                iconLeadingDescription = "account",
                                keyboardType = KeyboardType.Text,
                                onValueChange = { text ->
                                    singUpViewModel.name.value = text.trim()
                                }
                            )
                            MyEditTextClear( // Input Email address
                                label = "Email",
                                hint = "eg. john",
                                iconLeading = Icons.Default.Email,
                                iconLeadingDescription = "Email",
                                keyboardType = KeyboardType.Email,
                                onValueChange = { text ->
                                    singUpViewModel.email.value = text.trim()
                                }
                            )

                            MyEditTextClear( // Input Phone number
                                label = "Phone",
                                hint = "eg. 0718672654",
                                iconLeading = Icons.Default.Phone,
                                iconLeadingDescription = "phone",
                                keyboardType = KeyboardType.Phone,
                                onValueChange = { text ->
                                    singUpViewModel.phone.value = text.trim()
                                }
                            )
                        }
                    }

                    MyVerticalSpacer(height = 10)
                    Card( //Farm details
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(16.dp)
                        ) {
                            MyText(text = "Your Poultry Farm")
                            MyEditTextClear(
                                label = "Farm Name",
                                hint = "eg. Abuya Poultry Farm",
                                iconLeading = ImageVector.vectorResource(id = R.drawable.egg_outline),// painterResource(id = R.drawable.egg_outline),//Image(imageVector =, contentDescription = ),
                                iconLeadingDescription = "place",
                                keyboardType = KeyboardType.Text,
                                onValueChange = {
                                    singUpViewModel.farmName.value = it
                                }
                            )
                        }
                    }

                    MyVerticalSpacer(height = 10)

                    Card( //Set Password card
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(16.dp)
                        ) {
                            MyText(text = "Set Password")
                            MyPasswordEditText( // Input new Password
                                label = "Password",
                                // hint = "New Password",
                                iconLeading = Icons.Default.Lock,
                                iconLeadingDescription = "Password",
                                keyboardType = KeyboardType.Password,
                                onValueChange = { text ->
                                    singUpViewModel.password.value = text
                                }
                            )

                            MyPasswordEditText( // Confirm Password
                                label = "Confirm Password",
                                //hint = "Confirm Password",
                                iconLeading = Icons.Default.Lock,
                                iconLeadingDescription = "Password",
                                keyboardType = KeyboardType.Password,
                                onValueChange = { text ->
                                    singUpViewModel.confirmPassword.value = text
                                }
                            )
                        }
                    }
                }

               Row (verticalAlignment = Alignment.CenterVertically){
                   Checkbox(checked = singUpViewModel.terms.value, onCheckedChange = {
                       singUpViewModel.terms.value = it} )
                   Text(text = "Accept Terms and conditions")

               }


                NormButton( //The sign up Button
                    onButtonClick = { singUpViewModel.onSignUp() },
                    btnName = "Sign Up",
                    modifier = Modifier.fillMaxWidth(),
                    enabled = singUpViewModel.terms.value
                )
               MyVerticalSpacer(height = 10)

            }
        }
    }
}

@Composable
fun SignUpContent(){
    val state by remember{ mutableStateOf(SignUpParts.SET_PASSWORD.name) }

    //Header
    //Part
    //continue previous button
    Column(
        modifier =  Modifier.padding(6.dp)
    ) {
        //Header
        Text(
            text = "Hello, lets start with your name",
            style = MaterialTheme.typography.headlineLarge,
            fontStyle = FontStyle.Italic
        )

        //Part
        AnimatedContent(targetState = state , label = "signUpContentAnime") {currentPart->
            when(currentPart){
                SignUpParts.PERSONAL_DETAILS.name -> {
                    PersonalDetails(
                        onResponse = {
                            //update state
                        }
                    )
                }
                SignUpParts.CONTACT_DETAILS.name -> {
                    ContactDetails()
                }
                SignUpParts.FARM_DETAILS.name -> {
                    FarmDetails()
                }
                SignUpParts.SET_PASSWORD.name -> {
                    SetPassword()
                }
            }
        }

        MyVerticalSpacer(height = 30)
        Row(
            Modifier.fillMaxWidth().padding(6.dp)
        ) {

            MyOutlineButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onButtonClick = { /*TODO*/ },
                btnName = "Previous"
            )
            NormButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onButtonClick = { /*TODO*/ },
                btnName = "Continue")
        }
    }
}

@Composable
fun MyText(
    text:String,
    modifier: Modifier = Modifier
){
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
    SignUpContent()
}