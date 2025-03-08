package com.example.carapp.obd2

import com.example.carapp.presentation.terminalviewmodel.TerminalMessage

class ObdErrorResponse(
    val type: ObdResponseErrorType
) : ObdResponse("")

enum class ObdResponseErrorType(val message: String/*, val description: String*/) {
    CAN_ERROR("CAN ERROR"),
    NO_DATA("NO DATA"),
    UNABLE_TO_CONNECT("UNABLE TO CONNECT"),
    BUS_INIT_ERROR("BUS INIT... ERROR"),
    BUS_BUSY("BUS BUSY"),
    DATA_ERROR("DATA ERROR"),
    STOPPED("STOPPED"),
    SEARCHING("SEARCHING..."),
    FB_ERROR("FB ERROR"),
    BUFFER_FULL("BUFFER FULL"),
    LV_RESET("LV RESET"),
    WRONG("WRONG"),
    TIMEOUT("TIMEOUT");

    companion object {
        fun fromString(value: String): ObdResponseErrorType? {
            return entries.find { it.message == value }
        }
    }
}