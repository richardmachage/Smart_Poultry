package com.example.smartpoultry.data.dataSource.room.entities.alerts

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
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

}