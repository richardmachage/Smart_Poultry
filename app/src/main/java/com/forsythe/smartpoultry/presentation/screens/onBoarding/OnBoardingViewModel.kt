package com.forsythe.smartpoultry.presentation.screens.onBoarding

import androidx.lifecycle.ViewModel
import com.forsythe.smartpoultry.data.dataSource.local.datastore.AppDataStore
import com.forsythe.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.forsythe.smartpoultry.utils.FIRST_INSTALL
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