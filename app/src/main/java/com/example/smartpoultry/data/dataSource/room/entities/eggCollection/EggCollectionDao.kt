package com.example.smartpoultry.data.dataSource.room.entities.eggCollection

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Dao
interface EggCollectionDao {
    //here goes all functions needed to modify the EggCollection table

    @Insert
    suspend  fun insertCollectionRecord(eggCollection: EggCollection)

    @Delete
    suspend fun deleteCollectionRecord(eggCollection: EggCollection)

    @Query("SELECT * FROM egg_collection_tbl ORDER BY date DESC")
    fun getAllCollectionRecords(): Flow<List<EggCollection>>

    @Query("SELECT * FROM egg_collection_tbl WHERE date = :date")
    fun getCollectionRecord(date:String): Flow<List<EggCollection>>

    @Query("SELECT * FROM egg_collection_tbl WHERE date BETWEEN :startDate AND :endDate ")
    fun getCollectionRecordsBetween(startDate : Date, endDate: Date) : Flow<List<EggCollection>>

}