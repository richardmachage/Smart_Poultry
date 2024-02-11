package com.example.smartpoultry.data.dataSource.room.entities.eggCollection

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class EggCollection(
    @PrimaryKey(autoGenerate = true)
    val productionId : Int = 0,
    val date : String,
    val cellId : String,
    val eggCount : Int
)
