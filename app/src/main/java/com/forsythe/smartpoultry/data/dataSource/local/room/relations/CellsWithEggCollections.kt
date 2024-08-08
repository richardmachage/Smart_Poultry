package com.forsythe.smartpoultry.data.dataSource.local.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.cells.Cells
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.eggCollection.EggCollection

data class CellsWithEggCollections(
    @Embedded val cell : Cells, //parent entity

    @Relation(
        parentColumn = "cellId",
        entityColumn = "cellId"
    )
    val eggCollection: EggCollection
)
