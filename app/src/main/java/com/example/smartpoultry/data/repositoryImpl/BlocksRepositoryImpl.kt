package com.example.smartpoultry.data.repositoryImpl

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.smartpoultry.data.dataSource.datastore.AppDataStore
import com.example.smartpoultry.data.dataSource.datastore.FARM_ID_KEY
import com.example.smartpoultry.data.dataSource.remote.firebase.BLOCKS_COLLECTION
import com.example.smartpoultry.data.dataSource.remote.firebase.CELLS_COLLECTION
import com.example.smartpoultry.data.dataSource.remote.firebase.FARMS_COLLECTION
import com.example.smartpoultry.data.dataSource.remote.firebase.FirestorePathProvider
import com.example.smartpoultry.data.dataSource.room.entities.blocks.Blocks
import com.example.smartpoultry.data.dataSource.room.entities.blocks.BlocksDao
import com.example.smartpoultry.data.dataSource.room.relations.BlocksWithCells
import com.example.smartpoultry.domain.repository.BlocksRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class BlocksRepositoryImpl @Inject constructor(
    private val blocksDao: BlocksDao,
    private val fireStoreDB: FirebaseFirestore,
    //dataStore: AppDataStore
    firestorePathProvider: FirestorePathProvider
) : BlocksRepository {
    private var blocksCollection:CollectionReference? = null
    private val farmsCollection = fireStoreDB.collection(FARMS_COLLECTION)
    private val farmDocument = farmsCollection.document("710uve6Bmd25yAXcnPfr")//dataStore.farmID)
    //private val blocksCollection = farmDocument.collection(BLOCKS_COLLECTION)
    private val cellsCollection = farmDocument.collection(CELLS_COLLECTION)
   // private val blocksCollectionPath = fireStoreDB.collection(FARMS_COLLECTION).document(dataStore.farmID).collection(BLOCKS_COLLECTION)
    //private val cellsCollectionPath = fireStoreDB.collection(FARMS_COLLECTION).document(dataStore.farmID).collection(CELLS_COLLECTION)
    init {
        CoroutineScope(Dispatchers.IO).launch {
            firestorePathProvider.blocksCollectionFlow.collect{
                blocksCollection = it
                listenForFireStoreChanges()
            }
        }
    }

    private fun listenForFireStoreChanges() {
        //fireStoreDB.collection(blocksCollectionPath.path)
            blocksCollection?.addSnapshotListener { querySnapshot, exception ->

            if (exception != null) { //if an error exists, it logs the error and returns early from the listener.
                Log.w("BlocksRepository", "Listening to Firestore changes failed.", exception)
                return@addSnapshotListener
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
        //fireStoreDB.collection(blocksCollectionPath.path)
            blocksCollection
            ?.document(blockId.toString())
            ?.set(
                Blocks(
                    blockId = blockId.toInt(),
                    blockNum = block.blockNum,
                    totalCells = block.totalCells
                )
            )
            ?.addOnSuccessListener {

            }
            ?.addOnFailureListener {
                Log.e("BlocksRepository", "Failed to add block to Firestore.", it)
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
        //fireStoreDB.collection(blocksCollectionPath.path)
            blocksCollection
            ?.document(block.blockId.toString())
            ?.delete()
            ?.addOnSuccessListener {

            }
            ?.addOnFailureListener {

            }

        //Then delete the cells of the block as well in the cells collection
        //fireStoreDB.collection(cellsCollectionPath.path)
            cellsCollection
            .whereEqualTo("blockId", block.blockId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                Log.i("Firebase", "query successful")

                fireStoreDB.runBatch { batch ->
                    querySnapshot.documents.forEach { document ->
                       // Log.i("Firebase", "deleting cell ${document.id}")
                        batch.delete(cellsCollection.document(document.id))
                    }
                }.addOnCompleteListener {

                }
            }
            .addOnFailureListener {
                //Log.i("Firebase", "query failed")

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
