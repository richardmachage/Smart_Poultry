package com.example.smartpoultry.presentation.screens.onBoarding

import androidx.lifecycle.ViewModel
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.datastore.PreferencesRepo
import com.example.smartpoultry.utils.FIRST_INSTALL
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor (
    val dataStore: AppDataStore,
    val preferencesRepo: PreferencesRepo
): ViewModel() {
    fun saveAppEntry(){
        /*viewModelScope.launch(Dispatchers.IO){
            dataStore.saveData(FIRST_INSTALL, "onBoardingDone")
        }
        */
        preferencesRepo.saveData(FIRST_INSTALL,"onBoardingDone")
    }
}