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
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartpoultry.presentation.composables.MyEditTextClear
import com.example.smartpoultry.presentation.composables.MyVerticalSpacer
import com.example.smartpoultry.presentation.screens.signUp.models.ContactDetailsResponse

@Preview
@Composable
fun ContactDetails(
    onResponse: (ContactDetailsResponse) -> Unit = {}
){

    var contactResponse by remember{ mutableStateOf(ContactDetailsResponse("",""))}
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        MyEditTextClear( // Input Email address
            label = "Email",
            hint = "eg. john",
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