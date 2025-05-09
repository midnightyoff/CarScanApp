package com.example.carapp.obd2.mod3

import com.example.carapp.obd2.DtcResponse
import com.example.carapp.obd2.ObdDecoder
import com.example.carapp.obd2.mod1.EngineSpeed
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class DiagnosticTroubleCodes(private val input: String, private val expected: List<String>) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun values() = listOf(
//            arrayOf("03 \rSEARCHING...\rUNABLE TO CONNECT", ""), // throw exception
            // Empty data
            arrayOf("03 \rSEARCHING...\r43 00 \r43 00", listOf<String>()),
            arrayOf("03 \r43 00 \r43 00", listOf<String>()),
            arrayOf("4300", listOf<String>()),
            // Two frames with four dtc
            arrayOf("4300035104A1AB\r43F10600000000", listOf("P0003", "C1104", "B21AB", "U3106")),
            // One frame with three dtc
//            arrayOf("47010301040105", listOf("P0103", "P0104", "P0105")),
//            // One frame with two dtc
//            arrayOf("47010301040000", listOf("P0103", "P0104")),
//            // Two frames with four dtc CAN (ISO-15765) format
//            arrayOf("00A\r0:470401080118\r1:011901200000", listOf("P0108", "P0118", "P0119", "P0120")),
//            // One frame with two dtc CAN (ISO-15765) format
//            arrayOf("470201200121", listOf("P0120", "P0121")),
        )
    }

    @Test
    fun `decode DTCs (Diagnostic trouble codes) responses`() {
        val response = ObdDecoder.parseResponse(input) as DtcResponse
        Assertions.assertEquals(response.troubleCodes, expected)
//        val rpm = response.data.toDouble()
//        Assertions.assertEquals(rpm, expected, 0.01)
    }
}

class ObdMod3DecoderTest {
    @Test
    fun `decode mod3 responses`() {
//        run {
//            val response = ObdDecoder.parseResponse("OK\r\r")
//            Assertions.assertEquals(response.data, "OK")
//        }
//        run {
//            val response = ObdDecoder.parseResponse("NO DATA\r\r")
//            Assertions.assertEquals(response.data, "NO DATA")
//        }
//        run {
//            val response = ObdDecoder.parseResponse("41 0D 40\r\r")
//            Assertions.assertEquals(response.data, "64")
//        }
//        run {
//            val response = ObdDecoder.parseResponse("7EA 03 41 0D 40\r\r")
//            Assertions.assertEquals(response.data, "64")
//        }
    }
}