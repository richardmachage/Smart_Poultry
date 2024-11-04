package com.forsythe.smartpoultry.data.repositoryImpl

import android.util.Log
import androidx.paging.PagingSource
import com.forsythe.smartpoultry.data.dataModels.DailyEggCollection
import com.forsythe.smartpoultry.data.dataModels.EggRecordFull
import com.forsythe.smartpoultry.data.dataSource.local.datastore.PreferencesRepo
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.eggCollection.EggCollection
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.eggCollection.EggCollectionDao
import com.forsythe.smartpoultry.data.dataSource.remote.firebase.models.EggCollectionFb
import com.forsythe.smartpoultry.domain.repository.EggCollectionRepository
import com.forsythe.smartpoultry.utils.EGGS_COLLECTION
import com.forsythe.smartpoultry.utils.FARMS_COLLECTION
import com.forsythe.smartpoultry.utils.FARM_ID_KEY
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.sql.Date
import javax.inject.Inject

class EggCollectionRepositoryImpl @Inject constructor(
    private val eggCollectionDao: EggCollectionDao,
    private val fireStoreDb: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val preferencesRepo: PreferencesRepo
) : EggCollectionRepository {

    init {
        firebaseAuth.currentUser?.let { listenForFireStoreChanges() }
    }
    override  fun listenForFireStoreChanges() {
        //fireStoreDb.collection(eggsCollectionPath.path)
        val farmsCollection = fireStoreDb.collection(FARMS_COLLECTION)
        val farmDocument: DocumentReference = farmsCollection.document(getFarmId())
        val eggsCollection: CollectionReference = farmDocument.collection(EGGS_COLLECTION)
        eggsCollection
            .addSnapshotListener { querySnapShot, exception ->

                if (exception != null) { //if an error exists, it logs the error and returns early from the listener.
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

    override suspend fun addNewRecord(eggCollection: EggCollection, isNetworkAvailable : Boolean): AddRecordResult {
        var result = AddRecordResult()
        return try {
            // First insert to Local DB to get record Id
            val recordId = eggCollectionDao.insertCollectionRecord(eggCollection)

            //asertain networkconnection
            if (isNetworkAvailable) {
                // Device is online
                val farmsCollection = fireStoreDb.collection(FARMS_COLLECTION)
                val farmDocument = farmsCollection.document(getFarmId())
                val eggsCollectionRef: CollectionReference =
                    farmDocument.collection(EGGS_COLLECTION)

                eggsCollectionRef
                    .document(recordId.toString())
                    .set(
                        EggCollection(
                            productionId = recordId.toInt(),
                            date = eggCollection.date,
                            cellId = eggCollection.cellId,
                            eggCount = eggCollection.eggCount,
                            henCount = eggCollection.henCount
                        )
                    )
                    .addOnSuccessListener {
                        result = result.copy(
                            isSuccess = true,
                            message =  "Record added successfully"
                        )

                    }
                    .addOnFailureListener {
                        Log.d(
                            "add new record",
                            "Failed to add record to firebase ${it.message}"
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            eggCollectionDao.deleteCollectionRecord(recordId.toInt())
                        }

                        result = result.copy(
                            isSuccess = false,
                            message ="Failed : ${it.localizedMessage}"
                        )
                    }
                    .await()
                return result
            }
            else{
                // Device is offline: Update local storage and assume success
                //proceed to set, using caching
                val farmsCollection = fireStoreDb.collection(FARMS_COLLECTION)
                val farmDocument = farmsCollection.document(getFarmId())
                val eggsCollectionRef: CollectionReference = farmDocument.collection(EGGS_COLLECTION)

                eggsCollectionRef
                    .document(recordId.toString())
                    .set(
                        EggCollection(
                            productionId = recordId.toInt(),
                            date = eggCollection.date,
                            cellId = eggCollection.cellId,
                            eggCount = eggCollection.eggCount,
                            henCount = eggCollection.henCount
                        )
                    )

                // Assume success since the write will be cached
                result = result.copy(isSuccess = true, message = "Record added locally successfully")
                return result
            }
        }
        catch (exception : Exception){
            result = result.copy(isSuccess = false, message = "Failed: ${exception.message.toString()}")
            return result
        }

    }

    override suspend fun deleteRecord(recordId: Int) {
        eggCollectionDao.deleteCollectionRecord(recordId)
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

    override fun getAllFullEggCollection(): PagingSource<Int, EggRecordFull> {
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

data class AddRecordResult(
    var isSuccess : Boolean = false,
    var message: String = ""
)