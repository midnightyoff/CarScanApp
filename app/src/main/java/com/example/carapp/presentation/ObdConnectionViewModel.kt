package com.example.carapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carapp.obd2.ObdCommand
import com.example.carapp.obd2.ObdConnection
import com.example.carapp.obd2.ObdCustomCommand
import com.example.carapp.obd2.ObdResponse
import com.example.carapp.obd2.ObdSyncSender
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ObdConnectionViewModel : ViewModel() {
//    private val _connection = MutableStateFlow<ObdConnection?>(null)
//    val connection: StateFlow<ObdConnection?> get() = _connection

    private val _lastResponse  = MutableStateFlow<ObdResponse>(ObdResponse(""))
    val lastResponse: StateFlow<ObdResponse> get() = _lastResponse

    private var connection: ObdConnection? = null

    fun setConnection(obdConnection: ObdConnection) {
//        _connection.value = obdConnection
        connection = obdConnection
    }

    fun getConnection(): ObdConnection? {
        return connection
    }

    fun sendCommand(command: ObdCommand): ObdResponse {
        Log.d("", "send: ${command}")
        var response = ObdResponse("")
        val sender = connection?.let { ObdSyncSender(it) }
        viewModelScope.launch {
            if (sender != null) {
                response = sender.runCommand(command)
            }
//            response = connection?.send(command)!!
        }
        return response
    }
    fun sendCommand(command: String) {
        Log.d("", "send: ${command}")
        val obdCommand = ObdCustomCommand.getCommand(command)
        val sender = connection?.let { ObdSyncSender(it) }
        viewModelScope.launch {
            if (sender != null) {
                val response = sender.runCommand(obdCommand)
                _lastResponse.value = ObdResponse(response.data)
            }
//            val obdResponse = _connection.value?.send(obdCommand)
//            val response = connection?.send(obdCommand)
//            if (response != null) {
//                _lastResponse.value = ObdResponse(response)
//            }
        }
    }
}