package com.example.smartpoultry.data.repositoryImpl

import android.util.Log
import com.example.smartpoultry.data.dataModels.DailyEggCollection
import com.example.smartpoultry.data.dataModels.EggRecordFull
import com.example.smartpoultry.data.dataSource.datastore.PreferencesRepo
import com.example.smartpoultry.data.dataSource.remote.firebase.EGGS_COLLECTION
import com.example.smartpoultry.data.dataSource.remote.firebase.FARMS_COLLECTION
import com.example.smartpoultry.data.dataSource.remote.firebase.models.EggCollectionFb
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollection
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollectionDao
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import com.example.smartpoultry.utils.FARM_ID_KEY
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject

class EggCollectionRepositoryImpl @Inject constructor(
    private val eggCollectionDao: EggCollectionDao,
    private val fireStoreDb: FirebaseFirestore,
    //dataStore: AppDataStore,
    private val preferencesRepo: PreferencesRepo
) : EggCollectionRepository {

    init {
        listenForFireStoreChanges()
    }
    override  fun listenForFireStoreChanges() {
        //fireStoreDb.collection(eggsCollectionPath.path)
        val farmsCollection = fireStoreDb.collection(FARMS_COLLECTION)
        val farmDocument: DocumentReference = farmsCollection.document(getFarmId())
        val eggsCollection: CollectionReference = farmDocument.collection(EGGS_COLLECTION)
        eggsCollection
            .addSnapshotListener { querySnapShot, exception ->

                if (exception != null) { //f an error exists, it logs the error and returns early from the listener.
                    Log.w("Error", "Listen failed.", exception)
                    return@addSnapshotListener
                }

                for (docChange in querySnapShot!!.documentChanges) {
                    val eggCollection = docChange.document.toObject(EggCollectionFb::class.java)

                    when (docChange.type) {
                        DocumentChange.Type.ADDED -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {

                                    eggCollectionDao.insertCollectionRecord(
                                        EggCollection(
                                            //eggCollection.productionId,
                                            date = Date(eggCollection.date.time), //eggCollection.date,
                                            cellId = eggCollection.cellId,
                                            eggCount = eggCollection.eggCount,
                                            henCount = eggCollection.henCount
                                        )
                                    )
                                } catch (e: Exception) {
                                    // Log.i("Error : ", "record with date ")
                                }
                            }
                        }

                        DocumentChange.Type.MODIFIED -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    eggCollectionDao.updateCollectionRecord(
                                        EggCollection(
                                            eggCollection.productionId,
                                            date = Date(eggCollection.date.time),
                                            eggCollection.cellId,
                                            eggCollection.eggCount,
                                            eggCollection.henCount
                                        )
                                    )
                                } catch (e: Exception) {
                                    //Log.i("error","record with date exists")
                                }
                            }
                        }

                        DocumentChange.Type.REMOVED -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                eggCollectionDao.deleteCollectionRecord(
                                    eggCollection.productionId
                                )
                            }
                        }
                    }
                }
            }
    }

    override suspend fun fetchAndUpdateEggRecords() {
        try {
            val farmsCollection = fireStoreDb.collection(FARMS_COLLECTION)
            val farmDocument: DocumentReference = farmsCollection.document(getFarmId())
            val eggsCollection: CollectionReference = farmDocument.collection(EGGS_COLLECTION)
            eggsCollection.get()
                .addOnSuccessListener { querySnapShot ->
                    if (!querySnapShot.isEmpty) {
                        val listOfEggCollections =
                            querySnapShot.documents.map { it.toObject(EggCollection::class.java)!! }
                        GlobalScope.launch {
                            //insert the egg collection records to room
                            eggCollectionDao.insertAll(listOfEggCollections)
                        }
                    }
                }
                .addOnFailureListener { Throwable(it) }
        } catch (e: Exception) {
            //todo handle exception
            Log.e("FirestoreFetch", "Error fetching egg records: ", e)
        }
    }

    override suspend fun addNewRecord(eggCollection: EggCollection): Boolean {
        var insertStatus = true
        try {

            val recordId = eggCollectionDao.insertCollectionRecord(eggCollection)
            //fireStoreDb.collection(eggsCollectionPath.path)

            //now update remote
            val farmsCollection = fireStoreDb.collection(FARMS_COLLECTION)
            val farmDocument = farmsCollection.document(getFarmId())
            val eggsCollection = farmDocument.collection(EGGS_COLLECTION)
            eggsCollection.document(recordId.toString())
                .set(
                    EggCollection(
                        productionId = recordId.toInt(),
                        date = eggCollection.date,
                        cellId = eggCollection.cellId,
                        eggCount = eggCollection.eggCount,
                        henCount = eggCollection.henCount

                    )
                )
        } catch (e: Exception) {
            insertStatus = false
        }
        return insertStatus
    }

    override suspend fun deleteRecord(recordId: Int) {
        eggCollectionDao.deleteCollectionRecord(recordId)
        //fireStoreDb.collection(eggsCollectionPath.path)
        val farmsCollection = fireStoreDb.collection(FARMS_COLLECTION)
        val farmDocument: DocumentReference = farmsCollection.document(getFarmId())
        val eggsCollection: CollectionReference = farmDocument.collection(EGGS_COLLECTION)
        eggsCollection.document(recordId.toString())
            .delete()
    }

    override fun getAllRecords(): Flow<List<EggCollection>> {
        return eggCollectionDao.getAllCollectionRecords()
    }

    override fun getRecord(date: Date): Flow<List<EggCollection>> {
        return eggCollectionDao.getCollectionRecord(date)
    }

    override fun getCollectionsBetween(startDate: Date, endDate: Date): Flow<List<EggCollection>> {
        return eggCollectionDao.getCollectionRecordsBetween(startDate, endDate)
    }

    override fun getAllRecordsForCell(cellId: Int): Flow<List<EggCollection>> {
        return eggCollectionDao.getAllRecordsForCell(cellId)
    }

    override fun getRecordsForCellBetween(
        cellId: Int,
        startDate: Date,
        endDate: Date
    ): Flow<List<EggCollection>> {
        return eggCollectionDao.getRecordsForCellBetween(cellId, startDate, endDate)
    }

    override fun getRecentEggCollectionRecords(startDate: Date): Flow<List<DailyEggCollection>> {
        return eggCollectionDao.getRecentEggCollectionRecords(startDate)
    }

    override fun getCellEggCollectionForPastDays(
        cellId: Int,
        startDate: Date
    ): Flow<List<EggCollection>> {
        return eggCollectionDao.getCellEggCollectionForPastDays(cellId, startDate)
    }

    override fun getCellCollectionByMonth(
        cellId: Int,
        yearMonth: String
    ): Flow<List<EggCollection>> {
        return eggCollectionDao.getCellCollectionByMonth(cellId = cellId, yearMonth = yearMonth)
    }

    override fun getBlockEggCollection(blockId: Int): Flow<List<DailyEggCollection>> {
        return eggCollectionDao.getBlockEggCollections(blockId)
    }

    override fun getBlockCollectionByMonth(
        blockId: Int,
        yearMonth: String
    ): Flow<List<DailyEggCollection>> {
        return eggCollectionDao.getBlockCollectionByMonth(blockId = blockId, yearMonth = yearMonth)
    }

    override fun getBlockCollectionsBetweenDates(
        blockId: Int,
        startDate: Date,
        endDate: Date
    ): Flow<List<DailyEggCollection>> {
        return eggCollectionDao.getBlockEggCollectionsBetweenDates(blockId, startDate, endDate)
    }

    override fun getBlockEggCollectionForPastDays(
        blockId: Int,
        startDate: Date
    ): Flow<List<DailyEggCollection>> {
        return eggCollectionDao.getBlockEggCollectionsForPastDays(blockId, startDate)
    }

    override fun getOverallCollectionForPAstDays(startDate: Date): Flow<List<DailyEggCollection>> {
        return eggCollectionDao.getOverallCollectionForPAstDays(startDate)
    }

    override fun getAllFullEggCollection(): Flow<List<EggRecordFull>> {
        return eggCollectionDao.getEggRecordsFull()
    }

    override fun getOverallCollectionBetweenDates(
        startDate: Date,
        endDate: Date
    ): Flow<List<DailyEggCollection>> {
        return eggCollectionDao.getOverallCollectionBetweenDates(startDate, endDate)
    }

    override fun getOverallCollectionByMonth(yearMonth: String): Flow<List<DailyEggCollection>> {
        return eggCollectionDao.getOverallCollectionByMonth(yearMonth = yearMonth)
    }

    private fun getFarmId() = preferencesRepo.loadData(FARM_ID_KEY)!!
}