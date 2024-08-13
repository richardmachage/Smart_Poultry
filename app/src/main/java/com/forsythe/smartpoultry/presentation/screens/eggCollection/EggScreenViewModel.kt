package com.forsythe.smartpoultry.presentation.screens.eggCollection

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.cells.Cells
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.eggCollection.EggCollection
import com.forsythe.smartpoultry.data.dataSource.local.room.relations.BlocksWithCells
import com.forsythe.smartpoultry.domain.repository.BlocksRepository
import com.forsythe.smartpoultry.domain.repository.CellsRepository
import com.forsythe.smartpoultry.domain.repository.EggCollectionRepository
import com.forsythe.smartpoultry.presentation.uiModels.BlockEggCollection
import com.forsythe.smartpoultry.presentation.uiModels.CellEggCollection
import com.forsythe.smartpoultry.utils.localDateToJavaDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
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

    var zeroCellEggs : Cells? = null
    var isLoading = mutableStateOf(false)
    var insertStatus = mutableStateOf(true)
    var toastMessage = mutableStateOf("")
    var myInputBlocks = mutableStateListOf<BlockEggCollection>()
        private set

    private var _eggCollectionScreenState by mutableStateOf(EggCollectionScreenState())
    val eggCollectionScreenState: EggCollectionScreenState
        get() = _eggCollectionScreenState


    init {
        setChosenDateValue(LocalDate.now())
        setMyInputBlocks()
    }

    private fun setMyInputBlocks() {
        viewModelScope.async {
            getAllBlocks.collect {
                myInputBlocks.clear()
                myInputBlocks.addAll(transformBlocksIntoBlocksForInput(it))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    var selectedDate = mutableStateOf(LocalDate.now())
        private set

    private lateinit var chosenDateValue: Date

    @RequiresApi(Build.VERSION_CODES.O)
    fun setChosenDateValue(localDate: LocalDate) {
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSaveRecord(block: Int, cellsInput: List<CellEggCollection>, isNetAvailable : Boolean) {
        viewModelScope.launch {
            isLoading.value = true
            run loop@{
                cellsInput.forEachIndexed { _, record ->
                    if (
                        eggCollectionRepository.addNewRecord(
                            EggCollection(
                                date = chosenDateValue,
                                cellId = record.cellId,
                                eggCount = record.eggCount,
                                henCount = record.henCount
                            ),
                            isNetAvailable
                        ).isSuccess
                    ) {
                        insertStatus.value = true
                        //updateEggCount(blockIndex = block, cellIndex = index, newEggCount = 0)

                    } else {
                        insertStatus.value = false
                        // updateEggCount(blockIndex = block, cellIndex = index, newEggCount = 0)
                        return@loop
                    }
                }
            }
            setMyInputBlocks()
            isLoading.value = false
            if (insertStatus.value) toastMessage.value =
                "records for block ${block + 1} saved successfully"
            else toastMessage.value =
                "Failed! Records for Block: ${block + 1} for date: ${selectedDate.value} already exist"
        }
    }

    fun onSaveSingleCellRecord(cell: Cells, eggCount: Int, isNetAvailable : Boolean) {
        viewModelScope.launch {
            isLoading.value = true
            //insertStatus.value
            val result = eggCollectionRepository.addNewRecord(
                EggCollection(
                    date = chosenDateValue,
                    cellId = cell.cellId,
                    eggCount = eggCount,
                    henCount = cell.henCount,
                ),
                isNetAvailable
            )
            isLoading.value = false

            /*toastMessage.value =
                if (insertStatus.value) "saved successfully"
                else "Failed! Records for ${cell.cellNum} on ${selectedDate.value} already exist"*/
            toastMessage.value = result.message
        }
    }

    private fun resetEntries(block: Int, cellsInput: List<CellEggCollection>) {
        cellsInput.forEachIndexed { index, _ ->
            updateEggCount(block, cellIndex = index, newEggCount = 0)
        }
    }

}