package com.example.smartpoultry.presentation.screens.settingsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


const val PAST_DAYS_KEY = "past_days"
const val CONSUCUTIVE_DAYS_KEY = "consucutive_days"
const val THRESHOLD_RATIO_KEY = "threshold_ratio"

@HiltViewModel
class SettingsViewModel @Inject constructor (
    private val dataStore: AppDataStore
): ViewModel() {

    // Initialize StateFlows with default values
    private val _pastDays = MutableStateFlow("")
    val pastDays: StateFlow<String> = _pastDays

    private val _consucutiveNumberOfDays = MutableStateFlow("")
    val consucutiveNumberOfDays: StateFlow<String> = _consucutiveNumberOfDays

    private val _thresholdRatio = MutableStateFlow("")
    val thresholdRatio: StateFlow<String> = _thresholdRatio
    /*
    lateinit var pastDays : MutableState<String>
    lateinit var consucutiveNumberOfDays : MutableState<String>
    lateinit var thresholdRatio : MutableState<String>
*/
    init {
        loadInitialValues()

    }

    private fun loadInitialValues() {
        viewModelScope.launch {
            _pastDays.value = getFromDataStore(PAST_DAYS_KEY)
            _thresholdRatio.value = getFromDataStore(THRESHOLD_RATIO_KEY)
            _consucutiveNumberOfDays.value = getFromDataStore(CONSUCUTIVE_DAYS_KEY)
        }
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