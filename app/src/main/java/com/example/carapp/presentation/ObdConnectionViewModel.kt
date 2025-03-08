package com.example.carapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carapp.obd2.ObdConnection
import com.example.carapp.obd2.ObdCustomCommand
import com.example.carapp.obd2.ObdResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ObdConnectionViewModel : ViewModel() {
    private val _connection = MutableStateFlow<ObdConnection?>(null)
    val connection: StateFlow<ObdConnection?> get() = _connection

    private val _lastResponse  = MutableStateFlow<ObdResponse>(ObdResponse(""))
    val lastResponse: StateFlow<ObdResponse> get() = _lastResponse

    fun setConnection(obdConnection: ObdConnection) {
        _connection.value = obdConnection
    }

    fun sendCommand(command: String) {
        val obdCommand = ObdCustomCommand.getCommand(command)
        viewModelScope.launch {
            val obdResponse = _connection.value?.send(obdCommand)
            if (obdResponse != null) {
                _lastResponse.value = obdResponse
            }
        }
    }
}