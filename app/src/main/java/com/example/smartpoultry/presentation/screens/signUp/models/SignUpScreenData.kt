package com.example.smartpoultry.presentation.screens.signUp.models

import com.example.smartpoultry.utils.Countries

data class SignUpScreenData(
    var firstName: String = "",
    var lastName: String = "",
    var gender: String = "Select Gender",//Genders.NONE.type,
    var phone: String = "",
    var email: String = "",
    var farmName: String = "",
    var country: Countries? = null,//String = "",
    var password: String = ""
) {
    fun checkBlanks(): Boolean {
        return (
                firstName.isNotBlank() ||
                lastName.isNotBlank() ||
                gender != "Select Gender"||
                phone.isNotBlank() ||
                email.isNotBlank() ||
                farmName.isNotBlank() ||
                country != null ||
                password.isNotBlank())
    }
}
