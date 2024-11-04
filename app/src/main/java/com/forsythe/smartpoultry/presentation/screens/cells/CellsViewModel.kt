package com.forsythe.smartpoultry.presentation.screens.cells

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.forsythe.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.cells.Cells
import com.forsythe.smartpoultry.domain.repository.BlocksRepository
import com.forsythe.smartpoultry.domain.repository.CellsRepository
import com.forsythe.smartpoultry.utils.EDIT_HEN_COUNT_ACCESS
import com.forsythe.smartpoultry.utils.MANAGE_BLOCKS_CELLS_ACCESS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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

    /*fun getCellsForBLock(blockId: Int): StateFlow<List<Cells>> {
        return cellsRepository.getCellsForBlock(blockId).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList(),
        )
    }*/

    fun getTotalHenCount(blockId: Int): Flow<Int>{
        return cellsRepository.getTotalHenCount(blockId)
    }

    fun getCellsForBLock(blockId: Int): Flow<PagingData<Cells>>{
        val pager = Pager(
            config = PagingConfig(
                pageSize = 15,
                prefetchDistance = 25
            ),
            pagingSourceFactory = {cellsRepository.getCellsForBlock(blockId)}
        )

        return pager.flow
    }

    //fun getUserRole() = preferencesRepo.loadData(USER_ROLE_KEY)!!
    fun getManageBlockCellsAccess() = preferencesRepo.loadData(MANAGE_BLOCKS_CELLS_ACCESS).toBoolean()
    fun getEditHenCountAccess() = preferencesRepo.loadData(EDIT_HEN_COUNT_ACCESS).toBoolean()

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