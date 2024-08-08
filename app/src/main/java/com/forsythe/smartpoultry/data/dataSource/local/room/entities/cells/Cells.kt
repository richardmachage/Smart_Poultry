package com.forsythe.smartpoultry.data.dataSource.local.room.entities.cells

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("cells_tbl")
data class Cells(
    @PrimaryKey(autoGenerate = true)
    val cellId : Int = 0,
    val blockId : Int = 0,
    val cellNum : Int = 0,
    val henCount : Int = 0,
)
