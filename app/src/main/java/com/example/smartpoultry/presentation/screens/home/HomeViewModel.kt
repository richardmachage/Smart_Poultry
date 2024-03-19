package com.example.smartpoultry.presentation.screens.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.smartpoultry.data.dataModels.DailyEggCollection
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.datastore.USER_ROLE_KEY
import com.example.smartpoultry.domain.reports.Report
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import com.example.smartpoultry.domain.workers.AnalysisWorker
import com.example.smartpoultry.utils.localDateToJavaDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.sql.Date
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    val blocksRepository: BlocksRepository,
    val cellsRepository: CellsRepository,
    val eggCollectionRepository: EggCollectionRepository,
    val report: Report,
    val dataStore: AppDataStore,
   // @ApplicationContext val context: Context
) : ViewModel() {


    val userRole = dataStore.readData(USER_ROLE_KEY).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ""
    )

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
    fun getOverallCollectionsForPastDays(days : Int): Flow<List<DailyEggCollection>> {
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

    fun fireWorker(context: Context){
        val workRequest = OneTimeWorkRequestBuilder<AnalysisWorker>().build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            "unique_analysis_work",
            ExistingWorkPolicy.KEEP ,
            workRequest
        )
    }
}