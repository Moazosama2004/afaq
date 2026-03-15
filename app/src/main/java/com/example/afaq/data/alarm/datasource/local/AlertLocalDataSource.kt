package com.example.afaq.data.alarm.datasource.local

import com.example.afaq.data.alarm.model.AlertEntity
import kotlinx.coroutines.flow.Flow

class AlertLocalDataSource(
    private val alertDao: AlertDao
) {

    suspend fun insertAlert(alert: AlertEntity) =
        alertDao.insertAlert(alert)

    fun getAllAlerts(): Flow<List<AlertEntity>> =
        alertDao.getAllAlerts()

    suspend fun deleteAlertById(id: Int) =
        alertDao.deleteAlertById(id)

    suspend fun updateAlertStatus(id: Int, isActive: Boolean) =
        alertDao.updateAlertStatus(id, isActive)
}