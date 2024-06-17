package com.example.smartpoultry.presentation.screens.manageUsers

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.datastore.PreferencesRepo
import com.example.smartpoultry.data.dataSource.remote.firebase.models.User
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import com.example.smartpoultry.utils.FARM_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageUsersViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val preferencesRepo: PreferencesRepo
): ViewModel(){


    /*objectives here
        *  1. delete user
        *  2. change permissions of a given user
        *  3. see all users registered
        * */
    val toastMessage = mutableStateOf("")
    val farmId = mutableStateOf("")
    val listOfUsers = mutableStateListOf<User>()
    var currentUser : User? = null //user selected for editing
    val showBottomSheet = mutableStateOf(false)
    val updateListOfEmployees = mutableStateOf("")
    val isLoading = mutableStateOf(false)




    init {
        viewModelScope.launch {
            farmId.value = preferencesRepo.loadData(FARM_ID_KEY).toString()
            val result = firebaseAuthRepository.getFarmEmployees(farmId.value)
            result.onSuccess {
                listOfUsers.addAll(it)
            }
        }
    }

    suspend fun updateListOfUsers(){
       farmId.value = preferencesRepo.loadData(FARM_ID_KEY).toString()
        val result = firebaseAuthRepository.getFarmEmployees(farmId.value)
        result.onSuccess {
            listOfUsers.clear()
            listOfUsers.addAll(it)
        }
    }
    fun onListItemClicked(user : User ){
        //toastMessage.value = "email for this user is ${item.email}"
        currentUser = user
        showBottomSheet.value = true
    }

    fun onDeleteUser(userId:String){
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