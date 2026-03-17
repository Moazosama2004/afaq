package com.example.afaq.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.afaq.data.alarm.model.AlertEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertEntity): Long

    @Query("SELECT * FROM alerts ORDER BY startTime ASC")
    fun getAllAlerts(): Flow<List<AlertEntity>>

    @Query("DELETE FROM alerts WHERE id = :id")
    suspend fun deleteAlertById(id: Int)

    @Query("UPDATE alerts SET isActive = :isActive WHERE id = :id")
    suspend fun updateAlertStatus(id: Int, isActive: Boolean)
}