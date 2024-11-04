package com.forsythe.smartpoultry.data.dataSource.local.room.entities.cells

import androidx.paging.PagingSource
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

    @Query("SELECT SUM('henCount') FROM cells_tbl WHERE blockId = :blockId")
    fun getTotalHenCount(blockId : Int): Flow<Int>

    @Query("SELECT * FROM cells_tbl WHERE blockId = :blockId ORDER BY cellNum ASC")
    //fun getCellsForABLock(blockId:Int):Flow<List<Cells>>
    fun getCellsForABLock(blockId: Int):PagingSource<Int, Cells>

    @Update
    suspend fun updateCellInfo(cells: Cells)

}