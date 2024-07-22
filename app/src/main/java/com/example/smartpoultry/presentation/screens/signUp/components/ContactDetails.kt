package com.example.smartpoultry.presentation.screens.signUp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.smartpoultry.presentation.composables.MyEditTextClear
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.screens.signUp.models.ContactDetailsResponse

@Composable
fun ContactDetails(
    onResponse: (ContactDetailsResponse) -> Unit = {},
    contactResponse : ContactDetailsResponse = ContactDetailsResponse("","")
){

    var contactResponse by remember{ mutableStateOf(contactResponse)}
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        MyEditTextClear(
            value = contactResponse.email,// Input Email address
            label = "Email",
            hint = "eg. smartpoultry@gmail.com",
            iconLeading = Icons.Default.Email,
            iconLeadingDescription = "Email",
            keyboardType = KeyboardType.Email,
            onValueChange = { text ->
                contactResponse = contactResponse.copy(email = text)
                onResponse(contactResponse)
            }
        )

        MyVerticalSpacer(height = 5)

        MyEditTextClear( // Input Phone number
            value = contactResponse.phone,
            label = "Phone",
            hint = "eg. 0718672654",
            iconLeading = Icons.Default.Phone,
            iconLeadingDescription = "phone",
            keyboardType = KeyboardType.Phone,
            onValueChange = { text ->
                contactResponse = contactResponse.copy(phone = text)
                onResponse(contactResponse)
            }
        )
    }
}