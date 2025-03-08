package com.example.carapp

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
