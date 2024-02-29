package com.example.smartpoultry.data.repositoryImpl

import com.example.smartpoultry.data.dataSource.room.entities.blocks.Blocks
import com.example.smartpoultry.data.dataSource.room.entities.blocks.BlocksDao
import com.example.smartpoultry.data.dataSource.room.relations.BlocksWithCells
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BlocksRepositoryImpl @Inject constructor(
    private val blocksDao: BlocksDao,
    private val fireStoreDB : FirebaseFirestore
) : BlocksRepository {
    override suspend fun addNewBlock(block: Blocks) : Long {
        val blockId = blocksDao.addNewBlock(block)
        fireStoreDB
            .collection("Blocks")
            .document(blockId.toString())
            .set(
                Blocks(blockId = blockId.toInt(),
                    blockNum = block.blockNum,
                    totalCells = block.totalCells)
            )
            .addOnSuccessListener {

            }
            .addOnFailureListener{

            }
        return blockId
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

    override fun getBlocksWithCells(): Flow<List<BlocksWithCells>> {
        return blocksDao.getBlocksWithCells()
    }

}
