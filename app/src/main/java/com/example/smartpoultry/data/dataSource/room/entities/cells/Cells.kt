package com.example.smartpoultry.data.dataSource.room.entities.cells

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("cells_tbl")
data class Cells(
    @PrimaryKey(autoGenerate = true)
    val cellId : Int = 0,
    val cellNum : Int,
    val henCount : Int,
)
