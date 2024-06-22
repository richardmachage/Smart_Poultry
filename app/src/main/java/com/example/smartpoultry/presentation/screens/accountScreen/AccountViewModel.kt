package com.example.smartpoultry.presentation.screens.accountScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.local.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.example.smartpoultry.data.dataSource.remote.firebase.models.User
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import com.example.smartpoultry.utils.USER_EMAIL_KEY
import com.example.smartpoultry.utils.USER_NAME_KEY
import com.example.smartpoultry.utils.USER_PHONE_KEY
import com.example.smartpoultry.utils.USER_ROLE_KEY
import com.example.smartpoultry.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val fireBaseAuthRepo: FirebaseAuthRepository,
    private val dataStore: AppDataStore,
    private val preferencesRepo: PreferencesRepo
) : ViewModel() {
    //val myDataStore = dataStore
    var isLoading = mutableStateOf(false)
    var toastMessage = mutableStateOf("")
    val user = User(
        name = getUserName(),
        role = getUserRole(),
        email = getUserEmail(),
        phone = getUserPhone(),
    )

    private fun getUserName() = preferencesRepo.loadData(USER_NAME_KEY)!!
    private fun getUserRole() = preferencesRepo.loadData(USER_ROLE_KEY)!!
    private fun getUserEmail() = preferencesRepo.loadData(USER_EMAIL_KEY)!!
    private fun getUserPhone() = preferencesRepo.loadData(USER_PHONE_KEY)!!

   /* val userRole = dataStore.readData(USER_ROLE_KEY).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ""
    )
    val userName = dataStore.readData(USER_NAME_KEY).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ""
    )

    val userEmail = dataStore.readData(USER_EMAIL_KEY).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ""
    )
    val userPhone = dataStore.readData(USER_PHONE_KEY).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ""
    )*/

    fun registerUser(userRole: String, email: String, name: String,phone: String) {
        viewModelScope.launch {
            isLoading.value = true
            if (!isValidEmail(email)){
                isLoading.value = false
                toastMessage.value = "Invalid email"
            }else if (userRole.isBlank()){
                isLoading.value = false
                toastMessage.value = "Please select a role"
            }else{
                val result = fireBaseAuthRepo.registerUser(
                    email = email,
                    role = userRole,
                    password = "0000000",
                    farmId = dataStore.farmID,
                    name = name,
                    phone = phone
                )
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
    }

    fun changeEmail(email: String) {
        viewModelScope.launch {
            val result = fireBaseAuthRepo.editEmail(email = email)
            result.onSuccess {
                toastMessage.value = "Request successful, changes will reflect on next log in"
            }
            result.onFailure {
                toastMessage.value = "failed: ${it.message.toString()}"
            }
        }
    }

    fun changeUserName(name: String) {
        viewModelScope.launch {
            isLoading.value = true
            val result = fireBaseAuthRepo.editUserName(name)
            if (result.isSuccess) toastMessage.value =
                "Change successful, changes will reflect on next log in"
            else if (result.isFailure) toastMessage.value =
                "Failed: ${result.exceptionOrNull()?.message.toString()}"

            isLoading.value = false
        }
    }

    fun changeUserRole(role: String) {

    }

    fun changePhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            isLoading.value = true
            val result = fireBaseAuthRepo.editPhone(phoneNumber)
            result.onSuccess {
                toastMessage.value = "Change successful, changes will reflect on next log in"
            }
            result.onFailure {
                toastMessage.value = "failed: ${it.message.toString()}"
            }
            isLoading.value = false
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch{
            val result = fireBaseAuthRepo.resetPassword(email)

        }
    }
}