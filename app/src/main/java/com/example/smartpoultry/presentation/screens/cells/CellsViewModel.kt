package com.example.smartpoultry.presentation.screens.cells

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartpoultry.data.dataSource.room.relations.BlocksWithCells
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CellsViewModel @Inject constructor(
    private val cellsRepository: CellsRepository,
    private val blocksRepository: BlocksRepository,
): ViewModel() {

    var blocksWithCells = mutableStateListOf<BlocksWithCells>()

    init{
        viewModelScope.launch {
            blocksRepository.getBlocksWithCells().collect{
                blocksWithCells.addAll(it)
            }
        }
    }

}