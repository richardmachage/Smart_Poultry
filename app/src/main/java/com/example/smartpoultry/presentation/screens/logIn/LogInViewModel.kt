package com.example.smartpoultry.presentation.screens.logIn

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import com.example.smartpoultry.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel
class LogInViewModel (
    private val firebaseAuthRepository: FirebaseAuthRepository
): ViewModel() {
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var validateError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var isLogInSuccess by mutableStateOf(false)

    fun onLogIn(){
        isLoading.value = true
        viewModelScope.launch {
            if (validateInputs()){
                val result = firebaseAuthRepository.logIn(email.value, password = password.value)
                result.onSuccess {
                    validateError.value = "Log In successsful"
                    isLogInSuccess = true
                }
                result.onFailure {
                    validateError.value = "Log in failed: ${it.message.toString()}"
                }
            }
        }
    }

    private fun validateInputs() : Boolean{
        if (email.value.isBlank()){
            validateError.value = "Email field is empty"
            return false
        }

        if (password.value.isBlank()){
            validateError.value = "Password field is empty"
            return false
        }

        if (!isValidEmail(email.value)){
            validateError.value = "Invalid email address"
            return false
        }

        return true
    }
}