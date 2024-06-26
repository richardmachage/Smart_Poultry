package com.example.smartpoultry.presentation.screens.signUp

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import com.example.smartpoultry.utils.checkPasswordLength
import com.example.smartpoultry.utils.isPasswordSame
import com.example.smartpoultry.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private  val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    var farmName = mutableStateOf("")
    var email = mutableStateOf("")
    var phone = mutableStateOf("")
    var name = mutableStateOf("")
    var password = mutableStateOf("")
    var confirmPassword = mutableStateOf("")
    var terms = mutableStateOf(false)

    var toastMessage = mutableStateOf("")
    var validationError = mutableStateOf("")
    var isCreateAccountSuccess by mutableStateOf(false)
    var isLoading = mutableStateOf(false)
        private set


    fun onSignUp() {
        viewModelScope.launch {
            if (validateSignUp()) {
                isLoading.value = true
                val result =
                    firebaseAuthRepository.signUp(
                        email = email.value.trim(),
                        password = password.value,
                        role = "Super",
                        farmName = farmName.value.trim(),
                        userName = name.value,
                        phone = phone.value
                    )
                   // firebaseAuthRepository.registerUser(email.value, password.value, userType.value)
                result.onSuccess {
                    validationError.value = "Account created successfully, proceed to log in"
                    isLoading.value = false
                    isCreateAccountSuccess = true
                }
                    .onFailure {
                        validationError.value = "Failed to sign up : ${it.message.toString()}"
                        Log.d("error:","Failed to sign up : ${it.message.toString()}" )
                        isLoading.value = false
                    }
            }
        }
    }

    private fun validateSignUp(): Boolean {
        //check empty fields
        if (checkEmptyFields()){
            //validationError.value = "You must fill in all fields to Sign Up"
            toastMessage.value = "You must fill in all fields to Sign Up"
            return false
        }

        // Validate Email
        if (!isValidEmail(email.value)) {
            //validationError.value = "Invalid Email address"
            toastMessage.value = "Invalid Email address"
            return false
        }

        // Check if Passwords Match
        if (!isPasswordSame(password.value, confirmPassword.value)) {
            //validationError.value = "Passwords do not match"
            toastMessage.value = "Passwords do not match"
            return false
        }

        // Check Password Length
        if (!checkPasswordLength(password.value)) {
            //validationError.value = "Passwords must be more than 6 characters"
            toastMessage.value = "Passwords must be more than 6 characters"
            return false
        }

        // At this point, all validations have passed
        return true
    }

    private fun checkEmptyFields() : Boolean{
        return  farmName.value.isBlank() || email.value.isBlank() || password.value.isBlank() || confirmPassword.value.isBlank() || name.value.isBlank() || phone.value.isBlank()
    }
}

