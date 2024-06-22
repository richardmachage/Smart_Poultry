package com.example.smartpoultry.presentation.screens.cells

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.example.smartpoultry.data.dataSource.local.room.entities.cells.Cells
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.utils.USER_ROLE_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CellsViewModel @Inject constructor(
    private val cellsRepository: CellsRepository,
    private val blocksRepository: BlocksRepository,
    private val preferencesRepo: PreferencesRepo
) : ViewModel() {

    /*val userRole = dataStore.readData(USER_ROLE_KEY).stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        ""
    )*/

    var showDialog = mutableStateOf(false)
    lateinit var selectedCell: Cells

    //dialog inputs
    var cellNumText = mutableStateOf("")
    var henCountText = mutableStateOf("")
    fun getCellsForBLock(blockId: Int): StateFlow<List<Cells>> {
        return cellsRepository.getCellsForBlock(blockId).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList(),
        )
    }

    fun getUserRole() = preferencesRepo.loadData(USER_ROLE_KEY)!!
    fun setTheSelectedCell(cell: Cells) {
        selectedCell = cell
    }

    fun updateCellInfo(cell: Cells){
        viewModelScope.launch {
            cellsRepository.updateCellInfo(cell)
        }
    }

    fun onDeleteCell(cell: Cells){
        viewModelScope.launch {
            cellsRepository.deleteCell(cell)
        }
    }

    fun onAddNewCell(cell : Cells){
        viewModelScope.launch{
            cellsRepository.addNewCell(cell)
        }
    }

}