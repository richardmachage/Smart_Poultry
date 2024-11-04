package com.forsythe.smartpoultry.domain.repository

import androidx.paging.PagingSource
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.cells.Cells
import kotlinx.coroutines.flow.Flow

interface CellsRepository {

    suspend fun addNewCell(cell: Cells)

    suspend fun deleteCell(cell: Cells)

    fun getAllCells() : Flow<List<Cells>>

    fun getCell(cellId:Int): Flow<List<Cells>>

    fun getTotalHenCount(blockId: Int):Flow<Int>

    fun getCellsForBlock(blockId : Int): PagingSource<Int, Cells>//Flow<List<Cells>>

    suspend fun updateCellInfo(cell: Cells)

    suspend fun fetchAndUpdateCells()

    fun listenForFireStoreChanges()
}