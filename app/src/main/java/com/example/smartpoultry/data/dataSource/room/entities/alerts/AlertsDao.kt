package com.example.smartpoultry.data.dataSource.room.entities.alerts

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.smartpoultry.data.dataModels.AlertFull
import kotlinx.coroutines.flow.Flow
import java.sql.Date

@Dao
interface AlertsDao {
    @Upsert
    suspend fun addAlert(alert : Alerts) : Long
    @Delete
    suspend fun deleteAlert(alert: Alerts)

    @Query("SELECT * FROM alerts_tbl ORDER BY date DESC")
    fun getAllAlerts() : Flow<List<Alerts>>

    @Query("SELECT * FROM alerts_tbl WHERE date = :date")
    fun getAlertsForDate(date: Date) : Flow<List<Alerts>>

    @Query("UPDATE alerts_tbl SET attended = :status WHERE alertId= :alertId ")
    suspend fun updateAttendedStatus(status:Boolean, alertId:Int)

    @Transaction
    @Query("SELECT alerts_tbl.alertId, alerts_tbl.date, alerts_tbl.attended, cells_tbl.cellNum, blocks_tbl.blockNum FROM alerts_tbl INNER JOIN cells_tbl ON alerts_tbl.flaggedCellId = cells_tbl.cellId INNER JOIN blocks_tbl ON cells_tbl.blockId = blocks_tbl.blockId ORDER BY date DESC")
    fun getFlaggedCellsFull() : Flow<List<AlertFull>>
}