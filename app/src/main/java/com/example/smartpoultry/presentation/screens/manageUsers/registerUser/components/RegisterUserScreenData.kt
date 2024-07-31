package com.example.smartpoultry.presentation.screens.manageUsers.registerUser.components

import com.example.smartpoultry.utils.Countries

data class RegisterUserScreenData(
    var firstName:String = "",
    var lastName: String = "",
    var phone : String ="",
    var email : String ="",
    var gender : String = "Select gender",//Genders.NONE.type,
    var country : Countries?,
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


}
