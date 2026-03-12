package com.example.afaq.presentation.alarms.manager

import com.example.afaq.data.alarm.model.AlertEntity

sealed class AlertsUiState {
    object Loading : AlertsUiState()
    object Empty : AlertsUiState()
    data class Success(val alerts: List<AlertEntity>) : AlertsUiState()
    data class Error(val message: String) : AlertsUiState()
}