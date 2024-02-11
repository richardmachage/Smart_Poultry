package com.example.smartpoultry.data.dataSource.room.entities.blocks

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blocks_tbl")
data class Blocks(
    @PrimaryKey(autoGenerate = true)
    val blockId : Int = 0,
    val blockNum : Int,
    val totalCells : Int
)
