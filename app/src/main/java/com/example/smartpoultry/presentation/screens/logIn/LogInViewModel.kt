package com.example.smartpoultry.presentation.screens.logIn

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class LogInViewModel (
    private val firebaseAuthRepository: FirebaseAuthRepository
): ViewModel() {
    var email = mutableStateOf("")
    var password = mutableStateOf("")


}