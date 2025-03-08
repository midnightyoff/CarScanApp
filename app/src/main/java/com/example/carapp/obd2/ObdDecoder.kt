package com.example.carapp.obd2

import com.example.carapp.obd2.mod1.EngineSpeed
import com.example.carapp.obd2.mod1.Mod1CommandsManager
import com.example.carapp.obd2.mod1.VehicleSpeed

class ObdDecoder {
    companion object {
        fun getCommandsManager(mode: String): ObdCommandsManager {
            val managers = mapOf(
                "01" to Mod1CommandsManager()
            )
            val manager = managers[mode] ?: throw NoSuchElementException("Unsupported obd mode: \"${mode}\"")
            return manager
        }
        fun decode(mode: String, pid: String, data: List<String>): ObdResponse {
            try {
                val manager = getCommandsManager(mode) // throw Unsupported obd mode
                val decoder = manager.getCommand(pid) // throw Unsupported obd pid
                return decoder.decode(data) // throw IllegalArgumentException if data has wrong length
            } catch (e: NoSuchElementException) {
                return ObdResponse(e.message.toString())
            } catch (e: IllegalArgumentException) {
                return ObdResponse(e.message.toString())
            }
        }
        /*
            AT H1 // Headers on
            01 0D
            '<pos_answer>28</pos_answer> <header>7EA</header><size>03</size><data>41 0D 28</data>'
            '7E8 03 41 0D 28 \r7EA 03 41 0D 28 \r\r>'

            XX 33
            '?\r\r>'

            AT E0
            'OK\r\r>'
            01 0D
            '41 0D 30 \r\r>'

            AT E1
            'OK\r\r>'
            01 0D
            '01 0D \r41 0D 0C \r\r>'

         */
        /*
            input examples:
            "41 0C 0A 50 \r41 0C 0A 50 \r\r>"

        */
        fun parseResponse(response: String): ObdResponse {
            var cleanedResponse = response
            val suffix = "\r\r"
            if (cleanedResponse.endsWith(suffix))
                cleanedResponse = cleanedResponse.substring(0, cleanedResponse.length - suffix.length)
            try {
                /*
                    input examples:
                    41 0D 0C // headerOff
                    7EA 03 41 0D 0C // headerOn
                    18DAF110 03 41 0D 09 // headerOn
                */
                val frame = ObdFrame.parseResponse(cleanedResponse)
                return decode(frame.mode, frame.pid, frame.data)
            } catch (_: IllegalArgumentException) {
                /*
                    input examples:
                    OK
                    NO DATA
                    ?
                */
                return ObdResponse(cleanedResponse)
            }
        }
    }
}