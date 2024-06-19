package com.example.smartpoultry.presentation.screens.home

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataModels.DailyEggCollection
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.datastore.PreferencesRepo
import com.example.smartpoultry.domain.reports.Report
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import com.example.smartpoultry.domain.repository.FirebaseAuthRepository
import com.example.smartpoultry.utils.IS_PASSWORD_RESET_KEY
import com.example.smartpoultry.utils.USER_EMAIL_KEY
import com.example.smartpoultry.utils.USER_NAME_KEY
import com.example.smartpoultry.utils.USER_ROLE_KEY
import com.example.smartpoultry.utils.localDateToJavaDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    val blocksRepository: BlocksRepository,
    val cellsRepository: CellsRepository,
    val eggCollectionRepository: EggCollectionRepository,
    private val report: Report,
    val dataStore: AppDataStore,
    val preferencesRepo: PreferencesRepo,
    val firebaseAuthRepository: FirebaseAuthRepository
    // @ApplicationContext val context: Context
) : ViewModel() {

    var farmName = mutableStateOf("")
    var passwordReset = mutableStateOf("")
    var isLoading by  mutableStateOf(false)
    var toastMessage by mutableStateOf("")
    var navigateToLogin by mutableStateOf("")
    var isLoadingText by mutableStateOf("")
    init {
        viewModelScope.launch {
             //   getFarmName()
        }
        passwordReset.value = preferencesRepo.loadData(IS_PASSWORD_RESET_KEY)?:""
    }

    suspend fun syncWithRemote(){
        //later on you should check first if it does not match the "previousFarmId"
        isLoadingText = "Loading"
        isLoading = true
        isLoadingText = "Syncing blocks data..."
        blocksRepository.fetchAndUpdateBlocks()
        isLoadingText = "Syncing Cells data.."
        cellsRepository.fetchAndUpdateCells()
        isLoadingText = "Syncing Egg collection records.."
        eggCollectionRepository.fetchAndUpdateEggRecords()
        isLoading = false
    }

    fun onPasswordReset(email : String){
        if (email.isNotEmpty()){
            viewModelScope.launch {
                isLoading = true
                val result = firebaseAuthRepository.resetPassword(email)

                result.onSuccess {
                    //set the isPassword reset value to true
                    viewModelScope.launch {
                        firebaseAuthRepository.updateIsPasswordChanged()
                        toastMessage = "Password rest link has been sent to your email"

                        //Log Out
                        firebaseAuthRepository.logOut()

                        //then navigate to login
                        navigateToLogin = "yes"

                    }
                }
                result.onFailure {
                    Log.d("error", it.message.toString())
                }
            }
        }
    }
    private fun getFarmName() {
        viewModelScope.launch {
            farmName.value = firebaseAuthRepository.getFarm()
        }
    }

    /*val userRole = dataStore.readData(USER_ROLE_KEY).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ""
    )*/
    fun getUserRole() = preferencesRepo.loadData(USER_ROLE_KEY)

    /*val userName = dataStore.readData(USER_NAME_KEY).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ""
    )*/
    fun getName() = preferencesRepo.loadData(USER_NAME_KEY)
    fun getEmail() = preferencesRepo.loadData(USER_EMAIL_KEY)

    val totalBlocks = blocksRepository.getAllBlocks().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    val totalCells = cellsRepository.getAllCells().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun getOverallCollectionsForPastDays(days: Int): Flow<List<DailyEggCollection>> {
        return eggCollectionRepository.getOverallCollectionForPAstDays(
            startDate = Date(
                localDateToJavaDate(
                    getDateDaysAgo(days)
                )
            )
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList(),
        )
    }


    @SuppressLint("SimpleDateFormat")
    fun onCreateReport(name: String, content: String, reportType: String) {
        report.createAndSavePDF(name, content, reportType)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateDaysAgo(numberOfDays: Int): LocalDate {
        return LocalDate.now().minusDays(numberOfDays.toLong())
    }


}