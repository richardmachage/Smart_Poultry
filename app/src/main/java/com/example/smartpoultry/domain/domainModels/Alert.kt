package com.example.smartpoultry.domain.domainModels

import androidx.room.Entity
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import java.sql.Date

data class Alert(
    val date : Date,
    val flaggedCell : Cells,
    val attended : Boolean = false
)
