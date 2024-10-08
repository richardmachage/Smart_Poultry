package com.forsythe.smartpoultry.data.repositoryImpl

import android.util.Log
import com.forsythe.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.blocks.Blocks
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.blocks.BlocksDao
import com.forsythe.smartpoultry.data.dataSource.local.room.relations.BlocksWithCells
import com.forsythe.smartpoultry.domain.repository.BlocksRepository
import com.forsythe.smartpoultry.utils.BLOCKS_COLLECTION
import com.forsythe.smartpoultry.utils.CELLS_COLLECTION
import com.forsythe.smartpoultry.utils.FARMS_COLLECTION
import com.forsythe.smartpoultry.utils.FARM_ID_KEY
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BlocksRepositoryImpl @Inject constructor(
    private val blocksDao: BlocksDao,
    private val fireStoreDB: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    //dataStore: AppDataStore
    //firestorePathProvider: FirestorePathProvider,
    private val preferencesRepo: PreferencesRepo
) : BlocksRepository {

    init {
        firebaseAuth.currentUser?.let { listenForFireStoreChanges() }
    }


    override fun listenForFireStoreChanges() {
        //check if farmId exists
        //val farmId = preferencesRepo.loadData(FARM_ID_KEY) ?: ""

        //if (farmId.isNotBlank()) {
        val farmsCollection = fireStoreDB.collection(FARMS_COLLECTION)
        val farmDocument: DocumentReference = farmsCollection.document(getFarmId())
        val blocksCollection: CollectionReference = farmDocument.collection(BLOCKS_COLLECTION)
        blocksCollection.addSnapshotListener { querySnapshot, exception ->

            if (exception != null) { //if an error exists, it logs the error and returns early from the listener.
                Log.w(
                    "BlocksRepository",
                    "Listening to Firestore changes failed.",
                    exception
                )
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

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun fetchAndUpdateBlocks() {
        try {
            val farmsCollection = fireStoreDB.collection(FARMS_COLLECTION)
            val farmDocument: DocumentReference = farmsCollection.document(getFarmId())
            val blocksCollection: CollectionReference = farmDocument.collection(BLOCKS_COLLECTION)

            blocksCollection.get()
                .addOnSuccessListener { querySnapShot ->
                    if (!querySnapShot.isEmpty) {
                        val listOfBlocks =
                            querySnapShot.documents.map { it.toObject(Blocks::class.java)!! }
                        GlobalScope.launch {
                            //insert the blocks to room
                            blocksDao.insertAll(listOfBlocks)
                        }

                    }
                }
                .addOnFailureListener {
                    Throwable(it)
                }
        } catch (e: Exception) {
            //todo handle exception
            Log.e("FirestoreFetch", "Error fetching blocks: ", e)
        }

    }

    override suspend fun addNewBlock(block: Blocks, isNetAvailble : Boolean): Long {
        //add local storage
        val blockId = blocksDao.addNewBlock(block)

        //then add to remote
        val farmsCollection = fireStoreDB.collection(FARMS_COLLECTION)
        val farmDocument: DocumentReference = farmsCollection.document(getFarmId())
        val blocksCollection: CollectionReference = farmDocument.collection(BLOCKS_COLLECTION)

        if (isNetAvailble){
            blocksCollection
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
                    Log.e("BlocksRepository", "Failed to add block to Firestore.", it)
                    //delete the block in local in case of failure
                    CoroutineScope(Dispatchers.IO).launch {
                        blocksDao.deleteBlock(block)
                    }
                }
                .await()
        }

        else{
            blocksCollection
                .document(blockId.toString())
                .set(
                    Blocks(
                        blockId = blockId.toInt(),
                        blockNum = block.blockNum,
                        totalCells = block.totalCells
                    )
                )
        }

        return blockId
    }

    override suspend fun deleteBlock(block: Blocks) {
        //this should delete the whole block, i.e block from block table and its cells from the cells table
        //first we delete the block from the blocks table(local room)
        blocksDao.deleteBlock(block = block)

        //secondly, delete cells of the block from the cells table(local room)
        blocksDao.deleteCellsForBlock(blockId = block.blockId)

        //then delete the block in the remote data source to allow for synchronization
        //fireStoreDB.collection(blocksCollectionPath.path)
        val farmsCollection = fireStoreDB.collection(FARMS_COLLECTION)
        val farmDocument: DocumentReference = farmsCollection.document(getFarmId())
        val blocksCollection: CollectionReference = farmDocument.collection(BLOCKS_COLLECTION)

        blocksCollection
            .document(block.blockId.toString())
            .delete()
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }

        //Then delete the cells of the block as well in the cells collection
        //fireStoreDB.collection(cellsCollectionPath.path)

        val cellsCollection: CollectionReference = farmDocument.collection(CELLS_COLLECTION)

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

    private fun getFarmId() = preferencesRepo.loadData(FARM_ID_KEY)!!
}
