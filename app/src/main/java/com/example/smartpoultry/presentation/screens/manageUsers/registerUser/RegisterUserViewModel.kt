package com.example.smartpoultry.presentation.screens.manageUsers.registerUser

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.smartpoultry.presentation.screens.manageUsers.registerUser.components.RegisterUserParts
import com.example.smartpoultry.presentation.screens.manageUsers.registerUser.components.RegisterUserScreenData
import com.example.smartpoultry.presentation.screens.manageUsers.registerUser.components.RegisterUserScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterUserViewModel @Inject constructor(

) : ViewModel() {

    private var listOfParts = RegisterUserParts.entries.toList()
    private var currentPartIndex = 0

    private var _registerUserScreenState by mutableStateOf(RegisterUserScreenState(
        isLoading = false,
        showPrevious = false,
        showContinue = true,
        currentPart = listOfParts[currentPartIndex],
        isContinueEnabled = isContinueEnabled(listOfParts[currentPartIndex])
    ))
  val registerUserScreenState:RegisterUserScreenState
      get() = _registerUserScreenState

    private var _registerUserScreenData by mutableStateOf(RegisterUserScreenData())
    val registerUserScreenData : RegisterUserScreenData
        get() = _registerUserScreenData

    fun onPrevious() {
        if (currentPartIndex > 0){
            currentPartIndex = currentPartIndex - 1
            _registerUserScreenState = _registerUserScreenState.copy(
                currentPart = listOfParts[currentPartIndex],
                showPrevious = if (currentPartIndex == 0) false else true,
                showContinue = true
            )

        }
    }

    fun onContinue() {

    }

    fun onDone() {

    }

    private fun isContinueEnabled(currentPart: RegisterUserParts):Boolean{
        return when(currentPart){
            RegisterUserParts.PERSONAL_DETAILS -> {
                false
            }
            RegisterUserParts.CONTACT_DETAILS -> {
                false
            }
        }
    }
}