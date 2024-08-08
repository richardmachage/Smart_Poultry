package com.forsythe.smartpoultry.presentation.screens.signUp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.forsythe.smartpoultry.presentation.composables.dropDownMenus.DropDownMenu
import com.forsythe.smartpoultry.presentation.composables.spacers.MyVerticalSpacer
import com.forsythe.smartpoultry.presentation.composables.textInputFields.MyEditTextClear
import com.forsythe.smartpoultry.presentation.screens.signUp.models.Genders
import com.forsythe.smartpoultry.presentation.screens.signUp.models.PersonalDetailsResponse
import com.forsythe.smartpoultry.presentation.ui.theme.SmartPoultryTheme
import com.ramcosta.composedestinations.annotation.Destination

@Composable
fun PersonalDetails(
    personalDetailsResponse: PersonalDetailsResponse ,//= PersonalDetailsResponse("","","Select Gender"),
    onResponse : (PersonalDetailsResponse) -> Unit = {}
) {
    var personalDetails by remember {
        mutableStateOf(personalDetailsResponse)
    }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        //name, gender
        MyEditTextClear( // Input first name
            value = personalDetails.firstName,
            label = "First name",
            hint = "John",
            iconLeading = Icons.Default.AccountCircle,
            iconLeadingDescription = "account",
            keyboardType = KeyboardType.Text,
            onValueChange = { text ->
                personalDetails = personalDetails.copy(firstName = text.trim())
                onResponse(personalDetails)
            },
            onClear = {
                personalDetails = personalDetails.copy(firstName = "")
                onResponse(personalDetails)
            }

        )

        MyVerticalSpacer(height = 5)
        MyEditTextClear( // Input last name
            value = personalDetails.lastName,
            label = "Last name",
            hint = "doe",
            iconLeading = Icons.Default.AccountCircle,
            iconLeadingDescription = "account",
            keyboardType = KeyboardType.Text,
            onValueChange = { text ->
                personalDetails = personalDetails.copy(lastName = text.trim())
                onResponse(personalDetails)
            },
            onClear = {
                personalDetails = personalDetails.copy(lastName = "")
                onResponse(personalDetails)
            }
        )
        MyVerticalSpacer(height = 5)

        DropDownMenu(
            items = listOf(Genders.MALE.type, Genders.FEMALE.type, Genders.NONE.type),
            onItemClick = {
                personalDetails = personalDetails.copy(gender = it)
                onResponse(personalDetails)
            },
            defaultValue = personalDetails.gender
        )
    }
}

@Preview//(showBackground = true, showSystemUi = true)
@Composable
@Destination
fun PersonalPreview(
) {
    //SignUpScreen()
    SmartPoultryTheme {
        //PersonalDetails()
    }
}