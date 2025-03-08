package com.example.carapp.obd2

/*
    \details
    OBD2 frame
    +------------+  +------------+ +------------+ +------------+ +------------+ +------------+ +------------+ +------------+ +------------+
    |            |  |            | |            | |            | |            | |            | |            | |            | |            |
    | Identifier |  |   Length   | |    Mode    | |    PID     | |      A     | |      B     | |      С     | |      В     | |   Unused   |
    |            |  |            | |            | |            | |            | |            | |            | |            | |            |
    +------------+  +------------+ +------------+ +------------+ +------------+ +------------+ +------------+ +------------+ +------------+
       CAN ID                                 CAN DATA

    Identifier: For OBD2 messages, the identifier is standard 11-bit and used to distinguish between "request
    messages" (ID 7DF) and "response messages" (ID 7E8 to 7EF). Note that 7E8 will typically be where the main
    engine or ECU responds at. In some vehicles (e.g. vans and light/medium/heavy duty vehicles), you may find
    that the raw CAN data uses extended 29-bit CAN identifiers instead of 11-bit CAN identifiers. In this case,
    you will typically need to modify the OBD2 PID requests to use the CAN ID 18DB33F1 instead of 7DF. If the
    vehicle responds to the requests, you'll typically see responses with CANIDs 18DAF100 to 18DAF1FF (in practice,
    typically 18DAF110 and 18DAF11E).

    Request	18DB33F1 (417_018_865 dec)
    Response (example)	18DAF110
    18DAF100 to 18DAF1FF
    (417_001_728 dec 417_001_983)

    Length: This simply reflects the length in number of bytes of the remaining data (03 to 06).

    Mode: For requests, this will be between 01-0A. For responses the 0 is replaced by 4 (i.e. 41, 42, … , 4A).

    PID: For each mode, a list of standard OBD2 PIDs exist - e.g. in Mode 01, PID 0D is Vehicle Speed. Each PID
    has a description and some have a specified min/max and conversion formula.
 */

class ObdFrame(
    val identifier: String,
    val length: Int,
    val mode: String,
    val pid: String,
    val data: List<String>
) {
    companion object {
        fun parseResponse(input: String): ObdFrame {
            /*
                input examples:
                41 0D 0C \r\r> // headerOff
                7EA 03 41 0D 0C \r\r> // headerOn
                18DAF110 03 41 0D 09 \r\r> // headerOn
             */
            val cleanedInput = input.replace("\r", "").replace(">", "").trim()
            /*
                cleanedInput examples:
                41 0D 0C // headerOff
                7EA 03 41 0D 0C // headerOn
                18DAF110 03 41 0D 09 // headerOn
             */
            val parts = cleanedInput.split(" ")
            if (parts.size < 3)
                throw IllegalArgumentException("Invalid ObdFrame input: \"${input}\"")
            val firstPart = parts[0].toIntOrNull(16) ?: throw IllegalArgumentException("Invalid ObdFrame input: \"${input}\"")

            var identifier = ""
            var length = 0
            var modeIdx = 0
            if (isCANIdentifier(firstPart)) {
                if (parts.size < 5)
                    throw IllegalArgumentException("Invalid ObdFrame input: \"${input}\"")
                modeIdx = 2
                identifier = parts[0]
                length = parts[1].toIntOrNull() ?: throw IllegalArgumentException("Invalid ObdFrame input (Illegal length): \"${input}\"")
            }

            var mode = parts[modeIdx]
            if (mode.startsWith("4")) // TODO create a class for a mode
                mode = "0" + mode.substring(1)

            val pid = parts[modeIdx+1]

            val data = parts.drop(modeIdx+2)
            if (length == 0)
                length = data.size + 2

            return ObdFrame(identifier, length, mode, pid, data)
        }

        fun isCANIdentifier(value: Int): Boolean {
            return is11BitCANIdentifier(value) || is29BitCANIdentifier(value)
        }

        fun is11BitCANIdentifier(value: Int): Boolean {
            return is11BitResponseCANIdentifier(value) || is11BitRequestCANIdentifier(value)
        }
        fun is11BitRequestCANIdentifier(value: Int): Boolean {
            return value == 0x7DF
        }
        fun is11BitResponseCANIdentifier(value: Int): Boolean {
            return value in 0x7E8..0x7EF
        }

        fun is29BitCANIdentifier(value: Int): Boolean {
            return is29BitResponseCANIdentifier(value) || is29BitRequestCANIdentifier(value)
        }
        fun is29BitRequestCANIdentifier(value: Int): Boolean {
            return value == 0x18DB33F1
        }
        fun is29BitResponseCANIdentifier(value: Int): Boolean {
            return value in 0x18DAF100..0x18DAF1FF
        }
    }
}