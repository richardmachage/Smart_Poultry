package com.example.smartpoultry.presentation.screens.mainScreen

import androidx.lifecycle.ViewModel
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.datastore.PreferencesRepo
import com.example.smartpoultry.utils.USER_ROLE_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val dataStore: AppDataStore,
    private val preferencesRepo: PreferencesRepo
): ViewModel() {
   // val user = getThisUser(preferencesRepo)
    //val myDataStore = dataStore
    fun getUserRole() = preferencesRepo.loadData(USER_ROLE_KEY)!!
}