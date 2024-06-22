package com.example.smartpoultry.data.dataSource.local.room.entities.feeds

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity("feeds_table")
data class Feeds(
    @PrimaryKey(autoGenerate = true)
    val recordId : Int = 0,
    val date : Date,
    val numOfSacks : Int

)
