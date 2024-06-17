package com.example.smartpoultry.activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.datastore.PreferencesRepo
import com.example.smartpoultry.utils.FIRST_INSTALL
import com.example.smartpoultry.utils.getThisUser
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val dataStore: AppDataStore,
    val preferencesRepo: PreferencesRepo
) : ViewModel() {
    var isLoggedIn by mutableStateOf(false)
    var isFirstInstall by mutableStateOf(false)

    init {
        checkFirstInstall()
        checkIfLoggedIn()
        getThisUser(preferencesRepo)
        //checkIfFarmSaved()
    }


    private fun checkIfLoggedIn() {
        firebaseAuth.currentUser?.let {
            isLoggedIn = true
        }
    }

    private fun checkFirstInstall() {
        /*viewModelScope.launch {
            dataStore.readData(FIRST_INSTALL)
                .collect { if (it != "onBoardingDone") isFirstInstall = true }
        }*/
        val firstInstall = preferencesRepo.loadData(FIRST_INSTALL)!!
        if (firstInstall != "onBoardingDone") isFirstInstall = true


    }

    /*private fun checkIfFarmSaved() {
        viewModelScope.launch {
            dataStore.readData(FARM_ID_KEY).collect {
                // Log.d("Farm","Farm id from datastore :$it")
            }
        }
    }*/

}