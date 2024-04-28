package com.example.smartpoultry.presentation.screens.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.datastore.FIRST_INSTALL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor (
    val dataStore: AppDataStore
): ViewModel() {
    fun saveAppEntry(){
        viewModelScope.launch(Dispatchers.IO){
            dataStore.saveData(FIRST_INSTALL, "onBoardingDone")
        }
    }
}