package com.example.smartpoultry.data.dataSource.room.entities.eggCollection

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.time.LocalDate

@Entity(tableName = "egg_collection_tbl")
data class EggCollection(
    @PrimaryKey(autoGenerate = true)
    val productionId : Int = 0,
    val date : Date,
    val cellId : Int,
    val eggCount : Int
)
