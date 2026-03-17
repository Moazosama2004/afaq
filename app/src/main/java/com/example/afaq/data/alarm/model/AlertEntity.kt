package com.example.afaq.data.alarm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val startTime: Long,   // timestamp in ms
    val endTime: Long,     // timestamp in ms
    val type: String,      // "ALARM" or "NOTIFICATION"
    val isActive: Boolean = true
)