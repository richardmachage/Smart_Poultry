package com.example.smartpoultry.presentation.screens.manageUsers.registerUser.components

import com.example.smartpoultry.presentation.screens.signUp.models.Genders
import com.example.smartpoultry.utils.isValidEmail
import com.example.smartpoultry.utils.isValidPhone

data class RegisterUserScreenData(
    var firstName:String = "",
    var lastName: String = "",
    var phone : String ="",
    var email : String ="",
    var gender : String = Genders.NONE.type,
    var eggCollectionAccess : Boolean = false,
    var editHenCountAccess : Boolean = false,
    var manageBlockCells : Boolean = false,
    var manageUsers : Boolean = false,
){
    fun personalDetailsNotBlank() : Boolean{
     return firstName.isNotBlank() && lastName.isNotBlank() && gender.isNotBlank()
    }

    fun contactDetailsNotBlank():Boolean{
        return phone.isNotBlank() && email.isNotBlank()
    }

    fun checkIfValidEmail():Boolean{
            return contactDetailsNotBlank() && isValidEmail(this.email)
    }

    fun checkIfValidPhone() : Boolean{
        return  isValidPhone(phone)
    }
}
