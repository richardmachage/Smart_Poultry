package com.example.smartpoultry.presentation.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.domain.reports.Report
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import com.example.smartpoultry.utils.myDateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val report: Report
    ) : ViewModel() {

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

    val eggCollectionRecords = eggCollectionRepository.getRecentEggCollectionRecords(Date(LocalDate.now().toEpochDay())).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList(),
    )

    fun onCreateReport( ){
        var totalCells = 0
        var totalBlocks = 0
        var totalHen = 0
        viewModelScope.launch {
            cellsRepository.getAllCells().collect{
                totalCells = it.size
                totalHen = it.sumOf { cell: Cells -> cell.henCount }
            }
            blocksRepository.getAllBlocks().collect{
                totalBlocks = it.size
            }
        }
        report.createAndSavePDF(
            name = "Inventory ${myDateFormatter(LocalDate.now())}",
            content = "SMART POULTRY INVENTORY " +
                    "\nDate : ${myDateFormatter(LocalDate.now())}" +
                    "\n  " +
                    "\n  " +
                    "\nTotal Blocks : $totalBlocks" +
                    "\nTotal Cells: $totalCells" +
                    "\nTotal Chicken: $totalHen"
        )
    }


}