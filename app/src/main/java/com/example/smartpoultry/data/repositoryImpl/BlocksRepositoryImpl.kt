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
    private val fireStoreDB: FirebaseFirestore
) : BlocksRepository {
    init {
        listenForFireStoreChanges()
    }

    private fun listenForFireStoreChanges() {
        fireStoreDB.collection("Blocks").addSnapshotListener { querySnapshot, exception ->

            if (exception != null) { //f an error exists, it logs the error and returns early from the listener.
                Log.w("Error", "Listen failed.", exception)

            }


            for (docChange in querySnapshot!!.documentChanges) {
                val block =
                    docChange.document.toObject(Blocks::class.java) //For each change, it converts the document to a Blocks object

                when (docChange.type) {
                    DocumentChange.Type.ADDED,
                    DocumentChange.Type.MODIFIED -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            blocksDao.addNewBlock(block)

                        }
                    }

                    DocumentChange.Type.REMOVED -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            blocksDao.deleteBlock(block)
                            blocksDao.deleteCellsForBlock(blockId = block.blockId)

                        }
                    }
                }
            }
        }
    }

    override suspend fun addNewBlock(block: Blocks): Long {
        val blockId = blocksDao.addNewBlock(block)
        fireStoreDB
            .collection("Blocks")
            .document(blockId.toString())
            .set(
                Blocks(
                    blockId = blockId.toInt(),
                    blockNum = block.blockNum,
                    totalCells = block.totalCells
                )
            )
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
        return blockId
    }

    override suspend fun deleteBlock(block: Blocks) {
        //this should delete the whole block, i.e block from block table and its cells from the cells table
        //first we delete the block from the blocks table
        blocksDao.deleteBlock(block = block)

        //secondly, delete cells of the block from the cells table
        blocksDao.deleteCellsForBlock(blockId = block.blockId)

        //then delete the block in the remote data source to allow for synchronization
        fireStoreDB
            .collection("Blocks")
            .document(block.blockId.toString())
            .delete()
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }

        //Then delete the cells of the block as well
        Log.i("Firebase", "query firebase")

        fireStoreDB
            .collection("Cells")
            .whereEqualTo("blockId", block.blockId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                Log.i("Firebase", "query successful")

                fireStoreDB.runBatch { batch ->
                    querySnapshot.documents.forEach { document ->
                        Log.i("Firebase", "deleting cell ${document.id}")
                        batch.delete(fireStoreDB.collection("Cells").document(document.id))
                    }
                }.addOnCompleteListener {

                }
            }
            .addOnFailureListener {
                Log.i("Firebase", "query failed")

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
