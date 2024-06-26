package com.example.smartpoultry.presentation.screens.settingsScreen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.local.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.example.smartpoultry.data.dataSource.local.room.database.SmartPoultryDatabase
import com.example.smartpoultry.utils.CONSUCUTIVE_DAYS_KEY
import com.example.smartpoultry.utils.EDIT_HEN_COUNT_ACCESS
import com.example.smartpoultry.utils.EGG_COLLECTION_ACCESS
import com.example.smartpoultry.utils.FARM_ID_KEY
import com.example.smartpoultry.utils.IS_AUTOMATED_ANALYSIS_KEY
import com.example.smartpoultry.utils.IS_PASSWORD_RESET_KEY
import com.example.smartpoultry.utils.MANAGE_BLOCKS_CELLS_ACCESS
import com.example.smartpoultry.utils.MANAGE_USERS_ACCESS
import com.example.smartpoultry.utils.PAST_DAYS_KEY
import com.example.smartpoultry.utils.REPEAT_INTERVAL_KEY
import com.example.smartpoultry.utils.THRESHOLD_RATIO_KEY
import com.example.smartpoultry.utils.USER_EMAIL_KEY
import com.example.smartpoultry.utils.USER_NAME_KEY
import com.example.smartpoultry.utils.USER_PHONE_KEY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor (
    @ApplicationContext private val context: Context,
    private val dataStore: AppDataStore,
    private val firebaseAuth: FirebaseAuth,
    private val preferencesRepo: PreferencesRepo,
    private val smartPoultryDatabase: SmartPoultryDatabase
): ViewModel() {

    val toastMessage = mutableStateOf("")
    val myDataStore = dataStore
    val isLoading = mutableStateOf(false)
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
            _pastDays.value = preferencesRepo.loadData(PAST_DAYS_KEY)!!//getFromDataStore(PAST_DAYS_KEY).ifBlank { "0" }
            _thresholdRatio.value = preferencesRepo.loadData(THRESHOLD_RATIO_KEY)!!//getFromDataStore(THRESHOLD_RATIO_KEY).ifBlank { "0" }
            _consucutiveNumberOfDays.value = preferencesRepo.loadData(CONSUCUTIVE_DAYS_KEY)!!//getFromDataStore(CONSUCUTIVE_DAYS_KEY).ifBlank { "0" }
            _repeatInterval.value = preferencesRepo.loadData(REPEAT_INTERVAL_KEY)!!//getFromDataStore(REPEAT_INTERVAL_KEY).ifBlank { "0" }
            _isAutomatedAnalysis.value = preferencesRepo.loadData(IS_AUTOMATED_ANALYSIS_KEY)!!//getFromDataStore(IS_AUTOMATED_ANALYSIS_KEY).ifBlank { "0" }
        }
    }

    fun saveToDataStore(key: String, value : String){
        preferencesRepo.saveData(key=key,value=value)
        loadInitialValues()
    }

    fun getFromDataStore(key: String) : String{
        return preferencesRepo.loadData(key)!!
    }


    suspend fun onLogOut(){
        isLoading.value = true
        // Clear preferences and database in the IO context
        withContext(Dispatchers.IO) {
            //clear preferences
            Log.d("Clear Preferences", "onLogOut called: Start clearing... ")
            preferencesRepo.deleteData(FARM_ID_KEY)
            Log.d("Clear Preferences", "cleared farm id ")

            preferencesRepo.deleteData(USER_NAME_KEY)
            Log.d("Clear Preferences", "cleared user name")

            preferencesRepo.deleteData(USER_EMAIL_KEY)
            Log.d("Clear Preferences", "cleared email ")

            preferencesRepo.deleteData(EGG_COLLECTION_ACCESS)
            preferencesRepo.deleteData(EDIT_HEN_COUNT_ACCESS)
            preferencesRepo.deleteData(MANAGE_USERS_ACCESS)
            preferencesRepo.deleteData(MANAGE_BLOCKS_CELLS_ACCESS)
            Log.d("Clear Preferences", "cleared user role ")

            preferencesRepo.deleteData(USER_PHONE_KEY)
            Log.d("Clear Preferences", "cleared user phone ")

            preferencesRepo.deleteData(IS_PASSWORD_RESET_KEY)
            Log.d("Clear Preferences", "cleared is password reset ")


            //check if preferences is cleared
            val ema = preferencesRepo.loadData(USER_EMAIL_KEY)!!
            Log.d("Clear Preferences", "The user email now is : $ema")
            // Clear database and sign out concurrently
            Log.d("Clear Database", "Clearing Database begins ")
            val clearDatabaseJob = async {
                smartPoultryDatabase.clearAllTables()
                Log.d("Clear Database", "Clearing Database finish ")

            }
            val signOutJob = async {
                firebaseAuth.signOut()
                Log.d("Clear Database", "Clearing finish, sign outs ")

            }

            clearDatabaseJob.await()
            signOutJob.await()
            isLoading.value = false
        }
    }
}