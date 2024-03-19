package com.example.smartpoultry.data.repositoryImpl

import com.example.smartpoultry.data.dataModels.AlertFull
import com.example.smartpoultry.data.dataSource.room.entities.alerts.Alerts
import com.example.smartpoultry.data.dataSource.room.entities.alerts.AlertsDao
import com.example.smartpoultry.domain.repository.AlertsRepository
import kotlinx.coroutines.flow.Flow
import java.sql.Date
import javax.inject.Inject

class AlertsRepositoryImpl @Inject constructor(
    private val alertsDao: AlertsDao
) : AlertsRepository {
    override suspend fun addAlert(alerts: Alerts): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(alerts: Alerts) {
        TODO("Not yet implemented")
    }

    override fun getFlaggedCells(): Flow<List<AlertFull>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateAttendedStatus(status: Boolean, alertId: Int) {
        TODO("Not yet implemented")
    }

    override fun getAlertsForDate(date: Date): Flow<List<Alerts>> {
        TODO("Not yet implemented")
    }
}