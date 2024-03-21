package com.example.smartpoultry.presentation.screens.settingsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


const val PAST_DAYS_KEY = "past_days"
const val CONSUCUTIVE_DAYS_KEY = "consucutive_days"
const val THRESHOLD_RATIO_KEY = "threshold_ratio"
const val REPEAT_INTERVAL_KEY = "repeat_interval"
const val IS_AUTOMATED_ANALYSIS_KEY = "is_automated_analysis"

@HiltViewModel
class SettingsViewModel @Inject constructor (
    private val dataStore: AppDataStore,
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    val myDataStore = dataStore
    // Initialize StateFlows with default values
    private val _pastDays = MutableStateFlow("")
    val pastDays: StateFlow<String> = _pastDays

    private val _consucutiveNumberOfDays = MutableStateFlow("")
    val consucutiveNumberOfDays: StateFlow<String> = _consucutiveNumberOfDays

    private val _thresholdRatio = MutableStateFlow("")
    val thresholdRatio: StateFlow<String> = _thresholdRatio

    private val _repeatInterval = MutableStateFlow("")
    val repeatInterval : StateFlow<String> = _repeatInterval

    private val _isAutomatedAnalysis = MutableStateFlow("")
    val isAutomatedAnalysis : StateFlow<String> = _isAutomatedAnalysis

    init {
        //getPastDays()
        loadInitialValues()

    }

    private fun loadInitialValues() {
        viewModelScope.launch {
            _pastDays.value = getFromDataStore(PAST_DAYS_KEY).ifBlank { "0" }
            _thresholdRatio.value = getFromDataStore(THRESHOLD_RATIO_KEY).ifBlank { "0" }
            _consucutiveNumberOfDays.value = getFromDataStore(CONSUCUTIVE_DAYS_KEY).ifBlank { "0" }
            _repeatInterval.value = getFromDataStore(REPEAT_INTERVAL_KEY).ifBlank { "0" }
            _isAutomatedAnalysis.value = getFromDataStore(IS_AUTOMATED_ANALYSIS_KEY).ifBlank { "0" }
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


    fun onLogOut(){
        firebaseAuth.signOut()
        viewModelScope.launch {
            dataStore.clearDataStore()
        }
    }
}