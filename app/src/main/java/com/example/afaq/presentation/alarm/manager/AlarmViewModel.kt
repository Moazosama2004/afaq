package com.example.afaq.presentation.alarm.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.afaq.data.alarm.AlertRepo
import com.example.afaq.data.alarm.model.AlertEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertViewModel(
    private val alertRepo: AlertRepo
) : ViewModel() {

    private val _alertsState = MutableStateFlow<AlarmsUiState>(AlarmsUiState.Loading)
    val alertsState: StateFlow<AlarmsUiState> = _alertsState

    private val _addState = MutableStateFlow<AddAlarmState>(AddAlarmState.Idle)
    val addState: StateFlow<AddAlarmState> = _addState

    init {
        loadAlerts()
    }

    private fun loadAlerts() {
        viewModelScope.launch {
            alertRepo.getAllAlerts()
                .catch { e ->
                    _alertsState.value = AlarmsUiState.Error(e.message ?: "Unknown error")
                }
                .collect { list ->
                    _alertsState.value = if (list.isEmpty()) {
                        AlarmsUiState.Empty
                    } else {
                        AlarmsUiState.Success(list)
                    }
                }
        }
    }

    fun addAlert(startTime: Long, endTime: Long, type: String) {
        _addState.value = AddAlarmState.Loading
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
                _addState.value = AddAlarmState.Success
                _addState.value = AddAlarmState.Idle
            }.onFailure { e ->
                _addState.value = AddAlarmState.Error(e.message ?: "Unknown error")
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