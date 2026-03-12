package com.example.afaq.presentation.alarms.manager

sealed class AddAlertState {
    object Idle : AddAlertState()
    object Loading : AddAlertState()
    object Success : AddAlertState()
    data class Error(val message: String) : AddAlertState()
}