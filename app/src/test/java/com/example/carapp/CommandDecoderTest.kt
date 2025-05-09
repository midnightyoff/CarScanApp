package com.example.carapp

import com.example.carapp.obd2.ObdDecoder
import com.example.carapp.obd2.mod1.EngineSpeed
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.junit.runners.Parameterized



//@RunWith(Parameterized::class)
//class CommandDecoderTest(private val input: String, private val expected: String) {
//    companion object {
//        @JvmStatic
//        @Parameterized.Parameters
//        fun values() = listOf(
//            arrayOf("410C0000", 0.0),
//            arrayOf("410CFFFF", 16_383.75),
//            arrayOf("410C200D", 2051.25)
//        )
//    }
//}
//
//
//@RunWith(Parameterized::class)
//class EngineSpeedTest(private val input: String, private val expected: Double) {
//    companion object {
//        @JvmStatic
//        @Parameterized.Parameters
//        fun values() = listOf(
//            arrayOf("410C0000", 0.0),
//            arrayOf("410CFFFF", 16_383.75),
//            arrayOf("410C200D", 2051.25)
//        )
//    }
//
//    @Test
//    fun `decode speed`() {
//        val response = EngineSpeed().decode(input)
//        val rpm = response.value.toDouble()
//        Assertions.assertEquals(rpm, expected, 0.01)
//    }
//}

class ObdMod3DecoderTest {
    @Test
    fun `decode responses`() {
        run {
            // AT E0
            val response = ObdDecoder.parseResponse("OK\r\r")
            Assertions.assertEquals(response.data, "OK")
        }
        run {
            // AT Z
            val response = ObdDecoder.parseResponse("AT Z\r\r\rELM327 v2.1")
            Assertions.assertEquals(response.data, "ELM327 v2.1")
           val responseEchoOff = ObdDecoder.parseResponse("\r\rELM327 v2.1")
            Assertions.assertEquals(responseEchoOff.data, "ELM327 v2.1")
        }
//        run {
//            val response = ObdDecoder.parseResponse("NO DATA\r\r")
//            Assertions.assertEquals(response.data, "NO DATA")
//        }
//        run {
//            val response = ObdDecoder.parseResponse("03 \rSEARCHING...\rUNABLE TO CONNECT")
//            Assertions.assertEquals(response.data, "64")
//        }
//        run {
//            val response = ObdDecoder.parseResponse("7EA 03 41 0D 40\r\r")
//            Assertions.assertEquals(response.data, "64")
//        }
    }
}
