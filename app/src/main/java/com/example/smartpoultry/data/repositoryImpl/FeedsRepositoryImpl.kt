package com.example.smartpoultry.data.repositoryImpl

import com.example.smartpoultry.data.dataSource.local.room.entities.feeds.FeedTrack
import com.example.smartpoultry.data.dataSource.local.room.entities.feeds.Feeds
import com.example.smartpoultry.data.dataSource.local.room.entities.feeds.FeedsDao
import com.example.smartpoultry.domain.repository.FeedsRepository
import kotlinx.coroutines.flow.Flow
import java.sql.Date
import javax.inject.Inject

class FeedsRepositoryImpl @Inject constructor(
    private val feedsDao: FeedsDao
) : FeedsRepository {
    override suspend fun addNewFeedsRecord(feedRecord: Feeds) : Long{
       return feedsDao.addNewFeedsRecord(feedRecord = feedRecord)
    }

    override suspend fun deleteFeedsRecord(feedRecord: Feeds) {
        feedsDao.deleteFeedsRecord(feedRecord = feedRecord)
    }

    override fun getAllFeedsRecords(): Flow<List<Feeds>> {
        return  feedsDao.getAllFeedsRecords()
    }

    override fun getFeedsRecord(date: Date): Flow<List<Feeds>> {
        return feedsDao.getFeedsRecord(date)
    }

    override fun getFeedRecordsBetween(startDate: Date, endDate: Date): Flow<List<Feeds>> {
        return getFeedRecordsBetween(startDate,endDate)
    }

    override suspend fun addNewFeedTrackRecord(feedTrack: FeedTrack): Long {
       return feedsDao.addNewFeedTrackRecord(feedTrack = feedTrack)
    }
}