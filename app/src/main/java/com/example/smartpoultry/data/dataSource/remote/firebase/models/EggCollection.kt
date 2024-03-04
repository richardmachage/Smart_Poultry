package com.example.smartpoultry.data.dataSource.remote.firebase.models

import java.util.Date


data class EggCollectionFb(
    val productionId : Int = 0,
    val date : Date = Date(System.currentTimeMillis()),
    val cellId : Int = 0,
    val eggCount : Int = 0,
    val henCount : Int = 0,
)