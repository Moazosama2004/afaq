package com.example.afaq.data.alarm


import com.example.afaq.data.alarm.datasource.local.AlertLocalDataSource
import com.example.afaq.data.alarm.model.AlertEntity
import kotlinx.coroutines.flow.Flow

class AlertRepo(
    private val localDataSource: AlertLocalDataSource
) {
    suspend fun insertAlert(alert: AlertEntity) =
        localDataSource.insertAlert(alert)

    fun getAllAlerts(): Flow<List<AlertEntity>> =
        localDataSource.getAllAlerts()

    suspend fun deleteAlertById(id: Int) =
        localDataSource.deleteAlertById(id)

    suspend fun updateAlertStatus(id: Int, isActive: Boolean) =
        localDataSource.updateAlertStatus(id, isActive)
}