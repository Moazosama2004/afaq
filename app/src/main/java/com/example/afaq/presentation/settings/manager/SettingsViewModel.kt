package com.example.afaq.presentation.settings.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.afaq.data.settings.SettingsRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repo: SettingsRepo
) : ViewModel() {

    // ─── States - shared across app ───────────────────────
    val language = repo.language.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "English"
    )
    val tempUnit = repo.tempUnit.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "Celsius °C"
    )
    val windUnit = repo.windUnit.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "meter/sec"
    )
    val location = repo.location.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "GPS"
    )
//    val theme = repo.theme.stateIn(
//        viewModelScope,
//        SharingStarted.WhileSubscribed(5000),
//        "System"
//    )

    // ─── Update ────────────────────────────────────────────
    fun setLanguage(value: String) = viewModelScope.launch { repo.setLanguage(value) }
    fun setTempUnit(value: String) = viewModelScope.launch { repo.setTempUnit(value) }
    fun setWindUnit(value: String) = viewModelScope.launch { repo.setWindUnit(value) }
    fun setLocation(value: String) = viewModelScope.launch { repo.setLocation(value) }
//    fun setTheme(value: String) = viewModelScope.launch { repo.setTheme(value) }
}

class SettingsViewModelFactory(
    private val repo: SettingsRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}