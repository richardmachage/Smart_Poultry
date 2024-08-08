package com.forsythe.smartpoultry.presentation.screens.mainScreen

import androidx.lifecycle.ViewModel
import com.forsythe.smartpoultry.data.dataSource.local.datastore.AppDataStore
import com.forsythe.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.forsythe.smartpoultry.utils.EGG_COLLECTION_ACCESS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val dataStore: AppDataStore,
    private val preferencesRepo: PreferencesRepo
): ViewModel() {
   // val user = getThisUser(preferencesRepo)
    //val myDataStore = dataStore
    //fun getUserRole() = preferencesRepo.loadData(USER_ROLE_KEY)!!
    fun getEggCollectionAccess() = preferencesRepo.loadData(EGG_COLLECTION_ACCESS).toBoolean() // Make this a state Flow to update automatically
}