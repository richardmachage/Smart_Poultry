package com.example.smartpoultry.data.dataSource.room.entities.eggCollection

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smartpoultry.data.dataModels.DailyEggCollection
import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Dao
interface EggCollectionDao {
    //here goes all functions needed to modify the EggCollection table

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend  fun insertCollectionRecord(eggCollection: EggCollection)

    @Delete
    suspend fun deleteCollectionRecord(eggCollection: EggCollection)

    @Query("SELECT * FROM egg_collection_tbl ORDER BY date DESC")
    fun getAllCollectionRecords(): Flow<List<EggCollection>>

    @Query("SELECT * FROM egg_collection_tbl WHERE date = :date")
    fun getCollectionRecord(date: Date): Flow<List<EggCollection>>

    @Query("SELECT * FROM egg_collection_tbl WHERE date BETWEEN :startDate AND :endDate ")
    fun getCollectionRecordsBetween(startDate : Date, endDate: Date) : Flow<List<EggCollection>>

    @Query("SELECT * FROM egg_collection_tbl WHERE cellId = :cellId")
    fun getAllRecordsForCell(cellId : Int) : Flow<List<EggCollection>>

    @Query("SELECT * FROM egg_collection_tbl WHERE cellId = :cellId AND date BETWEEN :startDate AND  :endDate")
    fun getRecordsForCellBetween(cellId: Int, startDate: Date,endDate: Date) : Flow<List<EggCollection>>

    @Query("SELECT date, SUM(eggCount) as totalEggs FROM egg_collection_tbl WHERE date >=:startDate GROUP BY date")
    fun getRecentEggCollectionRecords(startDate: Date) : Flow<List<DailyEggCollection>>

}