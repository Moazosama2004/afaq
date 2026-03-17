package com.example.afaq.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed class NetworkStatus {
    object Online : NetworkStatus()
    object Offline : NetworkStatus()
}

class NetworkObserver(private val context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _networkStatus = MutableSharedFlow<NetworkStatus>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val networkStatus: SharedFlow<NetworkStatus> = _networkStatus.asSharedFlow()

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _networkStatus.tryEmit(NetworkStatus.Online)
        }

        override fun onLost(network: Network) {
            _networkStatus.tryEmit(NetworkStatus.Offline)
        }
    }

    init {
        // emit initial state
        val isOnline = NetworkUtils(context).isOnline()
        _networkStatus.tryEmit(
            if (isOnline) NetworkStatus.Online else NetworkStatus.Offline
        )
        // start observing
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, callback)
    }

    fun unregister() {
        connectivityManager.unregisterNetworkCallback(callback)
    }
}
