package com.example.smartpoultry.presentation.screens.accountScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val fireBaseAuthRepo: FirebaseAuthRepository,
    private val dataStore: AppDataStore
) : ViewModel() {
    val myDataStore = dataStore
    var isLoading = mutableStateOf(false)
    var toastMessage = mutableStateOf("")

    fun registerUser(userRole: String, email: String) {
        viewModelScope.launch {
            isLoading.value = true
            val result =
                fireBaseAuthRepo.registerUser(email = email, role = userRole, password = "0000000")
            result.onSuccess {
                isLoading.value = false
                toastMessage.value = "User registered successfully"
            }
            result.onFailure {
                toastMessage.value = "Failed to register: ${it.message.toString()}"
                isLoading.value = false
            }
        }
    }

    fun changeEmail(email: String) {

    }

    fun changeUserName(name: String) {
        viewModelScope.launch {
            fireBaseAuthRepo.editUserName(name)
        }
    }

    fun changeUserRole(role: String) {

    }

    fun changePhoneNumber(phoneNumber: String) {

    }

    fun resetPassword() {

    }
}