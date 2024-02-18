package com.example.smartpoultry.presentation.screens.cells

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.smartpoultry.data.dataSource.room.relations.BlocksWithCells
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class cellsViewModel @Inject constructor(
    private val cellsRepository: CellsRepository,
    private val blocksRepository: BlocksRepository,
): ViewModel() {

    var blocksWithCells = mutableStateListOf<BlocksWithCells>()
}