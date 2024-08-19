package com.forsythe.smartpoultry.presentation.screens.settingsScreen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.forsythe.smartpoultry.data.dataSource.local.datastore.AppDataStore
import com.forsythe.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.forsythe.smartpoultry.data.dataSource.local.room.database.SmartPoultryDatabase
import com.forsythe.smartpoultry.domain.workers.AnalysisWorker
import com.forsythe.smartpoultry.utils.CONSUCUTIVE_DAYS_KEY
import com.forsythe.smartpoultry.utils.EDIT_HEN_COUNT_ACCESS
import com.forsythe.smartpoultry.utils.EGG_COLLECTION_ACCESS
import com.forsythe.smartpoultry.utils.IS_AUTOMATED_ANALYSIS_KEY
import com.forsythe.smartpoultry.utils.IS_PASSWORD_RESET_KEY
import com.forsythe.smartpoultry.utils.MANAGE_BLOCKS_CELLS_ACCESS
import com.forsythe.smartpoultry.utils.MANAGE_USERS_ACCESS
import com.forsythe.smartpoultry.utils.PAST_DAYS_KEY
import com.forsythe.smartpoultry.utils.REPEAT_INTERVAL_KEY
import com.forsythe.smartpoultry.utils.THRESHOLD_RATIO_KEY
import com.forsythe.smartpoultry.utils.USER_EMAIL_KEY
import com.forsythe.smartpoultry.utils.USER_FIRST_NAME_KEY
import com.forsythe.smartpoultry.utils.USER_GENDER_KEY
import com.forsythe.smartpoultry.utils.USER_LAST_NAME_KEY
import com.forsythe.smartpoultry.utils.USER_PHONE_KEY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
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

    private var _showInfoDialog  = MutableStateFlow(false)
    val showInfoDialog : StateFlow<Boolean>
        get() = _showInfoDialog


    init {
        //getPastDays()
        loadInitialValues()

    }



     fun setWorker() {
            val workRequest = PeriodicWorkRequestBuilder<AnalysisWorker>(
                repeatInterval.value.toLong(),
                TimeUnit.HOURS
            ).setInitialDelay(repeatInterval.value.toLong(), TimeUnit.HOURS)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    "periodic_analysis",
                    ExistingPeriodicWorkPolicy.UPDATE,
                    workRequest
                )

         if (repeatInterval.value.toInt() > 1){
            toastMessage.value = "Automatic analysis set to repeat after ${repeatInterval.value} hours"
         }else{
             toastMessage.value = "Automatic analysis set to repeat after ${repeatInterval.value} hour"
         }
    }

     fun cancelWorker(){
        WorkManager.getInstance(context).cancelUniqueWork("periodic_analysis")
        toastMessage.value = "Automated analysis turned off"

    }

    private fun loadInitialValues() {
            _pastDays.value = preferencesRepo.loadData(PAST_DAYS_KEY)!!//getFromDataStore(PAST_DAYS_KEY).ifBlank { "0" }
            _thresholdRatio.value = preferencesRepo.loadData(THRESHOLD_RATIO_KEY)!!//getFromDataStore(THRESHOLD_RATIO_KEY).ifBlank { "0" }
            _consucutiveNumberOfDays.value = preferencesRepo.loadData(CONSUCUTIVE_DAYS_KEY)!!//getFromDataStore(CONSUCUTIVE_DAYS_KEY).ifBlank { "0" }
            _repeatInterval.value = preferencesRepo.loadData(REPEAT_INTERVAL_KEY)!!//getFromDataStore(REPEAT_INTERVAL_KEY).ifBlank { "0" }
            _isAutomatedAnalysis.value = preferencesRepo.loadData(IS_AUTOMATED_ANALYSIS_KEY)!!//getFromDataStore(IS_AUTOMATED_ANALYSIS_KEY).ifBlank { "0" }
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
            //1. User Details
            preferencesRepo.deleteData(USER_FIRST_NAME_KEY)
            preferencesRepo.deleteData(USER_LAST_NAME_KEY)
            preferencesRepo.deleteData(USER_EMAIL_KEY)
            preferencesRepo.deleteData(USER_PHONE_KEY)
            preferencesRepo.deleteData(USER_GENDER_KEY)

            //2. Access Levels
            preferencesRepo.deleteData(EGG_COLLECTION_ACCESS)
            preferencesRepo.deleteData(EDIT_HEN_COUNT_ACCESS)
            preferencesRepo.deleteData(MANAGE_USERS_ACCESS)
            preferencesRepo.deleteData(MANAGE_BLOCKS_CELLS_ACCESS)

            //3.is password reset feature
            preferencesRepo.deleteData(IS_PASSWORD_RESET_KEY)




            val signOutJob = async {
                firebaseAuth.signOut()
                Log.d("Clear Database", "Clearing finish, sign outs ")

            }

            //clearDatabaseJob.await()
            signOutJob.await()
            isLoading.value = false
        }
    }

    fun toggleInfoDialog(showDialog : Boolean ){
        _showInfoDialog.value = showDialog
    }

    fun onSendFeedback() {
        //TODO
    }

}