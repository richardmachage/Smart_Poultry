package com.example.smartpoultry.presentation.screens.signUp.models

data class ContactDetailsResponse(
    var phone : String,
    var email : String
)

fun ContactDetailsResponse.isValidContactDetails() : Boolean{
    return email.isNotBlank() || phone.isNotBlank()
}