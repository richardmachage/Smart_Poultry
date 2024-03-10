package com.example.smartpoultry.presentation.screens.settingsScreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


const val PAST_DAYS_KEY = "past_days"
const val CONSUCUTIVE_DAYS_KEY = "consucutive_days"
const val THRESHOLD_RATIO_KEY = "threshold_ratio"

@HiltViewModel
class SettingsViewModel @Inject constructor (
    private val dataStore: AppDataStore
): ViewModel() {

    lateinit var pastDays : MutableState<String>
    lateinit var consucutiveNumberOfDays : MutableState<String>
    lateinit var thresholdRatio : MutableState<String>

    init {
        pastDays.value = getFromDataStore(PAST_DAYS_KEY)
        consucutiveNumberOfDays.value = getFromDataStore(CONSUCUTIVE_DAYS_KEY)
        thresholdRatio.value = getFromDataStore(THRESHOLD_RATIO_KEY)

        Log.i(THRESHOLD_RATIO_KEY, thresholdRatio.value)
        Log.i(PAST_DAYS_KEY, pastDays.value)
        Log.i(CONSUCUTIVE_DAYS_KEY, consucutiveNumberOfDays.value)
    }
    fun saveToDataStore(key: String, value : String){
        viewModelScope.launch {
            dataStore.saveData(key, value)
        }
    }

    fun getFromDataStore(key: String) : String{
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