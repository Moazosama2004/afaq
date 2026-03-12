package com.example.afaq.presentation.alarms.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.afaq.data.alarm.AlertRepo
import com.example.afaq.data.alarm.model.AlertEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertViewModel (
    private val alertRepo: AlertRepo
) : ViewModel(){

    private val _alertsState = MutableStateFlow<AlertsUiState>(AlertsUiState.Loading)
    val alertsState: StateFlow<AlertsUiState> = _alertsState

    private val _addState = MutableStateFlow<AddAlertState>(AddAlertState.Idle)
    val addState: StateFlow<AddAlertState> = _addState

    init { loadAlerts() }

    private fun loadAlerts() {
        viewModelScope.launch {
            alertRepo.getAllAlerts()
                .catch { e ->
                    _alertsState.value = AlertsUiState.Error(e.message ?: "Unknown error")
                }
                .collect { list ->
                    _alertsState.value = if (list.isEmpty()) {
                        AlertsUiState.Empty
                    } else {
                        AlertsUiState.Success(list)
                    }
                }
        }
    }

    fun addAlert(startTime: Long, endTime: Long, type: String) {
        _addState.value = AddAlertState.Loading
        viewModelScope.launch {
            runCatching {
                alertRepo.insertAlert(
                    AlertEntity(
                        startTime = startTime,
                        endTime = endTime,
                        type = type
                    )
                )
            }.onSuccess {
                _addState.value = AddAlertState.Success
                _addState.value = AddAlertState.Idle
            }.onFailure { e ->
                _addState.value = AddAlertState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteAlert(id: Int) {
        viewModelScope.launch {
            runCatching { alertRepo.deleteAlertById(id) }
        }
    }

}


class AlertViewModelFactory(
    private val repo: AlertRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlertViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}