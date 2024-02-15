package com.example.smartpoultry.data.repositoryImpl

import com.example.smartpoultry.data.dataSource.room.entities.blocks.Blocks
import com.example.smartpoultry.data.dataSource.room.entities.blocks.BlocksDao
import com.example.smartpoultry.domain.repository.BlocksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BlocksRepositoryImpl @Inject constructor(
    private val blocksDao: BlocksDao
) : BlocksRepository {
    override suspend fun addNewBlock(block: Blocks) : Long {
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
