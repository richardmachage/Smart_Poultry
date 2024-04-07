package com.example.smartpoultry.domain.repository

interface FirebaseAuthRepository {
    suspend fun registerUser(email : String, password:String, role:String): Result<Boolean>
    suspend fun logIn(email: String, password: String) : Result<Boolean>
    suspend fun resetPassword(email: String) : Result<Boolean>
    suspend fun editUserName(name : String): Result<Boolean>
    fun logOut()
}