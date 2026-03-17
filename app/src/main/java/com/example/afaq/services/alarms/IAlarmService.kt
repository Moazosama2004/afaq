package com.example.afaq.services.alarms

import com.example.afaq.data.alarm.model.AlertEntity

interface IAlarmService {
    fun schedule(item: AlertEntity)
    fun cancel(item: AlertEntity)
}