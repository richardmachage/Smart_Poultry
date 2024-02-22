package com.example.smartpoultry.presentation.screens.eggCollection

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollection
import com.example.smartpoultry.data.dataSource.room.relations.BlocksWithCells
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import com.example.smartpoultry.presentation.uiModels.BlockEggCollection
import com.example.smartpoultry.presentation.uiModels.CellEggCollection
import com.example.smartpoultry.utils.localDateToJavaDate
import com.example.smartpoultry.utils.myDateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EggScreenViewModel @Inject constructor(
    private val blocksRepository: BlocksRepository,
    private val cellsRepository: CellsRepository,
    private val eggCollectionRepository: EggCollectionRepository
) : ViewModel() {
    val getAllBlocks = blocksRepository.getBlocksWithCells().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )

    var isLoading = mutableStateOf(false)
    var insertStatus = mutableStateOf(true)
    var myInputBlocks = mutableStateListOf<BlockEggCollection>()
        private set


    init {
        viewModelScope.launch {
            getAllBlocks.collect {
                myInputBlocks.clear()
                myInputBlocks.addAll(transformBlocksIntoBlocksForInput(it))
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    var selectedDate = mutableStateOf(LocalDate.now())
        private set
    private lateinit var chosenDateValue :Date

    @RequiresApi(Build.VERSION_CODES.O)
    fun setChosenDateValue(localDate: LocalDate){
        chosenDateValue = Date(localDateToJavaDate(localDate))
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun setSelectedDate(date: LocalDate) {
        selectedDate.value = date
    }

    private fun transformCellsEntityToCellsWithEggCount(listOfCells: List<Cells>): List<CellEggCollection> {
        return listOfCells.map { cell ->
            CellEggCollection(
                cellId = cell.cellId,
                cellNum = cell.cellNum, //listOfCells.indexOf(cell) + 1,
                eggCount = 0,
                henCount = cell.henCount
            )
        }
    }

    private fun transformBlocksIntoBlocksForInput(listOfBlocks: List<BlocksWithCells>): List<BlockEggCollection> {
        return listOfBlocks.map { block ->
            BlockEggCollection(
                blockId = block.block.blockId,
                blockNum = listOfBlocks.indexOf(block) + 1,
                cells = transformCellsEntityToCellsWithEggCount(block.cell)
            )
        }
    }

    fun updateEggCount(blockIndex: Int, cellIndex: Int, newEggCount: Int) {
        val updatedCells = myInputBlocks[blockIndex].cells.toMutableList()
        updatedCells[cellIndex] = updatedCells[cellIndex].copy(eggCount = newEggCount)

        val updatedBlock = myInputBlocks[blockIndex].copy(cells = updatedCells)
        myInputBlocks[blockIndex] = updatedBlock // This triggers recomposition
    }

    fun delayApp(time : Long){
        viewModelScope.launch {
            delay(time)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun onSaveRecord(block:Int, cellsInput : List<CellEggCollection>){
        viewModelScope.launch {

            run loop@{
                cellsInput.forEach{ record ->
                    if(
                        eggCollectionRepository.addNewRecord(EggCollection(
                            date = chosenDateValue,//Date.valueOf(selectedDate.value.toString()), //Date.valueOf(myDateFormatter(selectedDate.value)),
                            cellId = record.cellId,
                            eggCount = record.eggCount,
                            henCount = record.henCount
                        ))){
                        insertStatus.value = true
                    }else {
                        insertStatus.value = false
                        return@loop
                    }
                }
            }

        }
    }

}