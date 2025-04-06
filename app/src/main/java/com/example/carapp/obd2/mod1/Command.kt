package com.example.carapp.obd2.mod1

import com.example.carapp.obd2.ObdCommand
import com.example.carapp.obd2.ObdCommandsManager
import com.example.carapp.obd2.ObdResponse

/**
                  MODE 01 - Show current data
                  RESPONSE FORMAT
         ______   ______   _______  _______  _______  _______
        |      | |      | |       ||       ||       ||       |
        |  41  | | PID  | | 1 HEX || 2 HEX || 3 HEX || 4 HEX |
        |______| |______| |_______||_______||_______||_______|
 */
class Mod1CommandsManager : ObdCommandsManager {
    private val commands = mapOf(
        "0D" to VehicleSpeed(),
        "OC" to EngineSpeed()
    )
    override fun getCommand(pid: String): ObdCommand {
        val command = commands[pid] ?: throw NoSuchElementException("Unsupported obd pid: \"${pid}\"")
        return command
    }
}

class VehicleSpeed : ObdCommand() {
    override val mode = "01"
    override val pid = "0D"
    override fun decode(data: List<String>): ObdResponse {
        if (data.size != 1)
            throw IllegalArgumentException("DATA ERROR. Invalid VehicleSpeed input: ${data}")

        // data example: 3E
        val value = getVehicleSpeed(data[0])
        return ObdResponse(value.toString())
    }
    // 0 to	255 km/h
    fun getVehicleSpeed(firstHex: String): Int {
        val speed = firstHex.toInt(radix = 16)
        return speed
    }
}

class EngineSpeed : ObdCommand() {
    override val mode = "01"
    override val pid = "0C"
    override fun decode(data: List<String>): ObdResponse {
        if (data.size != 2)
            throw IllegalArgumentException("DATA ERROR. Invalid EngineSpeed input: ${data}")

        // data example: 1A F8
        val value = getEngineSpeed(data[0], data[1])
        return ObdResponse(value.toString())
    }
    // 0 to 16,383.75 rpm
    fun getEngineSpeed(firstHex: String, secondHex: String): Double {
        val firstDecimal = firstHex.toInt(16)
        val secondDecimal = secondHex.toInt(16)

        val rpm = ((256 * firstDecimal) + secondDecimal).toDouble() / 4.0
        return rpm
    }
}
