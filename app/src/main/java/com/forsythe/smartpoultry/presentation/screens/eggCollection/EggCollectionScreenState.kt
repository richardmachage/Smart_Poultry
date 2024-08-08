package com.forsythe.smartpoultry.presentation.screens.eggCollection

import com.forsythe.smartpoultry.data.dataSource.local.room.relations.BlocksWithCells

data class EggCollectionScreenState(
    var currentBlock: BlocksWithCells? = null
)
