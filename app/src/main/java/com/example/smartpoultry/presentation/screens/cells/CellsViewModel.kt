package com.example.smartpoultry.presentation.screens.cells

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.room.relations.BlocksWithCells
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.presentation.uiModels.BlockEggCollection
import com.example.smartpoultry.presentation.uiModels.CellEggCollection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CellsViewModel @Inject constructor(
    private val cellsRepository: CellsRepository,
    private val blocksRepository: BlocksRepository,
): ViewModel() {

    val getAllBlocks = blocksRepository.getBlocksWithCells().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )
    var blocksWithCells = mutableStateListOf<BlockEggCollection>()

    init{
        viewModelScope.launch {
            getAllBlocks.collect{
                blocksWithCells.clear()
                blocksWithCells.addAll(transformBlocksIntoBlocksForInput(it))
            }
        }
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

}