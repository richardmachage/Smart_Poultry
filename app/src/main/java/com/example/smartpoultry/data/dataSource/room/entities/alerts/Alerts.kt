package com.example.smartpoultry.data.dataSource.room.entities.alerts

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import java.sql.Date

@Entity(tableName = "alerts_tbl")
data class Alerts (
    @PrimaryKey(autoGenerate = true)
    val alertId : Int,
    val date : Date,
    val flaggedCell : Cells,
    val attended : Boolean = false
)