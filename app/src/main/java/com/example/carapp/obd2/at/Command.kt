package com.example.carapp.obd2.at

import com.example.carapp.obd2.ObdCommand
import com.example.carapp.obd2.ObdModeDecoder
import com.example.carapp.obd2.ObdResponse

class ModeAtDecoder : ObdModeDecoder {
    override fun decode(pid: String, data: String): ObdResponse {
        return ObdResponse(data)
    }
}

class ResetAll : ObdCommand() {
    override val mode = "AT"
    override val pid = "Z"
    override fun decode(data: String): ObdResponse {
        return ObdResponse(data)
    }
}
/*
class PrintingOfSpacesOff : ObdCommand() {
    override val mode = "AT"
    override val pid = "S0"
    override fun decode(data: List<String>): ObdResponse {
        return ObdResponse(data.toString())
    }
}

class PrintingOfSpacesOn : ObdCommand() {
    override val mode = "AT"
    override val pid = "S1"
    override fun decode(data: List<String>): ObdResponse {
        return ObdResponse(data.toString())
    }
}

class ResponsesOff : ObdCommand() {
    override val mode = "AT"
    override val pid = "R0"
    override fun decode(data: List<String>): ObdResponse {
        return ObdResponse(data.toString())
    }
}

class ResponsesOn : ObdCommand() {
    override val mode = "AT"
    override val pid = "R1"
    override fun decode(data: List<String>): ObdResponse {
        return ObdResponse(data.toString())
    }
}

class HeadersOff : ObdCommand() {
    override val mode = "AT"
    override val pid = "H0"
    override fun decode(data: List<String>): ObdResponse {
        return ObdResponse(data.toString())
    }
}

class HeadersOn : ObdCommand() {
    override val mode = "AT"
    override val pid = "H1"
    override fun decode(data: List<String>): ObdResponse {
        return ObdResponse(data.toString())
    }
}

class EchoOff : ObdCommand() {
    override val mode = "AT"
    override val pid = "E0"
    override fun decode(data: List<String>): ObdResponse {
        return ObdResponse(data.toString())
    }
}

class EchoOn : ObdCommand() {
    override val mode = "AT"
    override val pid = "E1"
    override fun decode(data: List<String>): ObdResponse {
        return ObdResponse(data.toString())
    }
}*/
