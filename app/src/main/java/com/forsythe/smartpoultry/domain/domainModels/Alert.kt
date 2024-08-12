package com.forsythe.smartpoultry.domain.domainModels

import com.forsythe.smartpoultry.data.dataSource.local.room.entities.cells.Cells
import java.sql.Date

data class Alert(
    val date : Date,
    val flaggedCell : Cells,
    val attended : Boolean = false
)
