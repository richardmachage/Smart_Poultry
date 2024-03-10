package com.example.smartpoultry.presentation.screens.settingsScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor (
    private val dataStore: AppDataStore
): ViewModel() {
    var pastDays = mutableStateOf("")
    var consucutiveNumberOfDays = mutableStateOf("")
    var thresholdRatio = mutableStateOf("")


    fun saveToDataStore(key: String, value : String){
        viewModelScope.launch {
            dataStore.saveData(key, value)
        }
    }

    fun getValueFromDataStore(key: String) : String{
        var data = ""
        viewModelScope.launch {
            dataStore.readData(key).collect{
                data = it
            }
        }
        return data
    }
    fun onLogOut() : Boolean{
        return true
    }
}