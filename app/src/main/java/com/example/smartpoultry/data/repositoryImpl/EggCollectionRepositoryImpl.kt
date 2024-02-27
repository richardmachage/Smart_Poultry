package com.example.smartpoultry.data.repositoryImpl

import com.example.smartpoultry.data.dataModels.DailyEggCollection
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollection
import com.example.smartpoultry.data.dataSource.room.entities.eggCollection.EggCollectionDao
import com.example.smartpoultry.data.dataSource.room.relations.CellsWithEggCollections
import com.example.smartpoultry.domain.repository.EggCollectionRepository
import kotlinx.coroutines.flow.Flow
import java.sql.Date
import javax.inject.Inject

class EggCollectionRepositoryImpl @Inject constructor (
    private val eggCollectionDao: EggCollectionDao
) : EggCollectionRepository
{
    override suspend fun addNewRecord(eggCollection: EggCollection) : Boolean{
        var insertStatus = true
        try {
            eggCollectionDao.insertCollectionRecord(eggCollection)
        }catch (e : Exception){
            insertStatus = false
        }
        return insertStatus
    }

    override suspend fun deleteRecord(eggCollection: EggCollection) {
        eggCollectionDao.deleteCollectionRecord(eggCollection)
    }

    override fun getAllRecords(): Flow<List<EggCollection>> {
        return eggCollectionDao.getAllCollectionRecords()
    }

    override fun getRecord(date: Date): Flow<List<EggCollection>> {
        return  eggCollectionDao.getCollectionRecord(date)
    }

    override fun getCollectionsBetween(startDate: Date, endDate: Date): Flow<List<EggCollection>> {
        return eggCollectionDao.getCollectionRecordsBetween(startDate,endDate)
    }

    override fun getAllRecordsForCell(cellId: Int): Flow<List<EggCollection>> {
        return eggCollectionDao.getAllRecordsForCell(cellId)
    }

    override fun getRecordsForCellBetween(cellId: Int, startDate: Date, endDate: Date): Flow<List<EggCollection>> {
        return eggCollectionDao.getRecordsForCellBetween(cellId, startDate,endDate)
    }

    override fun getRecentEggCollectionRecords(startDate: Date): Flow<List<DailyEggCollection>> {
        return  eggCollectionDao.getRecentEggCollectionRecords(startDate)
    }
    override fun getCellEggCollectionForPastDays(cellId: Int, startDate: Date): Flow<List<EggCollection>> {
        return  eggCollectionDao.getCellEggCollectionForPastDays(cellId,startDate)
    }

    override fun getCellCollectionByMonth(cellId: Int, yearMonth: String): Flow<List<EggCollection>> {
        return eggCollectionDao.getCellCollectionByMonth(cellId = cellId, yearMonth = yearMonth)
    }

    override fun getBlockEggCollection(blockId: Int): Flow<CellsWithEggCollections> {
        return eggCollectionDao.getBlockEggCollection(blockId)
    }
}