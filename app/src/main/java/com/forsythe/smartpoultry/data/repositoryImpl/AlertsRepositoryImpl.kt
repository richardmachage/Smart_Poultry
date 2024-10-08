package com.forsythe.smartpoultry.data.repositoryImpl

import com.forsythe.smartpoultry.data.dataModels.AlertFull
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.alerts.Alerts
import com.forsythe.smartpoultry.data.dataSource.local.room.entities.alerts.AlertsDao
import com.forsythe.smartpoultry.domain.repository.AlertsRepository
import kotlinx.coroutines.flow.Flow
import java.sql.Date
import javax.inject.Inject

class AlertsRepositoryImpl @Inject constructor(
    private val alertsDao: AlertsDao
) : AlertsRepository {
    override suspend fun addAlert(alerts: Alerts): Long {
        return alertsDao.addAlert(alerts)
    }

    override suspend fun deleteAlert(alertId: Int) {
        alertsDao.deleteAlert(alertId = alertId)
    }

    override fun getFlaggedCells(): Flow<List<AlertFull>> {
        return alertsDao.getFlaggedCellsFull()
    }

    override suspend fun updateAttendedStatus(status: Boolean, alertId: Int) {
        alertsDao.updateAttendedStatus(status,alertId)
    }

    override fun getAlertsForDate(date: Date): Flow<List<Alerts>> {
        return alertsDao.getAlertsForDate(date)
    }
}