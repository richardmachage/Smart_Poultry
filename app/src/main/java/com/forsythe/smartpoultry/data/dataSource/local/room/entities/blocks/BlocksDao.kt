package com.forsythe.smartpoultry.data.dataSource.local.room.entities.blocks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.forsythe.smartpoultry.data.dataSource.local.room.relations.BlocksWithCells
import kotlinx.coroutines.flow.Flow

@Dao
interface BlocksDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(block: List<Blocks>)

    @Upsert
    suspend fun addNewBlock(block : Blocks) : Long

    @Delete
    suspend fun deleteBlock(block : Blocks)

    @Query("SELECT * FROM blocks_tbl")
    fun getAllBlocks() : Flow<List<Blocks>>

    @Query("SELECT * FROM blocks_tbl WHERE blockId = :blockId")
    fun getBlock(blockId : Int) : Flow<List<Blocks>>

    @Transaction
    @Query("SELECT * FROM blocks_tbl ")
    fun getBlocksWithCells() : Flow<List<BlocksWithCells>>

    @Query("DELETE FROM cells_tbl WHERE blockId = :blockId")
    suspend fun deleteCellsForBlock(blockId: Int)
}