package com.example.smartpoultry.presentation.screens.splashScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewmodel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel() {
    var isLoggedIn by mutableStateOf(false)

    init {
        checkUser()
    }
    private fun checkUser(){
         firebaseAuth.currentUser?.let {
            isLoggedIn = true
        }
    }
}