package com.example.smartpoultry.data.dataSource.local.room.entities.PopulationChange

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "pop_change_tbl")
data class PopChange(
    @PrimaryKey(autoGenerate = true)
    val operationId : Int =0 ,
    val cellID : Int,
    val operationType : Boolean , //  Where by True is "adding" while false is "removing"
    val changeCount : Int, // Number of chicken added or removed
    val date : Date,
    val henCountToday: Int // Note that this is the hen cont on this given day, after the operation! if there are several operations then it should reflect the latest changes.
)
