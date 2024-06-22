package com.example.smartpoultry.domain.repository

import com.example.smartpoultry.data.dataModels.DailyEggCollection
import com.example.smartpoultry.data.dataModels.EggRecordFull
import com.example.smartpoultry.data.dataSource.local.room.entities.eggCollection.EggCollection
import kotlinx.coroutines.flow.Flow
import java.sql.Date

interface EggCollectionRepository{
    suspend fun addNewRecord(eggCollection: EggCollection) : Boolean

    suspend fun deleteRecord(recordId: Int)

    suspend fun fetchAndUpdateEggRecords()

    fun listenForFireStoreChanges()

    fun getAllRecords() : Flow<List<EggCollection>>

    fun getRecord(date: Date) : Flow<List<EggCollection>>

    fun getCollectionsBetween(startDate: Date, endDate: Date) : Flow<List<EggCollection>>

    fun getAllRecordsForCell(cellId: Int) : Flow<List<EggCollection>>

    fun getRecordsForCellBetween(cellId: Int, startDate: Date,endDate: Date) : Flow<List<EggCollection>>

    fun getRecentEggCollectionRecords(startDate: Date) : Flow<List<DailyEggCollection>>

    fun getCellEggCollectionForPastDays(cellId: Int, startDate: Date) : Flow<List<EggCollection>>

    fun getCellCollectionByMonth(cellId: Int,yearMonth: String): Flow<List<EggCollection>>

    fun getBlockEggCollection(blockId : Int) : Flow<List<DailyEggCollection>>

    fun getBlockCollectionByMonth(blockId: Int, yearMonth: String) : Flow<List<DailyEggCollection>>

    fun getBlockCollectionsBetweenDates(blockId: Int, startDate: Date,endDate: Date) : Flow<List<DailyEggCollection>>

    fun getBlockEggCollectionForPastDays(blockId:Int, startDate: Date) : Flow<List<DailyEggCollection>>

    fun getOverallCollectionByMonth(yearMonth: String) : Flow<List<DailyEggCollection>>

    fun getOverallCollectionBetweenDates(startDate: Date,endDate: Date) : Flow<List<DailyEggCollection>>

    fun getOverallCollectionForPAstDays(startDate: Date) : Flow<List<DailyEggCollection>>

    fun getAllFullEggCollection() : Flow<List<EggRecordFull>>

}