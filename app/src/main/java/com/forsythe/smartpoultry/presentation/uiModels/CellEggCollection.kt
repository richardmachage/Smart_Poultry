package com.forsythe.smartpoultry.presentation.uiModels

data class CellEggCollection(
    val cellId : Int,
    val cellNum : Int,
    var eggCount: Int,
    var henCount : Int
)
