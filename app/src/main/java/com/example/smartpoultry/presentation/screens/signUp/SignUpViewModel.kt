package com.example.smartpoultry.presentation.screens.signUp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import com.example.smartpoultry.utils.checkPasswordLength
import com.example.smartpoultry.utils.isPasswordSame
import com.example.smartpoultry.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private  val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    var userType = mutableStateOf("")
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var confirmPassword = mutableStateOf("")
    var validationError = mutableStateOf("")


    fun onSignUp(): Boolean {
        if (isValidEmail(email.value)) {
            if (isPasswordSame(password.value, confirmPassword.value)) {
                return if (checkPasswordLength(password = password.value)) {

                    val isSignUp = firebaseAuthRepository.registerUser(
                        email = email.value,
                        password = password.value,
                        role = userType.value
                    )
                    if (isSignUp) {
                        true
                    } else {
                        validationError.value = "Failed to sign up, try again later"
                        false
                    }
                } else {
                    validationError.value = "Passwords must be more than 6 characters"
                    false
                }
            }else{
                validationError.value = "Passwords do not match"
                return false
            }
        }
            else {
                validationError.value = "Invalid Email address"
                return false
            }
        }

    fun checkEmptyFields() : Boolean{
        return  userType.value.isEmpty() || email.value.isBlank() || password.value.isBlank() || confirmPassword.value.isBlank()
    }
}