package com.example.dictionaryapp.core.connectivity

import kotlinx.coroutines.flow.Flow

interface NetworkConnectivityChecker {
    fun networkConnectivityFlow(): Flow<Boolean>
}