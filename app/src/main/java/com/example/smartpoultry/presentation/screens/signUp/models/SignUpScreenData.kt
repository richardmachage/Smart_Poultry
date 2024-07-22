package com.example.smartpoultry.presentation.screens.signUp.models

data class SignUpScreenData(
    var firstName: String = "",
    var lastName: String = "",
    var gender: String = Genders.NONE.type,
    var phone: String = "",
    var email: String = "",
    var farmName: String = "",
    var country: String = "",
    var password: String = ""
) {
    fun checkBlanks(): Boolean {
        return (
                firstName.isNotBlank() ||
                lastName.isNotBlank() ||
                gender.isNotBlank() ||
                phone.isNotBlank() ||
                email.isNotBlank() ||
                farmName.isNotBlank() ||
                country.isNotBlank() ||
                password.isNotBlank())
    }
}
