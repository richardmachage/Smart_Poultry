package com.example.smartpoultry.data.repository

import com.example.smartpoultry.data.dataSource.room.entities.blocks.Blocks
import com.example.smartpoultry.data.dataSource.room.entities.blocks.BlocksDao
import com.example.smartpoultry.domain.domainModels.Block
import com.example.smartpoultry.domain.repository.BlocksRepository
import kotlinx.coroutines.flow.Flow

class BlocksRepositoryImpl(private val blocksDao: BlocksDao) : BlocksRepository {
    override suspend fun addNewBlock(block: Blocks) {
        return blocksDao.addNewBlock(block)
    }

    override suspend fun deleteBlock(block: Blocks) {
        return blocksDao.deleteBlock(block = block)
    }

    override fun getAllBlocks(): Flow<List<Blocks>> {
        return blocksDao.getAllBlocks()
    }

    override fun getBlock(block: Blocks): Flow<List<Blocks>> {
        return blocksDao.getBlock(block.blockId)
    }


}
