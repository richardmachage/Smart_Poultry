package com.example.smartpoultry.data.dataSource.room.entities.eggCollection

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class EggCollection(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val date : LocalDate,
    val cell : String,
    val eggCount : Int
)
