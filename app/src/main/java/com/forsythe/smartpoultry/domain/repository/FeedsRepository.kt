package com.forsythe.smartpoultry.domain.repository

import com.forsythe.smartpoultry.data.dataSource.local.room.entities.feeds.FeedTrack
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.feeds.Feeds
import kotlinx.coroutines.flow.Flow
import java.sql.Date

interface FeedsRepository {
    suspend fun addNewFeedsRecord(feedRecord: Feeds) : Long

    suspend fun deleteFeedsRecord(feedRecord: Feeds)

    fun getAllFeedsRecords() : Flow<List<Feeds>>

    fun getFeedsRecord(date : Date) : Flow<List<Feeds>>

    fun getFeedRecordsBetween(startDate : Date, endDate : Date) : Flow<List<Feeds>>

    suspend fun addNewFeedTrackRecord(feedTrack: FeedTrack) : Long

}