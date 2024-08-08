package com.forsythe.smartpoultry.domain.repository

import com.forsythe.smartpoultry.data.dataModels.AlertFull
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.alerts.Alerts
import kotlinx.coroutines.flow.Flow
import java.sql.Date

interface AlertsRepository {
    suspend fun addAlert (alerts: Alerts) : Long
    suspend fun deleteAlert (alertId: Int)

    fun getFlaggedCells(): Flow<List<AlertFull>>
    suspend fun updateAttendedStatus(status : Boolean, alertId:Int)

    fun getAlertsForDate(date: Date) : Flow<List<Alerts>>
}