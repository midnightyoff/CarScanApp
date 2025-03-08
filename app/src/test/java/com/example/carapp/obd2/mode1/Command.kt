package com.example.carapp.obd2.mode1

import com.example.carapp.obd2.mod1.EngineSpeed
import com.example.carapp.obd2.mod1.VehicleSpeed
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class VehicleSpeedTest(private val input: List<String>, private val expected: Int) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun values() = listOf(
//            arrayOf("410D00", 0),
//            arrayOf("410DFF", 255),
//            arrayOf("410D15", 21),
//            arrayOf("410D40", 64)

            arrayOf(listOf("00"), 0),
            arrayOf(listOf("FF"), 255),
            arrayOf(listOf("15"), 21),
            arrayOf(listOf("40"), 64)
        )
    }

    @Test
    fun `decode speed`() {
        val response = VehicleSpeed().decode(input)
        val speed = response.value.toInt()
        Assertions.assertEquals(speed, expected)
    }
}

@RunWith(Parameterized::class)
class EngineSpeedTest(private val input: List<String>, private val expected: Double) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun values() = listOf(
//            arrayOf("410C0000", 0.0),
//            arrayOf("410CFFFF", 16_383.75),
//            arrayOf("410C200D", 2051.25)

            arrayOf(listOf("00"), 0.0),
            arrayOf(listOf("FF"), 16_383.75),
            arrayOf(listOf("0D"), 2051.25)
        )
    }

    @Test
    fun `decode speed`() {
        val response = EngineSpeed().decode(input)
        val rpm = response.value.toDouble()
        Assertions.assertEquals(rpm, expected, 0.01)
    }
}