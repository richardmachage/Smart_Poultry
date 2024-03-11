package com.example.smartpoultry.domain.repository

interface FirebaseAuthRepository {
    suspend fun registerUser(email : String, password:String, role:String): Result<Boolean>
    fun logIn(email: String, password: String) : Boolean
    fun resetPassword(email: String) : Boolean
}