package com.forsythe.smartpoultry.domain.domainModels

data class Cell(
    val cellId : Int,
    val cellNum : Int,
    val blockId : Int,
    val henCount : Int=0,
    )
