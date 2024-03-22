package com.example.smartpoultry.data.dataModels

import java.sql.Date

data class EggRecordFull(
    val productionId : Int,
    val date: Date,
    val blockNum : Int,
    val cellNum : Int,
    val eggCount : Int,
    val henCount : Int,
) {
    fun doesMatchSearchQuery(query: String): Boolean {

        val matchingCombinations = listOf(
            "block $blockNum cell $cellNum",
            "date $date cell $cellNum block $blockNum",
            "date $date block $blockNum cell $cellNum",
            "block $blockNum cell $cellNum date $date",
            )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }

    }
}
