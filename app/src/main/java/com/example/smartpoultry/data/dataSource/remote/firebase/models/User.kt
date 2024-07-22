package com.example.smartpoultry.data.dataSource.remote.firebase.models

import com.example.smartpoultry.presentation.screens.signUp.models.Genders

data class User(
    val userId : String = "",
    val firstName : String = "",
    val lastName : String = "",
    val gender: String = Genders.NONE.type,
    val phone : String = "",
    val email : String = "",
    //val role : String = "",
    var farmId:String = "",
    var passwordReset: Boolean = false,
)
