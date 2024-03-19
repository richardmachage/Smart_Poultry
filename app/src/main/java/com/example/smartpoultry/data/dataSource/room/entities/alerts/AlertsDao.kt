package com.example.smartpoultry.data.dataSource.room.entities.alerts

import androidx.room.Dao
import androidx.room.Upsert

@Dao
interface AlertsDao {
    @Upsert
    suspend fun addAlert(alert : Alerts) : Long


}