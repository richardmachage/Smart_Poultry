package com.example.smartpoultry.data.dataSource.room.entities.cells

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CellsDao{

    @Insert
    suspend fun addNewCell(cell: Cells)
    @Delete
    suspend fun deleteCell(cell: Cells)

    @Query("SELECT * FROM cells_tbl")
    fun getAllCells() : Flow<List<Cells>>

    @Query("SELECT * FROM cells_tbl WHERE cellId = :cellId")
    fun getCell(cellId : Int) : Flow<List<Cells>>

    @Query("SELECT SUM('henCount') FROM cells_tbl")
    fun getTotalHenCount(): Flow<Int>

    @Query("SELECT * FROM cells_tbl WHERE blockId = :blockId")
    fun getCellsForABLock(blockId:Int):Flow<List<Cells>>

    @Query("UPDATE cells_tbl SET cellNum = :cellNum, henCount = :henCount WHERE cellId = :cellId")
    fun updateCellDetails(cellId:Int, cellNum:Int, henCount : Int)
}