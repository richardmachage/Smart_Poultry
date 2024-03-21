package com.example.smartpoultry.data.dataModels

import java.sql.Date

data class EggRecordFull(
    val recordId : Int,
    val date: Date,
    val blockNum : Int,
    val cellNum : Int,
    val eggCount : Int,
    val henCount : Int,
)
