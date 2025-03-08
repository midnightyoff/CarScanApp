package com.example.carapp.obd2

class PollingManager(
    private val obdConnection: ObdConnection,
    private val commands: MutableList<ObdCommand> = mutableListOf(),

) {

}