package com.example.smartpoultry.data.dataSource.room.entities.feeds

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Dao
interface FeedsDao {
    @Insert
    suspend fun addNewFeedsRecord(feedRecord : Feeds) : Long

    @Insert
    suspend fun addNewFeedTrackRecord(feedTrack: FeedTrack) : Long



    @Delete
    suspend fun deleteFeedsRecord(feedRecord: Feeds)

    @Query("SELECT * FROM feeds_table")
    fun getAllFeedsRecords() : Flow<List<Feeds>>

    @Query("SELECT * FROM feeds_table WHERE date = :date")
    fun getFeedsRecord(date : Date) : Flow<List<Feeds>>

    @Query("SELECT * FROM feeds_table WHERE date BETWEEN :startDate AND :endDate")
    fun getFeedRecordsBetween(startDate : Date, endDate : Date) : Flow<List<Feeds>>
}
