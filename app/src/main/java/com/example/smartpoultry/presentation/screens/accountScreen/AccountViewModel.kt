package com.example.smartpoultry.presentation.screens.accountScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val fireBaseAuthRepo : FirebaseAuthRepository,
    private val dataStore : AppDataStore
) : ViewModel() {
    val myDataStore = dataStore
    fun registerUser(){

    }
    fun changeEmail(){

    }
    fun changeUserName(name : String){
        viewModelScope.launch {
            fireBaseAuthRepo.editUserName(name)
        }
    }
}