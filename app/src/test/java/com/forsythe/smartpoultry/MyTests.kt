package com.forsythe.smartpoultry

import com.forsythe.smartpoultry.data.dataSource.local.room.entities.eggCollection.EggCollection
import java.sql.Date

class MyTests {
    fun myFlagCell(cellId:Int) : Boolean{
        val listOfRecords = listOf<EggCollection>(
            EggCollection(1, Date(System.currentTimeMillis()),1,2,3),
            EggCollection(1, Date(System.currentTimeMillis()),2,2,4),
            EggCollection(1, Date(System.currentTimeMillis()),3,2,5),
            EggCollection(1, Date(System.currentTimeMillis()),4,2,4),
            EggCollection(1, Date(System.currentTimeMillis()),5,2,4),
        )
        val thresholdRatio = 0.5
        val consecutiveDays = 2
        var count = 0
        for (record in listOfRecords){
            val ratio = record.eggCount.toFloat() / record.henCount.toFloat()
            if (ratio <= thresholdRatio){
                count++
                if (count >= consecutiveDays) return true
            }else{
                count = 0
            }
        }
        return false
    }
}