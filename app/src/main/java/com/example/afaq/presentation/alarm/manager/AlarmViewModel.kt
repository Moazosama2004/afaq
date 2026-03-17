package com.example.afaq.presentation.alarm.manager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.afaq.data.alarm.AlertRepo
import com.example.afaq.data.alarm.model.AlertEntity
import com.example.afaq.services.alarms.AndroidAlarmManager
import com.example.afaq.services.workmanager.WorkManagerScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertViewModel(
    private val alertRepo: AlertRepo,
    private val alarmManager :  AndroidAlarmManager,
    private val workManagerScheduler : WorkManagerScheduler

) : ViewModel() {

//    private val alarmManager = AndroidAlarmManager(application)
//    private val workManagerScheduler = WorkManagerScheduler(application)

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
                val entity = AlertEntity(
                    startTime = startTime,
                    endTime = endTime,
                    type = type
                )
                val id = alertRepo.insertAlert(entity).toInt()
                val savedEntity = entity.copy(id = id)
                
                // Trigger Actual Alarm/Notification
                if (type == "ALARM") {
                    alarmManager.schedule(savedEntity)
                } else {
                    workManagerScheduler.schedule(savedEntity)
                }

            }.onSuccess {
                _addState.value = AddAlarmState.Success
                _addState.value = AddAlarmState.Idle
            }.onFailure { e ->
                _addState.value = AddAlarmState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteAlert(alert: AlertEntity) {
        viewModelScope.launch {
            runCatching {
                alertRepo.deleteAlertById(alert.id)
                if (alert.type == "ALARM") {
                    alarmManager.cancel(alert)
                } else {
                    workManagerScheduler.cancel(alert.id)
                }
            }
        }
    }

}


class AlertViewModelFactory(
    private val repo: AlertRepo,
    private val alarmManager :  AndroidAlarmManager,
    private val workManagerScheduler : WorkManagerScheduler
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlertViewModel(repo,alarmManager,workManagerScheduler) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}