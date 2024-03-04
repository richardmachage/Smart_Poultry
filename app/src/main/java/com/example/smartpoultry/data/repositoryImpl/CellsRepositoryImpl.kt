package com.example.smartpoultry.data.repositoryImpl

import android.util.Log
import com.example.smartpoultry.data.dataSource.remote.firebase.models.Cell
import com.example.smartpoultry.data.dataSource.room.entities.cells.Cells
import com.example.smartpoultry.data.dataSource.room.entities.cells.CellsDao
import com.example.smartpoultry.domain.repository.CellsRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CellsRepositoryImpl @Inject constructor(
    private val cellsDao: CellsDao,
    private val fireStoreDb : FirebaseFirestore
) : CellsRepository {

    init {
        listenForFireStoreChanges()
    }

    private fun listenForFireStoreChanges() {
        fireStoreDb.collection("Blocks")
            .addSnapshotListener {
                                 querySnapshot, exception ->
                if (exception != null){ //f an error exists, it logs the error and returns early from the listener.
                    Log.w("Error","Listen failed.", exception)

                }

            }
    }

    override suspend fun addNewCell(cell: Cells) {
        val cellId = cellsDao.addNewCell(cell = cell)
        fireStoreDb
            .collection("Cells")
            .document(cellId.toString())
            .set(
                Cell(
                    cellId = cellId.toInt(),
                    blockId = cell.blockId,
                    cellNum = cell.cellNum,
                    henCount = cell.henCount
                )
            )
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }

    override suspend fun deleteCell(cell: Cells) {
        cellsDao.deleteCell(cell = cell)
        fireStoreDb
            .collection("Cells")
            .document(cell.cellId.toString())
            .delete()
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
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