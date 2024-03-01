package com.example.smartpoultry.data.repositoryImpl

import android.util.Log
import com.example.smartpoultry.data.dataSource.room.entities.blocks.Blocks
import com.example.smartpoultry.data.dataSource.room.entities.blocks.BlocksDao
import com.example.smartpoultry.data.dataSource.room.relations.BlocksWithCells
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BlocksRepositoryImpl @Inject constructor(
    private val blocksDao: BlocksDao,
    private val fireStoreDB : FirebaseFirestore
) : BlocksRepository {
    init {
        listenForFireStoreChanges()
    }
    private fun listenForFireStoreChanges() {
        fireStoreDB.collection("Blocks").addSnapshotListener{
                querySnapshot, exception ->

            if (exception != null){ //f an error exists, it logs the error and returns early from the listener.
                Log.w("Error","Listen failed.", exception)

            }


            for (docChange in querySnapshot!!.documentChanges){
                val block = docChange.document.toObject(Blocks::class.java) //For each change, it converts the document to a Blocks object

                when (docChange.type){
                    DocumentChange.Type.ADDED,
                    DocumentChange.Type.MODIFIED ->{
                        CoroutineScope(Dispatchers.IO).launch {
                            blocksDao.addNewBlock(block)

                        }
                    }
                    DocumentChange.Type.REMOVED -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            blocksDao.deleteBlock(block)
                        }
                    }
                }
            }
        }
    }
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
         blocksDao.deleteBlock(block = block)
        fireStoreDB
            .collection("Blocks")
            .document(block.blockId.toString())
            .delete()
            .addOnSuccessListener {

            }
            .addOnFailureListener{

            }
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
