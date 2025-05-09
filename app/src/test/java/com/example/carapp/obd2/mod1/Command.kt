package com.example.carapp.obd2.mod1

import com.example.carapp.obd2.ObdDecoder
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class VehicleSpeedTest(private val input: String, private val expected: Int) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun values() = listOf(
//            arrayOf("410D00", 0),
//            arrayOf("410DFF", 255),
//            arrayOf("410D15", 21),
//            arrayOf("410D40", 64)

            arrayOf("00", 0),
            arrayOf("FF", 255),
            arrayOf("15", 21),
            arrayOf("40", 64)
        )
    }

    @Test
    fun `decode speed`() {
        val response = VehicleSpeed().decode(input)
        val speed = response.data.toInt()
        Assertions.assertEquals(speed, expected)
    }
}

@RunWith(Parameterized::class)
class EngineSpeedTest(private val input: String, private val expected: Double) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun values() = listOf(
//            arrayOf("410C0000", 0.0),
//            arrayOf("410CFFFF", 16_383.75),
//            arrayOf("410C200D", 2051.25)

            arrayOf("0000", 0.0),
            arrayOf("FFFF", 16_383.75),
            arrayOf("200D", 2051.25)
        )
    }

    @Test
    fun `decode speed`() {
        val response = EngineSpeed().decode(input)
        val rpm = response.data.toDouble()
        Assertions.assertEquals(rpm, expected, 0.01)
    }
}

class ObdMode1DecoderTest {
    @Test
    fun `decode mod1 responses`() {
        run {
            val response = ObdDecoder.parseResponse("41 0D 04 \r41 0D 04 \r\r")
            Assertions.assertEquals(response.data, "4")
        }
    }
}