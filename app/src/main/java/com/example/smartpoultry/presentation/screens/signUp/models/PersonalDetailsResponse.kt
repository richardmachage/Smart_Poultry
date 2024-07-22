package com.example.smartpoultry.presentation.screens.signUp.models

data class PersonalDetailsResponse (
    var firstName:String,
    var lastName : String
)

fun PersonalDetailsResponse.isValid() : Boolean{
    return firstName.isNotBlank() || lastName.isNotBlank()
}