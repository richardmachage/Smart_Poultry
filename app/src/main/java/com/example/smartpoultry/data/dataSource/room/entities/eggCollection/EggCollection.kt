package com.example.smartpoultry.data.dataSource.room.entities.eggCollection

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(
    tableName = "egg_collection_tbl",
    indices = [Index(value = ["date", "cellId"], unique = true)]

)
data class EggCollection(
    @PrimaryKey(autoGenerate = true)
    val productionId : Int = 0,
    val date : Date,
    val cellId : Int,
    val eggCount : Int
)
