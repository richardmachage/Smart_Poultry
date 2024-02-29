package com.example.smartpoultry.data.dataSource.room.entities.blocks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.smartpoultry.data.dataSource.room.relations.BlocksWithCells
import kotlinx.coroutines.flow.Flow

@Dao
interface BlocksDao{
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
}