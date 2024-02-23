package com.example.smartpoultry.presentation.screens.analytics

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollection
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import com.example.smartpoultry.utils.localDateToJavaDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.sql.Date
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor (
    private val cellsRepository: CellsRepository,
    private val blocksRepository: BlocksRepository,
    private val eggCollectionRepository: EggCollectionRepository,
) : ViewModel() {
    var plotChart = mutableStateOf(false)
    var selectedCellID = mutableIntStateOf(0)
    var startDate = mutableStateOf(LocalDate.now())
    var endDate = mutableStateOf(LocalDate.now())


    val listOfBlocksWithCells = blocksRepository.getBlocksWithCells().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        emptyList(),
    )

   // @RequiresApi(Build.VERSION_CODES.O)
    //var listOfRecords = getCellCollectionBetweenDates()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCellCollectionBetweenDates() : Flow<List<EggCollection>> {
            return eggCollectionRepository.getRecordsForCellBetween(
                cellId = selectedCellID.intValue,
                startDate = Date(localDateToJavaDate( startDate.value)),
                endDate = Date(localDateToJavaDate( endDate.value))
            ).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = emptyList(),
            )

    }

}