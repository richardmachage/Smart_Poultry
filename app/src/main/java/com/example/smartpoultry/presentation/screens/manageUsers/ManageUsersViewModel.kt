package com.example.smartpoultry.presentation.screens.manageUsers

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.smartpoultry.data.dataSource.datastore.PreferencesRepo
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ManageUsersViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val preferencesRepo: PreferencesRepo
): ViewModel(){

    /*objectives here
    *  1. delete user
    *  2. change permissions of a given user
    *  3. see all users registered
    *  4. see the users currently singed in
    *
    * */

    val farmId = mutableStateOf("")

    init {
        farmId =
    }
}