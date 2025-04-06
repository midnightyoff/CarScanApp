package com.example.carapp.obd2.mod3

import com.example.carapp.obd2.ObdCommand
import com.example.carapp.obd2.ObdCommandsManager
import com.example.carapp.obd2.ObdResponse

class ShowDiagnosticTroubleCodes : ObdCommand() {
    override val mode = "03"
    override val pid = ""
    override fun decode(data: List<String>): ObdResponse {
//        return DtcDecoder.decode(data)
        return ObdResponse("TODO")
    }
}
class Mod3CommandsManager : ObdCommandsManager {
    override fun getCommand(pid: String): ObdCommand {
        return ShowDiagnosticTroubleCodes()
    }
}

data class ObdDtc(val dtc: String, val description: String)

/*
    \details
     Diagnostic Trouble Code
     Each trouble code requires 2 bytes to describe. Encoded in these bytes are a category and a number.

     It is typically shown decoded into a five-character form like "U0158", where the first character (here 'U') represents the category the DTC belongs to,
      and the remaining four characters are a hexadecimal representation of the number under that category.

     Category
        00: P - Powertrain
        01: C - Chassis
        10: B - Body
        11: U - Network

      TODO
      It is also worth noting that DTCs may sometimes be encountered in a four-character form, e.g. "C158", which is simply the plain hexadecimal representation of the two bytes, with proper decoding with respect to the category not having been performed.
*/

class DtcDecoder {
    companion object {
        // TODO parse from file https://gist.github.com/wzr1337/8af2731a5ffa98f9d506537279da7a0e
        private val dtcDatabase = mapOf(
            // Powertrain (P) codes
            "P0000" to "No trouble code",
            "P0001" to "Fuel Volume Regulator Control Circuit/Open",
            "P0002" to "Fuel Volume Regulator Control Circuit Range/Performance",
            "P0100" to "Mass or Volume Air Flow Circuit Malfunction",
            "P0101" to "Mass or Volume Air Flow Circuit Range/Performance Problem",
            "P0133" to "O2 Sensor Circuit Slow Response (Bank 1 Sensor 1)",
            // Chassis (C) codes
            "C0001" to "Brake Pedal Position Sensor Circuit Malfunction",
            // Body (B) codes
            "B0001" to "Driver Frontal Stage 1 Deployment Control",
            // Network (U) codes
            "U0001" to "High Speed CAN Communication Bus"
        )

        private fun getDescription(dtc: String): String {
            val description = dtcDatabase[dtc] ?: throw NoSuchElementException("Unsupported DTC: \"${dtc}\"")
            return description
        }
        fun decode(canFrame: String): List<ObdDtc> {
            val troubleCodesList = canFrame.chunked(4)
            val length = troubleCodesList.size
            var troubleCodes = mutableListOf<ObdDtc>()
            for (i in 0..<length) {
                val troubleCode = decodeDtc(troubleCodesList[i])
                troubleCodes.add(troubleCode)
            }
//            for (i in 0..<length step 2) {
//                troubleCodes.add(decodeDtc(troubleCodesList[i], troubleCodesList[i+1]))
//            }
            return troubleCodes
        }
        /*
            accepts as input a hexadecimal string containing two bytes
            example: 0133
        */
        fun decodeDtc(input: String): ObdDtc {
            val firstByteStr = input.substring(0, 2)
            val secondByteStr = input.substring(2, 4)

            val firstByte = firstByteStr.toInt(16)
//            val secondByte = secondByteStr.toInt(16)
            // get category
            val category = when (firstByte shr 6) { // first two bits of the first byte represent the category
                0b00 -> "P" // Powertrain
                0b01 -> "C" // Chassis
                0b10 -> "B" // Body
                0b11 -> "U" // Network
                else -> "" // throw IllegalStateException("Недопустимые биты категории")
            }
            // get number
//            val number = (firstByte and 0b00111111) shl 6 + secondByte // remaining 14 bits of the two bytes represent the number
            val remainingBits = (firstByte and 0b00111111).toString(16).uppercase().padStart(2, '0')
            val dtcCode = category + remainingBits + secondByteStr.uppercase()
            try {
                val description = getDescription(dtcCode)
                return ObdDtc(dtcCode, description)
            } catch (_: NoSuchElementException) {
                return ObdDtc(dtcCode, "")
            }
        }
    }
}

//enum class ObdDtcCategory(
//    val prefix: Char,
//    val description: String
//) {
//    POWERTRAIN('P', "Powertrain"),
//    CHASSIS('C', "Chassis"),
//    BODY('B', "Body"),
//    NETWORK('U', "Network");
//
//    companion object {
//        fun fromDtcCode(dtcCode: String): ObdDtcCategory {
//            require(dtcCode.isNotEmpty()) { "DTC код не может быть пустым" }
//
//            return when (dtcCode[0].uppercaseChar()) {
//                'P' -> POWERTRAIN
//                'C' -> CHASSIS
//                'B' -> BODY
//                'U' -> NETWORK
//                else -> throw IllegalArgumentException(
//                    "Неизвестная категория DTC в коде '$dtcCode'. " +
//                            "Допустимые префиксы: P, C, B, U"
//                )
//            }
//        }
//        fun parse(firstByte: Int): ObdDtcCategory {
//            val firstTwoBits = firstByte shr 30
//            return when (firstTwoBits) {
//                0b00 -> POWERTRAIN
//                0b01 -> CHASSIS
//                0b10 -> BODY
//                0b11 -> NETWORK
//                else -> throw IllegalArgumentException(
//                    "Недопустимые биты категории в байте 0x${firstByte.toString(16)}. " +
//                            "Первые 2 бита должны быть в диапазоне 00-11"
//                )
//            }
//        }
//    }
//}