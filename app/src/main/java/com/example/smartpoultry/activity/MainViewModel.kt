package com.example.smartpoultry.activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.datastore.FIRST_INSTALL
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val dataStore: AppDataStore
) : ViewModel() {
    var isLoggedIn by mutableStateOf(false)
    var isFirstInstall by mutableStateOf(false)

    init {
        checkIfLoggedIn()
    }
    private fun checkIfLoggedIn(){
        firebaseAuth.currentUser?.let {
            isLoggedIn = true
        }
    }

    private fun checkFirstInstall(){
        viewModelScope.launch(Dispatchers.IO){
             dataStore.readData(FIRST_INSTALL).collect { if (it == "true") isFirstInstall = true }
        }
    }


}