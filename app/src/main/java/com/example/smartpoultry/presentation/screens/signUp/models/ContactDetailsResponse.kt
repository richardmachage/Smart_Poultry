package com.example.smartpoultry.presentation.screens.signUp.models

import com.example.smartpoultry.utils.isValidEmail
import com.example.smartpoultry.utils.isValidPhone

data class ContactDetailsResponse(
    var phone : String,
    var email : String,
    var hasError : Boolean = false
){
    fun isNoEmptyField() : Boolean{
        return email.isNotBlank() && phone.isNotBlank()
    }
    fun checkIfValidEmail():Boolean{
        return isNoEmptyField() && isValidEmail(this.email)
    }

    fun checkIfValidPhone() : Boolean{
        return  isNoEmptyField() && isValidPhone(this.phone)
    }
}


