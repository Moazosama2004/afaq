package com.example.afaq.services.alarms

import java.time.LocalDateTime

data class AlarmItem(
    val message : String,
    val time : LocalDateTime
)
