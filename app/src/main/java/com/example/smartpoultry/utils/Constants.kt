package com.example.smartpoultry.utils

import com.example.smartpoultry.data.dataSource.datastore.PreferencesRepo
import com.example.smartpoultry.data.dataSource.remote.firebase.models.User


var THIS_USER : User? = null
fun getThisUser(preferencesRepo: PreferencesRepo) : User{
    THIS_USER?.let { return it }
    //collect user data from shared preferences and build the user class
    val userName = preferencesRepo.loadData(USER_NAME_KEY)!!
    val userEmail = preferencesRepo.loadData(USER_EMAIL_KEY)!!
    val userPhone = preferencesRepo.loadData(USER_PHONE_KEY)!!
    val userRole = preferencesRepo.loadData(USER_ROLE_KEY)!!
    val userFarmId = preferencesRepo.loadData(FARM_ID_KEY)!!
    val isPassWordReset = preferencesRepo.loadData(IS_PASSWORD_RESET_KEY)!!

    THIS_USER = User(
        //userId = firebaseAuth.currentUser?.uid.toString(),
        name = userName,
        email = userEmail,
        role = userRole,
        phone = userPhone,
        farmId = userFarmId,
        passwordReset = isPassWordReset == "true"
    )
    return THIS_USER!!
}

const val USER_ROLE_KEY = "user_role"
const val USER_NAME_KEY = "user_name"
const val USER_PHONE_KEY = "user_phone"
const val USER_EMAIL_KEY = "user_email"
const val FIRST_INSTALL = "first_install"
const val FARM_ID_KEY = "farm_id"
const val FARM_NAME_KEY = "farm_name"
const val IS_PASSWORD_RESET_KEY = "is_password_reset"
const val PAST_DAYS_KEY = "past_days"
const val CONSUCUTIVE_DAYS_KEY = "consucutive_days"
const val THRESHOLD_RATIO_KEY = "threshold_ratio"
const val REPEAT_INTERVAL_KEY = "repeat_interval"
const val IS_AUTOMATED_ANALYSIS_KEY = "is_automated_analysis"