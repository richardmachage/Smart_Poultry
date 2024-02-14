package com.example.smartpoultry.domain.repository

import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollection
import kotlinx.coroutines.flow.Flow
import java.sql.Date

interface EggCollectionRepository{
    suspend fun addNewRecord(eggCollection: EggCollection)

    suspend fun deleteRecord(eggCollection: EggCollection)

    fun getAllRecords() : Flow<List<EggCollection>>

    fun getRecord(date: Date) : Flow<List<EggCollection>>

    fun getCollectionsBetween(startDate: Date, endDate: Date) : Flow<List<EggCollection>>

    fun getAllRecordsForCell(cellId: Int) : Flow<List<EggCollection>>

    fun getRecordsForCellBetween(cellId: Int, startDate: Date,endDate: Date) : Flow<List<EggCollection>>

}