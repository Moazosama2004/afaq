package com.example.afaq.presentation.alarm.manager

sealed class AddAlarmState {
    object Idle : AddAlarmState()
    object Loading : AddAlarmState()
    object Success : AddAlarmState()
    data class Error(val message: String) : AddAlarmState()
}