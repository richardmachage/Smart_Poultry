package com.example.smartpoultry.presentation.screens.eggCollection

import com.example.smartpoultry.data.dataSource.local.room.relations.BlocksWithCells

data class EggCollectionScreenState(
    var currentBlock: BlocksWithCells? = null
)
