package com.example.smartpoultry.presentation.screens.onBoarding

import androidx.lifecycle.ViewModel
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor (
    val dataStore: AppDataStore
): ViewModel() {
    fun saveAppEntry(){

    }
}