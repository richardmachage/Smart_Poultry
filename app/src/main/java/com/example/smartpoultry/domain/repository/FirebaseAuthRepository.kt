package com.example.smartpoultry.domain.repository

interface FirebaseAuthRepository {
    suspend fun signUp(email: String, password: String, role: String, farmName:String): Result<Boolean>
    suspend fun registerUser(email : String, password:String, role:String): Result<Boolean>
    //suspend fun registerUser(email : String, password:String, role:String, farmId:String): Result<Boolean>
    suspend fun logIn(email: String, password: String) : Result<Boolean>
    suspend fun resetPassword(email: String) : Result<Boolean>
    suspend fun editUserName(name : String): Result<Boolean>
    suspend fun editEmail(email: String):Result<Boolean>
    suspend fun editUserRole(email: String, role: String): Result<Boolean>
    suspend fun editPhone (phone:String) : Result<Boolean>
    fun logOut()
}