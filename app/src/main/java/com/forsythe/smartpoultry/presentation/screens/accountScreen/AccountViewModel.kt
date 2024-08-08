package com.forsythe.smartpoultry.presentation.screens.accountScreen

//import com.forsythe.smartpoultry.utils.USER_ROLE_KEY
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forsythe.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.forsythe.smartpoultry.data.dataSource.remote.firebase.models.AccessLevel
import com.forsythe.smartpoultry.data.dataSource.remote.firebase.models.User
import com.forsythe.smartpoultry.domain.repository.FirebaseAuthRepository
import com.forsythe.smartpoultry.utils.EDIT_HEN_COUNT_ACCESS
import com.forsythe.smartpoultry.utils.EGG_COLLECTION_ACCESS
import com.forsythe.smartpoultry.utils.FARM_COUNTRY_KEY
import com.forsythe.smartpoultry.utils.FARM_ID_KEY
import com.forsythe.smartpoultry.utils.FARM_NAME_KEY
import com.forsythe.smartpoultry.utils.MANAGE_BLOCKS_CELLS_ACCESS
import com.forsythe.smartpoultry.utils.MANAGE_USERS_ACCESS
import com.forsythe.smartpoultry.utils.USER_EMAIL_KEY
import com.forsythe.smartpoultry.utils.USER_FIRST_NAME_KEY
import com.forsythe.smartpoultry.utils.USER_GENDER_KEY
import com.forsythe.smartpoultry.utils.USER_LAST_NAME_KEY
import com.forsythe.smartpoultry.utils.USER_PHONE_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val fireBaseAuthRepo: FirebaseAuthRepository,
    private val preferencesRepo: PreferencesRepo
) : ViewModel() {
    //val myDataStore = dataStore
    var isLoading = mutableStateOf(false)
    var toastMessage = mutableStateOf("")
    var eggCollectionAccess = mutableStateOf(false)
    var editHenCountAccess = mutableStateOf(false)
    var manageUsersAccess = mutableStateOf(false)
    var manageBlocksCellsAccess = mutableStateOf(false)
    var accessLevel = mutableStateOf(getAccessLevel())
    val user = User(
        firstName = getUserFirstName(),
        lastName = getUserLastName(),
        gender =  getUserGender(),
        email = getUserEmail(),
        phone = getUserPhone(),
    )

    private fun getUserFirstName() = preferencesRepo.loadData(USER_FIRST_NAME_KEY)!!
    private fun getUserLastName() = preferencesRepo.loadData(USER_LAST_NAME_KEY)!!
    private fun getUserEmail() = preferencesRepo.loadData(USER_EMAIL_KEY)!!
    private fun getUserPhone() = preferencesRepo.loadData(USER_PHONE_KEY)!!
    private fun getUserGender() = preferencesRepo.loadData(USER_GENDER_KEY)!!
    private fun getAccessLevel(): AccessLevel {
        return AccessLevel(
            manageUsers = preferencesRepo.loadData(MANAGE_USERS_ACCESS).toBoolean(),
            manageBlocksCells = preferencesRepo.loadData(MANAGE_BLOCKS_CELLS_ACCESS).toBoolean(),
            editHenCount = preferencesRepo.loadData(EDIT_HEN_COUNT_ACCESS).toBoolean(),
            collectEggs = preferencesRepo.loadData(EGG_COLLECTION_ACCESS).toBoolean()
        )

    }

    private fun getFarmId() = preferencesRepo.loadData(FARM_ID_KEY)!!
    fun getFarmName() = preferencesRepo.loadData(FARM_NAME_KEY)!!
    fun getFarmCountry() = preferencesRepo.loadData(FARM_COUNTRY_KEY)!!

/*

    fun registerUser( email: String, name: String, phone: String) {
        viewModelScope.launch {
            isLoading.value = true
            if (!isValidEmail(email)) {
                isLoading.value = false
                toastMessage.value = "Invalid email: $email"

            } else {
                val result = fireBaseAuthRepo.registerUser(
                    email = email,
                    password = "0000000",
                    farmId = getFarmId(),
                    name = name,
                    phone = phone,
                    accessLevel = AccessLevel(
                        collectEggs = eggCollectionAccess.value,
                        editHenCount = editHenCountAccess.value,
                        manageBlocksCells = manageBlocksCellsAccess.value,
                        manageUsers = manageUsersAccess.value
                    )
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
*/

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

}