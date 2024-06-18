package com.example.smartpoultry.data.repositoryImpl

import android.util.Log
import com.example.smartpoultry.data.dataSource.datastore.PreferencesRepo
import com.example.smartpoultry.data.dataSource.remote.firebase.CELLS_COLLECTION
import com.example.smartpoultry.data.dataSource.remote.firebase.FARMS_COLLECTION
import com.example.smartpoultry.data.dataSource.remote.firebase.models.Cell
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.room.entities.cells.CellsDao
import com.example.smartpoultry.domain.repository.CellsRepository
import com.example.smartpoultry.utils.FARM_ID_KEY
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CellsRepositoryImpl @Inject constructor(
    private val cellsDao: CellsDao,
    private val fireStoreDb: FirebaseFirestore,
    private val preferencesRepo: PreferencesRepo,
    private val firebaseAuth: FirebaseAuth
) : CellsRepository {

    init {
        firebaseAuth.currentUser?.let { listenForFireStoreChanges() }
    }


    override fun listenForFireStoreChanges() {
        //check if farmId exists
        //  val farmId = preferencesRepo.loadData(FARM_ID_KEY) ?: ""

        //if (farmId.isNotBlank()) {
        val farmsCollection = fireStoreDb.collection(FARMS_COLLECTION)
        val farmDocument: DocumentReference = farmsCollection.document(getFarmId())
        val cellsCollection: CollectionReference = farmDocument.collection(CELLS_COLLECTION)
        cellsCollection.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) { //if an error exists, it logs the error and returns early from the listener.
                Log.w("Error", "Listen failed.", exception)
                return@addSnapshotListener
            }

            for (docChange in querySnapshot!!.documentChanges) {
                val cell =
                    docChange.document.toObject(Cell::class.java) // converting the doc to cell object

                when (docChange.type) {
                    DocumentChange.Type.ADDED -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            cellsDao.addNewCell(
                                Cells(
                                    cellId = cell.cellId,
                                    cellNum = cell.cellNum,
                                    blockId = cell.blockId,
                                    henCount = cell.henCount
                                )
                            )
                        }
                    }

                    DocumentChange.Type.MODIFIED -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            cellsDao.updateCellInfo(
                                Cells(
                                    cellId = cell.cellId,
                                    cellNum = cell.cellNum,
                                    blockId = cell.blockId,
                                    henCount = cell.henCount
                                )
                            )
                        }
                    }

                    DocumentChange.Type.REMOVED -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            cellsDao.deleteCell(
                                Cells(
                                    cellId = cell.cellId,
                                    cellNum = cell.cellNum,
                                    blockId = cell.blockId,
                                    henCount = cell.henCount
                                )
                            )
                        }
                    }

                }
            }
        }
        //}

        /*else {
            // to retrieve farm Id first here
            fireStoreDb.collection(USERS_COLLECTION)
                .document(firebaseAuth.currentUser?.uid.toString())
                .get()
                .addOnSuccessListener { docSnapshot ->
                    val user = docSnapshot.toObject(User::class.java)
                    user?.let {
                        //store the id in preferences
                        preferencesRepo.saveData(FARM_ID_KEY, it.farmId)
                        val farmID = preferencesRepo.loadData(FARM_ID_KEY) ?: ""
                        val farmDoc = farmsCollection.document(farmID)
                        val cellsColle = farmDoc.collection(CELLS_COLLECTION)

                        cellsColle.addSnapshotListener { querySnapshot, exception ->
                            if (exception != null) { //if an error exists, it logs the error and returns early from the listener.
                                Log.w("Error", "Listen failed.", exception)
                                return@addSnapshotListener
                            }

                            for (docChange in querySnapshot!!.documentChanges) {
                                val cell =
                                    docChange.document.toObject(Cell::class.java) // converting the doc to cell object

                                when (docChange.type) {
                                    DocumentChange.Type.ADDED -> {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            cellsDao.addNewCell(
                                                Cells(
                                                    cellId = cell.cellId,
                                                    cellNum = cell.cellNum,
                                                    blockId = cell.blockId,
                                                    henCount = cell.henCount
                                                )
                                            )
                                        }
                                    }

                                    DocumentChange.Type.MODIFIED -> {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            cellsDao.updateCellInfo(
                                                Cells(
                                                    cellId = cell.cellId,
                                                    cellNum = cell.cellNum,
                                                    blockId = cell.blockId,
                                                    henCount = cell.henCount
                                                )
                                            )
                                        }
                                    }

                                    DocumentChange.Type.REMOVED -> {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            cellsDao.deleteCell(
                                                Cells(
                                                    cellId = cell.cellId,
                                                    cellNum = cell.cellNum,
                                                    blockId = cell.blockId,
                                                    henCount = cell.henCount
                                                )
                                            )
                                        }
                                    }

                                }
                            }
                        }

                    }

                }

        }*/
    }

    override suspend fun addNewCell(cell: Cells) {
        val cellId = cellsDao.addNewCell(cell = cell)
        //fireStoreDb.collection(cellsCollectionPath.path)
        val farmsCollection = fireStoreDb.collection(FARMS_COLLECTION)
        val farmDocument: DocumentReference = farmsCollection.document(getFarmId())
        val cellsCollection: CollectionReference = farmDocument.collection(CELLS_COLLECTION)
        cellsCollection.document(cellId.toString())
            .set(
                Cell(
                    cellId = cellId.toInt(),
                    blockId = cell.blockId,
                    cellNum = cell.cellNum,
                    henCount = cell.henCount
                )
            )
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }

    override suspend fun deleteCell(cell: Cells) {

        cellsDao.deleteCell(cell = cell)
        //fireStoreDb.collection(cellsCollectionPath.path)
        val farmsCollection = fireStoreDb.collection(FARMS_COLLECTION)
        val farmDocument: DocumentReference = farmsCollection.document(getFarmId())
        val cellsCollection: CollectionReference = farmDocument.collection(CELLS_COLLECTION)
        cellsCollection.document(cell.cellId.toString())
            .delete()
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }

    override fun getAllCells(): Flow<List<Cells>> {
        return cellsDao.getAllCells()
    }

    override fun getCell(cellId: Int): Flow<List<Cells>> {
        return cellsDao.getCell(cellId = cellId)
    }

    override fun getTotalHenCount(): Flow<List<Int>> {
        return cellsDao.getTotalHenCount()
    }

    override fun getCellsForBlock(blockId: Int): Flow<List<Cells>> {
        return cellsDao.getCellsForABLock(blockId = blockId)
    }

    override suspend fun updateCellInfo(cell: Cells) {
        cellsDao.updateCellInfo(cell)
        // fireStoreDb.collection(cellsCollectionPath.path)
        val farmsCollection = fireStoreDb.collection(FARMS_COLLECTION)
        val farmDocument: DocumentReference = farmsCollection.document(getFarmId())
        val cellsCollection: CollectionReference = farmDocument.collection(CELLS_COLLECTION)
        cellsCollection.document(cell.cellId.toString())
            .set(cell, SetOptions.merge())

    }

    override suspend fun fetchAndUpdateCells() {
        try {
            val farmsCollection = fireStoreDb.collection(FARMS_COLLECTION)
            val farmDocument: DocumentReference = farmsCollection.document(getFarmId())
            val cellsCollection: CollectionReference = farmDocument.collection(CELLS_COLLECTION)

            cellsCollection.get()
                .addOnSuccessListener { querySnapShot ->
                    if (!querySnapShot.isEmpty) {
                        val listOfCells =
                            querySnapShot.documents.map { it.toObject(Cells::class.java)!! }
                        GlobalScope.launch {
                            //insert the blocks to room
                            cellsDao.insertAll(listOfCells)
                        }
                    }
                }
                .addOnFailureListener {
                    Throwable(it)
                }
        } catch (e: Exception) {
            //todo handle exception
            Log.e("FirestoreFetch", "Error fetching Cells: ", e)
        }
    }

    private fun getFarmId() = preferencesRepo.loadData(FARM_ID_KEY)!!
}