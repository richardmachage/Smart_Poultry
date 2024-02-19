package com.example.smartpoultry.domain.repository

import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import kotlinx.coroutines.flow.Flow

interface CellsRepository {

    suspend fun addNewCell(cell: Cells)

    suspend fun deleteCell(cell: Cells)

    fun getAllCells() : Flow<List<Cells>>

    fun getCell(cellId:Int): Flow<List<Cells>>

    fun getTotalHenCount():Flow<Int>

    fun getCellsForBlock(blockId : Int):Flow<List<Cells>>
}