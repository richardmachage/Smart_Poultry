package com.example.smartpoultry.presentation.screens.logIn

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import com.example.smartpoultry.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
): ViewModel() {
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var validateError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var isLogInSuccess by mutableStateOf(false)

    fun onLogIn(){
        viewModelScope.launch {
            isLoading.value = true
            if (validateInputs()){
                val result = firebaseAuthRepository.logIn(email.value, password = password.value)
                result.onSuccess {
                    validateError.value = "Log In successful"
                    isLogInSuccess = true
                    //isLoading.value = false
                }
                result.onFailure {
                    validateError.value = "Log in failed: ${it.message.toString()}"
                   //isLoading.value = false
                }
            }
            isLoading.value = false
        }
    }

    fun onPasswordReset(){
        if (email.value.isNotBlank() && isValidEmail(email.value)){
            viewModelScope.launch {
                isLoading.value = true
                val result = firebaseAuthRepository.resetPassword(email.value)
                result.onSuccess {
                    validateError.value = "Password reset link sent to email"
                    isLoading.value = false
                }
                result.onFailure {
                    validateError.value = "Failed : ${it.message.toString()} "
                    isLoading.value = false

                }
            }
        }else{
            validateError.value = "Invalid email address"
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

    fun restartApp(context: Context) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        if (context is Activity) {
            context.finish()
        }
        Runtime.getRuntime().exit(0) // Optionally, force kill the app to ensure a full restart
    }
}