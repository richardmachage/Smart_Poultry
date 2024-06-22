package com.example.smartpoultry.data.dataSource.local.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.smartpoultry.data.dataSource.local.room.entities.blocks.Blocks
import com.example.smartpoultry.data.dataSource.local.room.entities.cells.Cells

data class BlocksWithCells(
    @Embedded val block: Blocks, //parent entity
    //define relationship between this two
    @Relation(
        parentColumn = "blockId", //from parent entity
        entityColumn = "blockId"//corresponding field from parent in child
    )
    val cell: List<Cells> //child entity
)