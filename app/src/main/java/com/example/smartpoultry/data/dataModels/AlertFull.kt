package com.example.smartpoultry.data.dataModels

import android.annotation.SuppressLint
import java.sql.Date
import java.text.SimpleDateFormat

data class AlertFull(
    val alertId : Int,
    val date:Date,
    val attended : Boolean,
    val cellNum : Int,
    val blockNum : Int
){
    @SuppressLint("SimpleDateFormat")
    fun doesMatchSearchQuery(query : String) : Boolean{
        val matchingCombinations = listOf(
            alertId.toString(),
            "date ${SimpleDateFormat("dd MMMM yyyy").format(date)}",
            //"Date ${SimpleDateFormat("dd MMMM yyyy").format(date)}",
            "cell $cellNum",
           // "Cell $cellNum",
            "block $blockNum",
            //"Block $blockNum",
        )

        return matchingCombinations.any{
            it.contains(query, ignoreCase = true)
        }
    }
}
