package com.example.smartpoultry.presentation.screens.accountScreen

import androidx.lifecycle.ViewModel
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class AccountViewModel(
    private val fireBaseAuthRepo : FirebaseAuthRepository
) : ViewModel() {
    fun registerUser(){

    }
    fun changeEmail(){

    }
    fun changeUserName(){

    }
}