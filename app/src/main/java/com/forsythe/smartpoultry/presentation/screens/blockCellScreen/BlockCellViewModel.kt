package com.forsythe.smartpoultry.presentation.screens.blockCellScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forsythe.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.blocks.Blocks
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.cells.Cells
import com.forsythe.smartpoultry.domain.repository.BlocksRepository
import com.forsythe.smartpoultry.domain.repository.CellsRepository
import com.forsythe.smartpoultry.presentation.uiModels.BlockItem
import com.forsythe.smartpoultry.utils.MANAGE_BLOCKS_CELLS_ACCESS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel //Hey we wanna inject dependencies using dagger hilt
class BlockCellViewModel @Inject constructor(
    private val blocksRepository: BlocksRepository,
    private val cellsRepository: CellsRepository,
    private val preferencesRepo: PreferencesRepo
) : ViewModel() {

    /*val userRole = dataStore.readData(USER_ROLE_KEY).stateIn(
        viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ""
    )*/
    //exposing flow as state
    val listOfBlocks = blocksRepository.getAllBlocks().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )

    val listOfBlocksWithCells = blocksRepository.getBlocksWithCells().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    var showDialog = mutableStateOf(false)

    //for managing input text to dialog
    var blockNumText = mutableStateOf("")
    var cellsText = mutableStateOf("")

    //fun getUserRole() = preferencesRepo.loadData(USER_ROLE_KEY)!!
    fun getManageBlockCellsAccess() =
        preferencesRepo.loadData(MANAGE_BLOCKS_CELLS_ACCESS).toBoolean()

    fun onAddNewBlock(blockItem: BlockItem, isNetAvailable: Boolean) {
        viewModelScope.launch {
            //Create the new Block first
            val blockId = blocksRepository.addNewBlock(
                block = Blocks(
                    blockNum = blockItem.blockNum,
                    totalCells = blockItem.numberOfCells
                ),
                isNetAvailable = isNetAvailable
            )

            //Then also create the cells for that block
            for (cell in 1..blockItem.numberOfCells) {
                cellsRepository.addNewCell(
                    Cells(
                        blockId = blockId.toInt(),
                        cellNum = cell
                    )
                )
            }


        }
    }

    fun onDeleteBlock(block: Blocks) {

        viewModelScope.launch {
            blocksRepository.deleteBlock(block = block)
        }
    }

    fun clearTextFields() {
        blockNumText.value = ""
        cellsText.value = ""
    }
}