package com.example.carapp.obd2

interface ObdCommandsManager {
    fun getCommand(pid: String): ObdCommand
}