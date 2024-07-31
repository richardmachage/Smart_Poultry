package com.example.smartpoultry.presentation.screens.manageUsers.registerUser

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.example.smartpoultry.data.dataSource.remote.firebase.models.AccessLevel
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import com.example.smartpoultry.presentation.screens.manageUsers.registerUser.components.AccessLevelDetailsResponse
import com.example.smartpoultry.presentation.screens.manageUsers.registerUser.components.RegisterUserParts
import com.example.smartpoultry.presentation.screens.manageUsers.registerUser.components.RegisterUserScreenData
import com.example.smartpoultry.presentation.screens.manageUsers.registerUser.components.RegisterUserScreenState
import com.example.smartpoultry.presentation.screens.signUp.models.ContactDetailsResponse
import com.example.smartpoultry.presentation.screens.signUp.models.PersonalDetailsResponse
import com.example.smartpoultry.utils.Countries
import com.example.smartpoultry.utils.FARM_COUNTRY_KEY
import com.example.smartpoultry.utils.FARM_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterUserViewModel @Inject constructor(
    private val preferencesRepo: PreferencesRepo,
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    private var listOfParts = RegisterUserParts.entries.toList()
    private var currentPartIndex = 0


    private var _registerUserScreenData by mutableStateOf(RegisterUserScreenData(country = getCountry()))
    val registerUserScreenData: RegisterUserScreenData
        get() = _registerUserScreenData

    private var _registerUserScreenState by mutableStateOf(
        RegisterUserScreenState(
            isLoading = false,
            showPrevious = false,
            showContinue = true,
            currentPart = listOfParts[currentPartIndex],
            isContinueEnabled = isContinueEnabled(listOfParts[currentPartIndex])
        )
    )
    val registerUserScreenState: RegisterUserScreenState
        get() = _registerUserScreenState


    fun onPrevious() {
        if (currentPartIndex > 0) {
            currentPartIndex-- //= currentPartIndex - 1
            _registerUserScreenState = _registerUserScreenState.copy(
                currentPart = listOfParts[currentPartIndex],
                showPrevious = if (currentPartIndex == 0) false else true,
                showContinue = true,
                isContinueEnabled = isContinueEnabled(listOfParts[currentPartIndex])
            )

        }
    }

    fun onContinue() {
        if (currentPartIndex < (listOfParts.size - 1)) {
            currentPartIndex++
            _registerUserScreenState = _registerUserScreenState.copy(
                currentPart = listOfParts[currentPartIndex],
                showPrevious = true,
                showContinue = currentPartIndex != listOfParts.size - 1,
                isContinueEnabled = isContinueEnabled(currentPart = listOfParts[currentPartIndex])
            )
        }
    }

    fun onDone() {
        registerUser()
    }

    private fun registerUser() {
        viewModelScope.launch {
            _registerUserScreenState = _registerUserScreenState.copy(isLoading = true)

            if (_registerUserScreenData.checkAllDetailsNotBlank()) {
                val result = firebaseAuthRepository.registerUser(
                    firstName = registerUserScreenData.firstName,
                    lastName = registerUserScreenData.lastName,
                    gender = registerUserScreenData.gender,
                    email = registerUserScreenData.email,
                    phone = registerUserScreenData.phone,
                    password = "0000000",
                    farmId = getFarmId(),
                    accessLevel = AccessLevel(
                        collectEggs = registerUserScreenData.eggCollectionAccess,
                        editHenCount = registerUserScreenData.editHenCountAccess,
                        manageUsers = registerUserScreenData.manageUsers,
                        manageBlocksCells = registerUserScreenData.manageBlockCells
                    )
                )

                result.onSuccess {
                    _registerUserScreenState = _registerUserScreenState.copy(
                        toastMessage = "${registerUserScreenData.firstName} registered successfully"
                    )
                }

                result.onFailure {
                    _registerUserScreenState = _registerUserScreenState.copy(
                        toastMessage = "Failed to register ${it.localizedMessage}"
                    )
                }

            } else {
                _registerUserScreenState = _registerUserScreenState.copy(
                    toastMessage = "Please fill in all required fields to register new user"
                )
            }

            _registerUserScreenState = _registerUserScreenState.copy(isLoading = false)

        }
    }

    fun onPersonalDetailsResponse(personalDetailsResponse: PersonalDetailsResponse) {
        if (personalDetailsResponse.isValidResponse()) {
            _registerUserScreenData = _registerUserScreenData.copy(
                firstName = personalDetailsResponse.firstName,
                lastName = personalDetailsResponse.lastName,
                gender = personalDetailsResponse.gender
            )
            _registerUserScreenState = _registerUserScreenState.copy(isContinueEnabled = true)

        } else {
            _registerUserScreenState = _registerUserScreenState.copy(isContinueEnabled = false)
        }
    }

    fun onContactDetailsResponse(contactDetailsResponse: ContactDetailsResponse) {
        if (contactDetailsResponse.isNoEmptyField()) {
            _registerUserScreenData = _registerUserScreenData.copy(
                phone = contactDetailsResponse.phone,
                email = contactDetailsResponse.email
            )
            _registerUserScreenState =
                _registerUserScreenState.copy(isContinueEnabled = contactDetailsResponse.hasError)
        } else {
            _registerUserScreenState = _registerUserScreenState.copy(isContinueEnabled = false)
        }
    }

    fun onAccessLevelResponse(accessLevelDetailsResponse: AccessLevelDetailsResponse) {
        _registerUserScreenData = _registerUserScreenData.copy(
            manageUsers = accessLevelDetailsResponse.manageUsers,
            editHenCountAccess = accessLevelDetailsResponse.editHenCount,
            manageBlockCells = accessLevelDetailsResponse.manageBlocksCells,
            eggCollectionAccess = accessLevelDetailsResponse.eggCollection
        )
    }

    private fun isContinueEnabled(currentPart: RegisterUserParts): Boolean {
        return when (currentPart) {
            RegisterUserParts.PERSONAL_DETAILS -> {
                //false
                _registerUserScreenData.personalDetailsNotBlank()
            }

            RegisterUserParts.CONTACT_DETAILS -> {
                _registerUserScreenData.contactDetailsNotBlank()

            }

            RegisterUserParts.ACCESS_LEVEL_DETAILS -> {
                true
            }
        }
    }

    private fun getCountry(): Countries {
        val country = preferencesRepo.loadData(FARM_COUNTRY_KEY) ?: Countries.KENYA.countryName
        return when (country) {
            Countries.KENYA.countryName -> Countries.KENYA
            Countries.TANZANIA.countryName -> Countries.TANZANIA
            else -> Countries.TANZANIA
        }
    }

    fun clearToastMessageValue() {
        _registerUserScreenState = _registerUserScreenState.copy(toastMessage = "")
    }

    private fun getFarmId() = preferencesRepo.loadData(FARM_ID_KEY)!!

}