package com.example.afaq.presentation.alarm.manager

import com.example.afaq.data.alarm.model.AlertEntity

sealed class AlarmsUiState {
    object Loading : AlarmsUiState()
    object Empty : AlarmsUiState()
    data class Success(val alerts: List<AlertEntity>) : AlarmsUiState()
    data class Error(val message: String) : AlarmsUiState()
}

sealed class AddAlarmState {
    object Idle : AddAlarmState()
    object Loading : AddAlarmState()
    object Success : AddAlarmState()
    data class Error(val message: String) : AddAlarmState()
}