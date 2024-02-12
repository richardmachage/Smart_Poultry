package com.example.smartpoultry.domain.domainModels

import java.sql.Date

data class EggCollectionRecord(
    val date : Date,
    val cellId : String,
    val eggCount : Int
)
