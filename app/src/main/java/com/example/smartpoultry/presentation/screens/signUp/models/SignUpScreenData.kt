package com.example.smartpoultry.presentation.screens.signUp.models

data class SignUpScreenData(
    var firstName : String = "",
    var lastName : String = "",
    var gender : String = Genders.NONE.type,
    var phone : String = "",
    var email: String = ""
)
