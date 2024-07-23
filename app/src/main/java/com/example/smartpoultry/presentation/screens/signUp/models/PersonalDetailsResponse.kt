package com.example.smartpoultry.presentation.screens.signUp.models

data class PersonalDetailsResponse (
    var firstName:String,
    var lastName : String,
    var gender : String ,//= Genders.NONE.type
)

fun PersonalDetailsResponse.isNoEmptyField() : Boolean{
    return firstName.isNotBlank() || lastName.isNotBlank()
}