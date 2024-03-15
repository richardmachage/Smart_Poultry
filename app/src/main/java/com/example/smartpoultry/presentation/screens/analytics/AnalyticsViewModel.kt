package com.example.smartpoultry.presentation.screens.analytics

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataModels.DailyEggCollection
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollection
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import com.example.smartpoultry.utils.localDateToJavaDate
import com.example.smartpoultry.utils.toYearMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.sql.Date
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
     val cellsRepository: CellsRepository,
    private val blocksRepository: BlocksRepository,
    private val eggCollectionRepository: EggCollectionRepository,
) : ViewModel() {
    var plotChart = mutableStateOf(false)
    var selectedBlockId = mutableIntStateOf(0)
    var selectedBlockNum = mutableIntStateOf(0)
    //var selectedBlockNum = mutableIntStateOf(0)
    var selectedCellID = mutableIntStateOf(0)
    var selectedCellNum = mutableIntStateOf(0)
    var selectedYear = mutableStateOf("")
    var selectedMonth = mutableStateOf("")
    var levelOfAnalysis = mutableStateOf("Cell")


    @RequiresApi(Build.VERSION_CODES.O)
    var startDate = mutableStateOf(LocalDate.now())

    @RequiresApi(Build.VERSION_CODES.O)
    var endDate = mutableStateOf(LocalDate.now())

    var isCustomRangeAnalysis = mutableStateOf(false)
    var isPastXDaysAnalysis = mutableStateOf(true)
    var isMonthlyAnalysis = mutableStateOf(false)

    var pastDays = mutableStateOf("")

    val listOfBlocksWithCells = blocksRepository.getBlocksWithCells().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        emptyList(),
    )


    @RequiresApi(Build.VERSION_CODES.O)
    fun getCellCollectionBetweenDates(): Flow<List<EggCollection>> {
        return eggCollectionRepository.getRecordsForCellBetween(
            cellId = selectedCellID.intValue,
            startDate = Date(localDateToJavaDate(startDate.value)),
            endDate = Date(localDateToJavaDate(endDate.value))
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList(),
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCellMonthlyRecords():Flow<List<EggCollection>>{
        return eggCollectionRepository.getCellCollectionByMonth(
            cellId = selectedCellID.intValue,
            yearMonth = toYearMonth( year = selectedYear.value, month = selectedMonth.value)
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList(),
        )
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun  getCellEggCollectionForPastDays() : Flow<List<EggCollection>>{
        return eggCollectionRepository.getCellEggCollectionForPastDays(
            cellId = selectedCellID.intValue,
            startDate = Date(localDateToJavaDate(
                getDateDaysAgo(pastDays.value.toIntOrNull() ?:0)
            ))
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList(),
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getBlockEggCollectionBetweenDates():Flow<List<DailyEggCollection>>{
        return eggCollectionRepository.getBlockCollectionsBetweenDates(
            blockId = selectedBlockId.intValue,
            startDate = Date(localDateToJavaDate(startDate.value)),
            endDate = Date(localDateToJavaDate(endDate.value))
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList(),
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getBlockCollectionsForPastDays(): Flow<List<DailyEggCollection>> {
        return eggCollectionRepository.getBlockEggCollectionForPastDays(
            blockId = selectedBlockId.intValue,
            startDate = Date(localDateToJavaDate(
                getDateDaysAgo(pastDays.value.toIntOrNull() ?:0)
            ))
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList(),
        )
    }

    fun getMonthlyBlockCollections() : Flow<List<DailyEggCollection>>{
        return  eggCollectionRepository.getBlockCollectionByMonth(
            blockId = selectedBlockId.intValue,
            yearMonth = toYearMonth( year = selectedYear.value, month = selectedMonth.value)
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList(),
        )
    }

    fun getMonthlyOverallCollections() : Flow<List<DailyEggCollection>>{
        return eggCollectionRepository.getOverallCollectionByMonth(
            yearMonth = toYearMonth(year = selectedYear.value, month = selectedMonth.value)
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList(),
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getOverallCollectionBetweenDays(): Flow<List<DailyEggCollection>> {
        return eggCollectionRepository.getOverallCollectionBetweenDates(
            startDate = Date(localDateToJavaDate(startDate.value)),
            endDate = Date(localDateToJavaDate(endDate.value))
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList(),
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getOverallCollectionsForPastDays(): Flow<List<DailyEggCollection>> {
        return eggCollectionRepository.getOverallCollectionForPAstDays(
            startDate = Date(
                localDateToJavaDate(
                    getDateDaysAgo(pastDays.value.toIntOrNull() ?: 0)
                )
            )
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList(),
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateDaysAgo(numberOfDays: Int): LocalDate {
        return LocalDate.now().minusDays(numberOfDays.toLong())
    }

}