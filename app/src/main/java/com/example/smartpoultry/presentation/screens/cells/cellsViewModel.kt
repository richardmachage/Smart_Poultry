package com.example.smartpoultry.presentation.screens.cells

import androidx.lifecycle.ViewModel
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.example.smartpoultry.domain.repository.CellsRepository

class cellsViewModel (
    private val cellsRepository: CellsRepository,
    private val blocksRepository: BlocksRepository,
): ViewModel() {
}