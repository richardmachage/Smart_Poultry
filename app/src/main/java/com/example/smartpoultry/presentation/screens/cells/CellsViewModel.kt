package com.example.smartpoultry.presentation.screens.cells

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CellsViewModel @Inject constructor(
    private val cellsRepository: CellsRepository,
    private val blocksRepository: BlocksRepository,
): ViewModel() {
   lateinit var blockId : State<Int>


    val listOfBlocks = blocksRepository.getAllBlocks().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList(),
    )

    fun getCellsForBLock(blockId: Int): StateFlow<List<Cells>> {

        return cellsRepository.getCellsForBlock(blockId).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList(),
        )
    }

}