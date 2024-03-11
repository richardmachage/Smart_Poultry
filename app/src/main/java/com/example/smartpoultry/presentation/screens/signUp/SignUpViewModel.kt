package com.example.smartpoultry.presentation.screens.signUp

import androidx.lifecycle.ViewModel
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    private  val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

}