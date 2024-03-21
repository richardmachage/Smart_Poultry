package com.example.smartpoultry.data.dataModels

import android.annotation.SuppressLint
import java.sql.Date
import java.text.SimpleDateFormat

data class EggRecordFull(
    //val recordId : Int,
    val date: Date,
    val blockNum : Int,
    val cellNum : Int,
    val eggCount : Int,
    val henCount : Int,
) {
    @SuppressLint("SimpleDateFormat")
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
           // recordId.toString(),
            "date ${SimpleDateFormat("dd MMMM yyyy").format(date)}",
            //"Date ${SimpleDateFormat("dd MMMM yyyy").format(date)}",
            "cell $cellNum",
            // "Cell $cellNum",
            "block $blockNum",
            //"Block $blockNum",
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
