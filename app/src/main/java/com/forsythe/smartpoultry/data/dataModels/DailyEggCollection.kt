package com.forsythe.smartpoultry.data.dataModels

import java.sql.Date

data class DailyEggCollection(
    val date : Date,
    val totalEggs : Int
)
