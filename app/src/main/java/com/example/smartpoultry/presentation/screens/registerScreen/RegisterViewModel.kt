package com.example.smartpoultry.presentation.screens.registerScreen

import androidx.lifecycle.ViewModel
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel(){
}