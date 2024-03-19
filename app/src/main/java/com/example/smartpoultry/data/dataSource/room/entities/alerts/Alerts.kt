package com.example.smartpoultry.data.dataSource.room.entities.alerts

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "alerts_tbl")
data class Alerts (
    @PrimaryKey(autoGenerate = true)
    val alertId : Int,
    val date : Date,
    val flaggedCellId : Int,
    val attended : Boolean = false
)