package com.example.smartpoultry.domain.repository

import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import kotlinx.coroutines.flow.Flow

interface CellsRepository {

    suspend fun addNewCell(cells: Cells)

    suspend fun deleteCell(cells: Cells)

    fun getAllCells() : Flow<List<Cells>>

    fun getCell(cellId:Int): Flow<List<Cells>>
}