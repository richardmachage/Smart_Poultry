package com.example.smartpoultry.domain.repository

import com.example.smartpoultry.data.dataSource.remote.firebase.models.AccessLevel
import com.example.smartpoultry.data.dataSource.remote.firebase.models.User

interface FirebaseAuthRepository {
    suspend fun signUp(email: String, password: String, role: String, farmName:String, userName:String, phone:String): Result<Boolean>
    //suspend fun registerUser(email : String, password:String, role:String): Result<Boolean>
    suspend fun registerUser(email : String, password:String, farmId:String, name: String, phone: String, accessLevel: AccessLevel): Result<Boolean>
    suspend fun logIn(email: String, password: String) : Result<Boolean>
    suspend fun resetPassword(email: String) : Result<Boolean>
    suspend fun editUserName(name : String): Result<Boolean>
    suspend fun editEmail(email: String):Result<Boolean>
    suspend fun editUserRole(email: String, role: String): Result<Boolean>
    suspend fun editPhone (phone:String) : Result<Boolean>
    suspend fun updateIsPasswordChanged(): Result<Boolean>
//    suspend fun getFarm() : String
    suspend fun getFarmEmployees (farmId: String): Result<List<User>>
    suspend fun deleteUser(userId:String):Result<String>
    suspend fun getAccessLevel(userId: String) : Result<AccessLevel>
    fun logOut()
    suspend fun editAccessLevel(userId: String, accessLevel: AccessLevel):Result<Boolean>
}