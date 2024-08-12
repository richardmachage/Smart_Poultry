package com.forsythe.smartpoultry.presentation.uiModels

data class BlockEggCollection(
    val blockId : Int,
    val blockNum : Int,
    var cells : List<CellEggCollection>
)
