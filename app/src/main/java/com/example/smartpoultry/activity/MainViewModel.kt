package com.example.smartpoultry.activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val firebaseAuth: FirebaseAuth
) : ViewModel() {
    var isLoggedIn by mutableStateOf(false)

    init {
        checkIfLoggedIn()
    }
    private fun checkIfLoggedIn(){
        firebaseAuth.currentUser?.let {
            isLoggedIn = true
        }
    }
}