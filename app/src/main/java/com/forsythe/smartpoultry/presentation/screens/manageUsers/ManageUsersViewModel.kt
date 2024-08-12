package com.forsythe.smartpoultry.presentation.screens.manageUsers

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forsythe.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.forsythe.smartpoultry.data.dataSource.remote.firebase.models.AccessLevel
import com.forsythe.smartpoultry.data.dataSource.remote.firebase.models.User
import com.forsythe.smartpoultry.domain.repository.FirebaseAuthRepository
import com.forsythe.smartpoultry.utils.FARM_ID_KEY
import com.forsythe.smartpoultry.utils.USER_EMAIL_KEY
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageUsersViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val preferencesRepo: PreferencesRepo,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : ViewModel() {


    /*objectives here
        *  1. delete user
        *  2. change permissions of a given user
        *  3. see all users registered
        * */
    val toastMessage = mutableStateOf("")
    val farmId = mutableStateOf("")
    val listOfUsers = mutableStateListOf<User>()
    var selectedUser: User? = null //user selected for editing
    var currentAccessLevel: AccessLevel? = null
    val showBottomSheet = mutableStateOf(false)
    val updateListOfEmployees = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    val myId = firebaseAuth.currentUser?.uid


    init {
        viewModelScope.launch {
            farmId.value = preferencesRepo.loadData(FARM_ID_KEY).toString()
            val result = firebaseAuthRepository.getFarmEmployees(farmId.value)
            result.onSuccess {
                listOfUsers.addAll(it)
            }
        }
    }
     fun getUserEmail() = preferencesRepo.loadData(USER_EMAIL_KEY)!!


    suspend fun updateListOfUsers() {
        farmId.value = preferencesRepo.loadData(FARM_ID_KEY).toString()
        val result = firebaseAuthRepository.getFarmEmployees(farmId.value)
        result.onSuccess {
            listOfUsers.clear()
            listOfUsers.addAll(it)
        }
    }

    fun onListItemClicked(user: User) {
        //toastMessage.value = "email for this user is ${item.email}"
        // currentUser = user
        viewModelScope.launch {
            firebaseAuthRepository.getAccessLevel(user.userId)
                .onSuccess {
                    isLoading.value = true
                    selectedUser = user
                    currentAccessLevel = it
                    isLoading.value = false
                    showBottomSheet.value = true
                }
                .onFailure {
                    toastMessage.value = "Error : ${it.localizedMessage}"
                }
        }
    }

    fun onDeleteUser(userId: String) {
        viewModelScope.launch {
            isLoading.value = true
            val result = firebaseAuthRepository.deleteUser(userId = userId)
            result.onSuccess {
                //Todo implement on success
                isLoading.value = false
                updateListOfEmployees.value = "yes"
                showBottomSheet.value = false
                toastMessage.value = "Deleted successfully"

            }
            result.onFailure {
                //Todo implement on failure
                isLoading.value = false
                toastMessage.value = "Failed to delete : ${it.localizedMessage?.toString()}"
                Log.e("on delete user", it.stackTraceToString())
            }
        }
    }

    suspend fun editAccessLevel(user: User, accessLevel: AccessLevel) : Boolean {
           /* isLoading.value = true
            val result = firebaseAuthRepository.editAccessLevel(user.userId,accessLevel)
                .onSuccess {
                    isLoading.value = false
                    toastMessage.value = "Access levels updated successfully"
                }
                .onFailure {
                    isLoading.value = false
                    toastMessage.value = "Failed to update access level for ${user.name}./n${it.localizedMessage}"
                }

        return result.isSuccess*/
        isLoading.value = true
        return try {
            val result = firebaseAuthRepository.editAccessLevel(user.userId, accessLevel)
            isLoading.value = false
            toastMessage.value = "Access levels updated successfully"
            result.getOrNull()?: false
        } catch (e: Exception) {
            isLoading.value = false
            toastMessage.value = "Failed to update access level for ${user.firstName}.\n${e.localizedMessage}"
            false
        }
    }
}