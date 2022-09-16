package com.pi.androidbasehiltmvvm.core.network

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<ConnectionStatus>

    enum class ConnectionStatus {
        Available, Unavailable, Losing, Lost
    }
}