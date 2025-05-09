package com.example.carapp.obd2.mod1

import com.example.carapp.obd2.MeasurementResponse
import com.example.carapp.obd2.ObdCommand
import com.example.carapp.obd2.ObdCommandsManager
import com.example.carapp.obd2.ObdModeDecoder
import com.example.carapp.obd2.ObdResponse

class Mode1Decoder : ObdModeDecoder {
    private val commands = mapOf(
        "0D" to VehicleSpeed(),
        "OC" to EngineSpeed()
    )
    override fun decode(pid: String, data: String): ObdResponse {
        val command = commands[pid] ?: throw NoSuchElementException("Mode 1. Unsupported obd pid: \"${pid}\"")
        return command.decode(data)
    }
}

class VehicleSpeed : ObdCommand() {
    override val mode = "01"
    override val pid = "0D"
    override fun decode(data: String): ObdResponse {
        if (data.length != 2)
            throw IllegalArgumentException("DATA ERROR. Invalid VehicleSpeed input: ${data}")

        // 0 to	255 km/h
        val speed = data.toInt(16)
        return MeasurementResponse(speed.toDouble(), "km/h")
        // data example: 3E
//        val value = getVehicleSpeed(bytes[0])
//        return ObdResponse(value.toString())
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
    override fun decode(data: String): ObdResponse {
        val bytes = mutableListOf<Int>()
        data.chunked(2).forEach{
                byte -> bytes.add(byte.toInt(16))
        }
        if (bytes.size != 2)
            throw IllegalArgumentException("DATA ERROR. Invalid EngineSpeed input: ${data}")

        // data example: 1A F8
        val rpm = ((256 * bytes[0]) + bytes[1]).toDouble() / 4.0
        return ObdResponse(rpm.toString())
    }
    // 0 to 16,383.75 rpm
    fun getEngineSpeed(firstHex: String, secondHex: String): Double {
        val firstDecimal = firstHex.toInt(16)
        val secondDecimal = secondHex.toInt(16)

        val rpm = ((256 * firstDecimal) + secondDecimal).toDouble() / 4.0
        return rpm
    }
}
