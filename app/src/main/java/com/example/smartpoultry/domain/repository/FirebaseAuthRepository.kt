package com.example.smartpoultry.domain.repository

interface FirebaseAuthRepository {
    fun registerUser(email : String, password:String, role:String): Boolean
    fun logIn(email: String, password: String) : Boolean
}