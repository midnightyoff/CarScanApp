package com.example.carapp.presentation.terminalviewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TerminalViewModel : ViewModel() {
    val messages = mutableStateListOf<TerminalMessage>()
    var inputText by mutableStateOf("")

    fun sendCommand(command: String) {
        if (command.isBlank()) return

        messages.add(TerminalMessage.Command(command))
        inputText = ""

        // TODO: Реальная отправка команды через OBD2
        simulateResponse(command)
    }

    private fun simulateResponse(command: String) {
        viewModelScope.launch {
            delay(500)
            val response = when (command.uppercase()) {
                "ATZ" -> "ELM327 v1.5"
                "0100" -> "41 00 BE 1F B8 10"
                else -> "NO DATA"
            }
            messages.add(TerminalMessage.Response(response))
        }
    }
}

sealed class TerminalMessage(
    val text: String
) {
    class Command(text: String) : TerminalMessage(text)
    class Response(text: String) : TerminalMessage(text)
}