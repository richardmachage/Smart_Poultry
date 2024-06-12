package com.example.smartpoultry.activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.datastore.FARM_ID_KEY
import com.example.smartpoultry.data.dataSource.datastore.FIRST_INSTALL
import com.example.smartpoultry.data.dataSource.datastore.PreferencesRepo
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val dataStore: AppDataStore,
    preferencesRepo: PreferencesRepo
) : ViewModel() {
    var isLoggedIn by mutableStateOf(false)
    var isFirstInstall by mutableStateOf(false)

    init {
        checkFirstInstall()
        checkIfLoggedIn()
        checkIfFarmSaved()
    }


    private fun checkIfLoggedIn(){
        firebaseAuth.currentUser?.let {
            isLoggedIn = true
            
        }
    }

    private fun checkFirstInstall(){
        viewModelScope.launch{
             dataStore.readData(FIRST_INSTALL).collect { if (it != "onBoardingDone") isFirstInstall = true }
        }
    }

    private fun checkIfFarmSaved(){
        viewModelScope.launch {
            dataStore.readData(FARM_ID_KEY).collect{
               // Log.d("Farm","Farm id from datastore :$it")
            }
        }
    }

}