package com.example.carapp.obd2

import com.example.carapp.obd2.mod3.ObdDtc

open class ObdResponse(
    val data: String
) {
    override fun toString(): String {
        return data
    }
}

data class MeasurementResponse(val value: Double, val unit: String): ObdResponse("$value $unit")

data class DtcResponse (
    val troubleCodes: List<String>
) : ObdResponse(troubleCodes.joinToString("\n")) {
    override fun toString(): String {
        return troubleCodes.joinToString("\n")
    }
}