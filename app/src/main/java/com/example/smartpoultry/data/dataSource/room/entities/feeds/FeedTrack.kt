package com.example.smartpoultry.data.dataSource.room.entities.feeds

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity("feeds_track_tbl")
data class FeedTrack(
    @PrimaryKey(autoGenerate = true)
    val trackRecordId : Int =0,
    val date: Date,
    val numOfSacks : Int
)
