package com.example.smartpoultry.presentation.screens.signUp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartpoultry.R
import com.example.smartpoultry.presentation.composables.spacers.MyVerticalSpacer
import com.example.smartpoultry.presentation.composables.textInputFields.MyEditTextClear
import com.example.smartpoultry.presentation.screens.signUp.models.ContactDetailsResponse
import com.example.smartpoultry.utils.Countries

@Preview(showSystemUi = true)
@Composable
fun ContactDetails(
    onResponse: (ContactDetailsResponse) -> Unit = {},
    contactDetailResponse : ContactDetailsResponse = ContactDetailsResponse("",""),
    country : Countries = Countries.KENYA
){

    var contactResponse by remember{ mutableStateOf(contactDetailResponse)}
    var isEmailValid by remember{ mutableStateOf(false)}
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
            },
            onClear = {
                contactResponse = contactResponse.copy(email = "")
                onResponse(contactResponse)

            }

        )

        MyVerticalSpacer(height = 5)

        var showSupportingText by remember{ mutableStateOf(false) }
        var hasError by remember{ mutableStateOf(false) }
        MyEditTextClear( // Input Phone number
            value = contactResponse.phone,
            label = "Phone",
            hint = "eg. 718672654",
            iconLeading = Icons.Default.Phone,
            iconLeadingDescription = "phone",
            keyboardType = KeyboardType.Phone,
            onValueChange = { text ->
                if (text.length < 9){
                    showSupportingText = true
                    hasError = false
                }
                if (text.length == 9){
                    showSupportingText = false
                    hasError = false
                }
                if (text.length > 9){
                    hasError = true
                    showSupportingText = false
                }
                contactResponse = contactResponse.copy(phone = text, hasError = text.length == 9)
                onResponse(contactResponse)
            },
            onClear = { contactResponse = contactResponse.copy(phone = "")
                onResponse(contactResponse)
            },
            prefix = {
                Text(
                    text = country.countryCode,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            hasError = hasError,
            supportingText = {
               if (hasError && contactResponse.phone.length > 9){
                   Text(
                       color = MaterialTheme.colorScheme.error,
                       text = stringResource(id =  R.string.invalid_phone_number))
               }
                if (showSupportingText){
                    Text(text = stringResource(id =  R.string.input_phone_number))
                }
            }
        )
    }
}