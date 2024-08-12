package com.forsythe.smartpoultry.domain.repository

import com.forsythe.smartpoultry.data.dataSource.remote.firebase.models.AccessLevel
import com.forsythe.smartpoultry.data.dataSource.remote.firebase.models.User

interface FirebaseAuthRepository {
    suspend fun signUp(email: String, password: String, role: String, farmName:String, firstName:String,lastName:String, phone:String, country:String,gender : String): Result<Boolean>
    //suspend fun registerUser(email : String, password:String, role:String): Result<Boolean>
    suspend fun registerUser(firstName: String, lastName: String,gender: String,email : String, password:String, farmId:String, phone: String, accessLevel: AccessLevel): Result<Boolean>
    suspend fun logIn(email: String, password: String) : Result<Boolean>
    suspend fun resetPassword(email: String) : Result<Boolean>
    suspend fun editFirstName(name : String): Result<Boolean>
    suspend fun editLastName(name : String): Result<Boolean>
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
    suspend fun editFarmName (farmName: String) : Result<Boolean>
}