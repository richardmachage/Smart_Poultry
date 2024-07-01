package com.example.smartpoultry.presentation.screens.manageUsers

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.example.smartpoultry.data.dataSource.remote.firebase.models.AccessLevel
import com.example.smartpoultry.data.dataSource.remote.firebase.models.User
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import com.example.smartpoultry.utils.FARM_ID_KEY
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageUsersViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val preferencesRepo: PreferencesRepo,
    firebaseAuth: FirebaseAuth,
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
    var currentUser: User? = null //user selected for editing
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
                    currentUser = user
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
}