package com.example.smartpoultry.presentation.screens.mainScreen

import androidx.lifecycle.ViewModel
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val dataStore: AppDataStore
): ViewModel() {

    val myDataStore = dataStore
}