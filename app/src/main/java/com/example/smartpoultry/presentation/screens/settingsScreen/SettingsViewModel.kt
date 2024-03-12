package com.example.smartpoultry.presentation.screens.settingsScreen

import android.util.Log
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

    init {
        //getPastDays()
        loadInitialValues()
        Log.i(THRESHOLD_RATIO_KEY, thresholdRatio.value)
        Log.i(CONSUCUTIVE_DAYS_KEY, consucutiveNumberOfDays.value)
        Log.i(PAST_DAYS_KEY, pastDays.value)
    }

    private fun loadInitialValues() {
        viewModelScope.launch {
            _pastDays.value = getFromDataStore(PAST_DAYS_KEY).ifBlank { "0" }
            _thresholdRatio.value = getFromDataStore(THRESHOLD_RATIO_KEY).ifBlank { "0" }
            _consucutiveNumberOfDays.value = getFromDataStore(CONSUCUTIVE_DAYS_KEY).ifBlank { "0" }
        }
    }

    fun saveToDataStore(key: String, value : String){
        viewModelScope.launch {
            Log.i("Saving to Datastore from viewmodel:", value)
            dataStore.saveData(key, value)

            Log.i("Load values from datastore", value)
            //loadInitialValues()
            Log.i("new pastDays", getFromDataStore(PAST_DAYS_KEY))
            Log.i("stateFlow pastDays", pastDays.value)
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
    }
}