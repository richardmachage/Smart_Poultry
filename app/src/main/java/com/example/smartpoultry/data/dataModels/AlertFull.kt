package com.example.smartpoultry.data.dataModels

import java.sql.Date

data class AlertFull(
    val date:Date,
    val attended : Boolean,
    val cellNum : Int,
    val blockNum : Int
)
