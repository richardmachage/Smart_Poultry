package com.example.smartpoultry.data.dataSource.local.room.entities.eggCollection

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.smartpoultry.data.dataModels.DailyEggCollection
import com.example.smartpoultry.data.dataModels.EggRecordFull
import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Dao
interface EggCollectionDao {
    //here goes all functions needed to modify the EggCollection table

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(eggCollections: List<EggCollection>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend  fun insertCollectionRecord(eggCollection: EggCollection) : Long

    @Update
    suspend fun updateCollectionRecord(eggCollection: EggCollection)

    @Query("DELETE  FROM egg_collection_tbl WHERE productionId = :recordId")
    suspend fun deleteCollectionRecord(recordId: Int)

    @Query("SELECT * FROM egg_collection_tbl ORDER BY date DESC ")
    fun getAllCollectionRecords(): Flow<List<EggCollection>>

    @Query("SELECT * FROM egg_collection_tbl WHERE date = :date ")
    fun getCollectionRecord(date: Date): Flow<List<EggCollection>>

    @Query("SELECT * FROM egg_collection_tbl WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC ")
    fun getCollectionRecordsBetween(startDate : Date, endDate: Date) : Flow<List<EggCollection>>

    @Query("SELECT * FROM egg_collection_tbl WHERE cellId = :cellId ORDER BY date DESC")
    fun getAllRecordsForCell(cellId : Int) : Flow<List<EggCollection>>

    @Query("SELECT * FROM egg_collection_tbl WHERE cellId = :cellId AND date BETWEEN :startDate AND  :endDate ORDER BY date DESC")
    fun getRecordsForCellBetween(cellId: Int, startDate: Date,endDate: Date) : Flow<List<EggCollection>>

    @Query("SELECT date, SUM(eggCount) as totalEggs FROM egg_collection_tbl WHERE date >=:startDate GROUP BY date ORDER BY date DESC")
    fun getRecentEggCollectionRecords(startDate: Date) : Flow<List<DailyEggCollection>>

    @Query("SELECT * FROM egg_collection_tbl WHERE date>=:startDate AND cellId = :cellId ORDER BY date DESC")
    fun getCellEggCollectionForPastDays(cellId: Int, startDate: Date) : Flow<List<EggCollection>>//on implementation, calculate date from past number of days input

    @Query("SELECT * FROM egg_collection_tbl WHERE strftime('%Y-%m', datetime(date / 1000, 'unixepoch')) = :yearMonth AND cellId=:cellId ORDER BY date DESC")
    fun getCellCollectionByMonth(cellId: Int,yearMonth: String): Flow<List<EggCollection>>


    @Transaction
    @Query("SELECT date, SUM(eggCount) AS totalEggs FROM egg_collection_tbl INNER JOIN cells_tbl ON egg_collection_tbl.cellId = cells_tbl.cellId WHERE cells_tbl.blockId = :blockId GROUP BY date ORDER BY date DESC")
    fun getBlockEggCollections(blockId: Int) : Flow<List<DailyEggCollection>>

    @Transaction
    @Query("SELECT date, SUM(eggCount) AS totalEggs FROM egg_collection_tbl INNER JOIN cells_tbl ON egg_collection_tbl.cellId = cells_tbl.cellId WHERE cells_tbl.blockId = :blockId AND date>=:startDate GROUP BY date ORDER BY date DESC")
    fun getBlockEggCollectionsForPastDays(blockId: Int, startDate: Date) : Flow<List<DailyEggCollection>>

    @Transaction
    @Query("SELECT date, SUM(eggCount) AS totalEggs FROM egg_collection_tbl INNER JOIN cells_tbl ON egg_collection_tbl.cellId = cells_tbl.cellId WHERE cells_tbl.blockId = :blockId AND date BETWEEN :startDate AND :endDate GROUP BY date ORDER BY date DESC")
    fun getBlockEggCollectionsBetweenDates(blockId: Int, startDate: Date,endDate: Date) : Flow<List<DailyEggCollection>>

    @Transaction
    @Query("SELECT date, SUM(eggCount) AS totalEggs FROM egg_collection_tbl INNER JOIN cells_tbl ON egg_collection_tbl.cellId = cells_tbl.cellId WHERE cells_tbl.blockId = :blockId AND strftime('%Y-%m', datetime(date / 1000, 'unixepoch')) = :yearMonth GROUP BY date ORDER BY date DESC")
    fun getBlockCollectionByMonth(blockId: Int, yearMonth: String) : Flow<List<DailyEggCollection>>

    @Query("SELECT date,SUM(eggCount) AS totalEggs FROM egg_collection_tbl WHERE strftime('%Y-%m', datetime(date / 1000, 'unixepoch')) = :yearMonth GROUP BY date ORDER BY date DESC")
    fun getOverallCollectionByMonth(yearMonth: String) : Flow<List<DailyEggCollection>>

    @Query("SELECT date, SUM(eggCount) AS totalEggs FROM egg_collection_tbl WHERE date BETWEEN :startDate AND :endDate GROUP BY date ORDER BY date DESC")
    fun getOverallCollectionBetweenDates(startDate: Date,endDate: Date) : Flow<List<DailyEggCollection>>

    @Query("SELECT date, SUM(eggcount) AS totalEggs FROM egg_collection_tbl WHERE date >= :startDate GROUP BY date ORDER BY date DESC")
    fun getOverallCollectionForPAstDays(startDate: Date) : Flow<List<DailyEggCollection>>

    @Transaction
    @Query("SELECT egg_collection_tbl.productionId,egg_collection_tbl.date, egg_collection_tbl.eggCount , egg_collection_tbl.henCount, cells_tbl.cellNum, blocks_tbl.blockNum FROM egg_collection_tbl INNER JOIN cells_tbl ON egg_collection_tbl.cellId = cells_tbl.cellId INNER JOIN blocks_tbl ON cells_tbl.blockId = blocks_tbl.blockId ORDER BY date DESC")
    fun getEggRecordsFull() : Flow<List<EggRecordFull>>

}