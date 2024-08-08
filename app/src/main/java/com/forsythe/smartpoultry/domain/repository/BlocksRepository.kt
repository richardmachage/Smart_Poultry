package com.forsythe.smartpoultry.domain.repository

import com.forsythe.smartpoultry.data.dataSource.local.room.entities.blocks.Blocks
import com.forsythe.smartpoultry.data.dataSource.local.room.relations.BlocksWithCells
import kotlinx.coroutines.flow.Flow

interface BlocksRepository {
    suspend fun addNewBlock(block: Blocks) : Long
    suspend fun deleteBlock(block: Blocks)

    fun getAllBlocks() : Flow<List<Blocks>>
    fun getBlock(block: Blocks) : Flow<List<Blocks>>
    suspend fun fetchAndUpdateBlocks()
    fun getBlocksWithCells() : Flow<List<BlocksWithCells>>
    fun listenForFireStoreChanges()

}