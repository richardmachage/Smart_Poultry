package com.example.smartpoultry.presentation.screens.eggCollection

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.room.entities.blocks.Blocks
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollection
import com.example.smartpoultry.data.dataSource.room.relations.BlocksWithCells
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import com.example.smartpoultry.presentation.uiModels.BlockEggCollection
import com.example.smartpoultry.presentation.uiModels.CellEggCollection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate
import java.util.ArrayList
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


    var selectedDate = mutableStateOf(LocalDate.now())
        private set

    var totalEggCount = mutableStateOf("")
        private set

    fun setTotalEggCount(totalEggs: Int) {
        totalEggCount.value = totalEggs.toString()
    }

    fun setSelectedDate(date: LocalDate) {
        selectedDate.value = date
    }

    private fun transformCellsEntityToCellsWithEggCount(listOfCells: List<Cells>): List<CellEggCollection> {
        return listOfCells.map { cell ->
            CellEggCollection(
                cellId = cell.cellId,
                cellNum = listOfCells.indexOf(cell) + 1,
                eggCount = 0
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

    fun onSaveRecord( block:Int, cellsInput : List<CellEggCollection>){
        viewModelScope.launch {
            cellsInput.forEachIndexed{index, record ->
                eggCollectionRepository.addNewRecord(EggCollection(
                    date = Date.valueOf(selectedDate.value.toString()),
                    cellId = record.cellId,
                    eggCount = record.eggCount,
                ))
            }
        }
    }



}