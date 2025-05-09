package com.example.carapp.obd2

import com.example.carapp.obd2.at.ModeAtDecoder
import com.example.carapp.obd2.mod1.Mode1Decoder

interface ObdModeDecoder {
    fun decode(pid: String, data: String): ObdResponse
}

class ObdDecoder {
    companion object {
        fun getModeDecoder(mode: String): ObdModeDecoder {
            val decoders = mapOf(
                "01" to Mode1Decoder(),
                "AT" to ModeAtDecoder()
            )
            val decoder = decoders[mode] ?: throw NoSuchElementException("Unsupported obd mode: \"${mode}\"")
            return decoder
        }
        fun decode(mode: String, pid: String, data: String): ObdResponse {
            try {
                val decoder = getModeDecoder(mode) // throw Unsupported obd mode
                return decoder.decode(pid, data) // throw IllegalArgumentException if data has wrong length
                // throw Unsupported obd pid
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
            "7E8 03 41 0D 0C \r7EA 03 41 0D 0C \r\r>"

        */
        fun parseResponse(rawResponse: String): ObdResponse {
            val linesSeparator = listOf("\r" /* ATL0 */, "\r\n" /*  ATL1 */, "\n") // separator among lines
            val endSeparator =listOf("\r\r", "\r\n\r\n", "\n\n", "\r", "\r\n", "\n") // separator at the end of the response
            var cleanResponse = rawResponse
            if (cleanResponse.endsWith(">"))
                cleanResponse = cleanResponse.dropLast(1)
            if (cleanResponse.endsWith("\r\r"))
                cleanResponse = cleanResponse.dropLast(2)

            var input = cleanResponse.uppercase().trim()
            BadObdResponseException.check(input)
            // remove adapter messages
            input = input.replace("BUS INIT", "")
                .replace("SEARCHING","")
                .replace("...", "")

            val lines = input.lines().distinct()
            var data = lines.last()

            var mode = ""
            var pid = ""
            var canId = ""
            var echoOn = false

            var cleanData = data.replace(" ", "")
            val minDataLength = 6
            val isOneFrame =  cleanData.all { it in '0'..'9' || it in 'A'..'F' } && cleanData.length >= minDataLength
            if (isOneFrame) { // isOneFrame
                // parse CAN ID
                val canId11BitLength = 3
                val canId29BitLength = 8
                if (ObdFrame.is11BitCANIdentifier(cleanData.substring(0, 3).toInt(16))) {
                    canId = cleanData.substring(0, canId11BitLength)
                    cleanData = cleanData.drop(canId11BitLength)
                } else if (cleanData.length >= canId29BitLength + minDataLength && ObdFrame.is29BitCANIdentifier(cleanData.substring(0, canId29BitLength).toInt(16))) {
                    canId = cleanData.substring(0, canId29BitLength)
                    cleanData = cleanData.drop(canId29BitLength)
                }

                if (cleanData.startsWith("4") && cleanData.length >= minDataLength) {
                    mode = "0" + cleanData.substring(1, 2)
                    pid = cleanData.substring(2, 4)
                    cleanData = cleanData.drop(4)
                }

//                if (mode.isEmpty()) {
//                    throw
//                }

                return decode(mode, pid, cleanData)
            }

            return ObdResponse(cleanResponse.lines().last())


//            val bytes = mutableListOf<Int>()
//            hexData.chunked(2).forEach{
//                    byte -> bytes.add(byte.toInt(16))
//            }

            var headersOn = false

            // eg VIN multiple frames
//            if (lines.any{ it.contains(":") }) {
//                lines.forEach { line ->
//                    val parts = line.split(":")
//                    if (parts.size > 1) {
//                        val hexData = parts[1].replace(" ", "")
//                        data += hexData
//                    }
//                }
//                if (echoOn) {
//
//                } else if (lastResponse.isNotEmpty()) {
//
//                }
//            }

            return ObdResponse("")
//            try {
//                /*
//                    input examples:
//                    41 0D 0C // headerOff
//                    7EA 03 41 0D 0C // headerOn
//                    18DAF110 03 41 0D 09 // headerOn
//                */
//                val frame = ObdFrame.parseResponse(cleanedResponse)
//                return decode(frame.mode, frame.pid, frame.data)
//            } catch (_: IllegalArgumentException) {
//                /*
//                    input examples:
//                    OK
//                    NO DATA
//                    ?
//                */
//                return ObdResponse(cleanedResponse)
//            }
        }
    }
}