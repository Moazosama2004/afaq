package com.example.afaq.presentation.network

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.afaq.utils.NetworkObserver
import com.example.afaq.utils.NetworkStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class NetworkViewModel(
    private val networkObserver: NetworkObserver
) : ViewModel() {

    val networkStatus: StateFlow<NetworkStatus> = networkObserver.networkStatus
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            NetworkStatus.Online
        )

    val isOnline: Boolean
        get() = networkStatus.value == NetworkStatus.Online

    override fun onCleared() {
        super.onCleared()
        networkObserver.unregister()
    }
}

class NetworkViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NetworkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NetworkViewModel(NetworkObserver(context.applicationContext)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
