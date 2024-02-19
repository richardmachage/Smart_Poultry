package com.example.smartpoultry.data.repositoryImpl

import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.room.entities.cells.CellsDao
import com.example.smartpoultry.domain.repository.CellsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CellsRepositoryImpl @Inject constructor(
    private val cellsDao: CellsDao
) : CellsRepository {
    override suspend fun addNewCell(cell: Cells) {
        cellsDao.addNewCell(cell = cell)
    }

    override suspend fun deleteCell(cell: Cells) {
        cellsDao.deleteCell(cell = cell)
    }

    override fun getAllCells(): Flow<List<Cells>> {
        return  cellsDao.getAllCells()
    }

    override fun getCell(cellId: Int): Flow<List<Cells>> {
        return cellsDao.getCell(cellId = cellId)
    }

    override fun getTotalHenCount(): Flow<List<Int>> {
        return cellsDao.getTotalHenCount()
    }

    override fun getCellsForBlock(blockId : Int): Flow<List<Cells>> {
        return cellsDao.getCellsForABLock(blockId = blockId)
    }

    override suspend fun updateCellInfo(cell: Cells) {
        cellsDao.updateCellInfo(cell)
    }
}