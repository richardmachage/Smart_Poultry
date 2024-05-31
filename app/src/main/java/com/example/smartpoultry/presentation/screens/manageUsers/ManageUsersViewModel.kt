package com.example.smartpoultry.presentation.screens.manageUsers

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.datastore.FARM_ID_KEY
import com.example.smartpoultry.data.dataSource.datastore.PreferencesRepo
import com.example.smartpoultry.data.dataSource.remote.firebase.models.User
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
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
    var  currentUser : User? = null //user selected for editing
    val showBottomSheet = mutableStateOf(false)



    init {
        viewModelScope.launch {
            farmId.value = preferencesRepo.loadData(FARM_ID_KEY).toString()
            val result = firebaseAuthRepository.getFarmEmployees(farmId.value)
            result.onSuccess {
                listOfUsers.addAll(it)
            }
        }
    }

    fun onListItemClicked(user : User ){
        //toastMessage.value = "email for this user is ${item.email}"
        currentUser = user
        showBottomSheet.value = true
    }

    fun onDeleteUser(userId:String){
        viewModelScope.launch {
           val result = firebaseAuthRepository.deleteUser(userId = userId)
            result.onSuccess {
                //Todo implement on succes
            }
            result.onFailure {
                //Todo implement on failure
            }
        }
    }

}