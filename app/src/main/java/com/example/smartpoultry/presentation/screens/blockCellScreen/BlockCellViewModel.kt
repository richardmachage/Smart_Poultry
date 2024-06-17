package com.example.smartpoultry.presentation.screens.blockCellScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.datastore.PreferencesRepo
import com.example.smartpoultry.data.dataSource.room.entities.blocks.Blocks
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.presentation.uiModels.BlockItem
import com.example.smartpoultry.utils.USER_ROLE_KEY
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

    fun getUserRole() = preferencesRepo.loadData(USER_ROLE_KEY)!!

    fun onAddNewBlock(blockItem: BlockItem) {
        viewModelScope.launch {
            //Create the new Block first
            val blockId = blocksRepository.addNewBlock(
                block = Blocks(
                    blockNum = blockItem.blockNum,
                    totalCells = blockItem.numberOfCells
                )
            )

            //Then also create the cells for that block
            for(cell in 1..blockItem.numberOfCells){
                cellsRepository.addNewCell(Cells(
                    blockId = blockId.toInt(),
                    cellNum = cell
                ))
            }


        }
    }

    fun onDeleteBlock(block: Blocks){

        viewModelScope.launch {
            blocksRepository.deleteBlock(block = block)
        }
    }

    fun clearTextFields(){
        blockNumText.value = ""
        cellsText.value = ""
    }
}