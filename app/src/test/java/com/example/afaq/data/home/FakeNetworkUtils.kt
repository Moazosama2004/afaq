package com.example.afaq.data.home

import com.example.afaq.data.network.INetworkUtils

class FakeNetworkUtils(
    private var online : Boolean = true
) : INetworkUtils{
    override fun isOnline(): Boolean = online

    fun setOnline() {online = true}
    fun setOffline() {online = false}

}