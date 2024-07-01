package com.example.smartpoultry.data.dataSource.remote.firebase.models

data class User(
    val userId : String = "",
    val name : String = "",
    val phone : String = "",
    val email : String = "",
    //val role : String = "",
    var farmId:String = "",
    var passwordReset: Boolean = false
)
