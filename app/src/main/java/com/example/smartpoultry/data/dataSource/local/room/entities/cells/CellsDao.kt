package com.example.smartpoultry.data.dataSource.local.room.entities.cells

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CellsDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cell : List<Cells>)

    @Upsert
    suspend fun addNewCell(cell: Cells) : Long

    @Delete
    suspend fun deleteCell(cell: Cells)

    @Query("SELECT * FROM cells_tbl")
    fun getAllCells() : Flow<List<Cells>>

    @Query("SELECT * FROM cells_tbl WHERE cellId = :cellId")
    fun getCell(cellId : Int) : Flow<List<Cells>>

    @Query("SELECT SUM('henCount') FROM cells_tbl")
    fun getTotalHenCount(): Flow<List<Int>>

    @Query("SELECT * FROM cells_tbl WHERE blockId = :blockId")
    fun getCellsForABLock(blockId:Int):Flow<List<Cells>>

    @Update
    suspend fun updateCellInfo(cells: Cells)

}