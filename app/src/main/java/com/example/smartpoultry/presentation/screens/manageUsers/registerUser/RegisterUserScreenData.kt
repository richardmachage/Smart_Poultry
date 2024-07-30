package com.example.smartpoultry.presentation.screens.manageUsers.registerUser

import com.example.smartpoultry.presentation.screens.signUp.models.Genders

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
)
