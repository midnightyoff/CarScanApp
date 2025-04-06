package com.example.carapp.obd2

import com.example.carapp.obd2.mod3.DtcDecoder
import com.example.carapp.obd2.mod3.ObdDtc
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

//@RunWith(Parameterized::class)
//class DtcDeccoderTest(private val input: String, private val expected: ObdDtc) {
//        companion object {
//        @JvmStatic
//        @Parameterized.Parameters
//        fun values() = listOf(
//            arrayOf("0133", ObdDtc("P0133", "O2 Sensor Circuit Slow Response (Bank 1 Sensor 1)"))
//        )
//    }
//
//    @Test
//    fun `parse obd DTC category`() {
//        val dtc = DtcDecoder.decode(input)
//        Assertions.assertEquals(dtc.dtc, expected.dtc)
//        Assertions.assertEquals(dtc.description, expected.description)
//    }
//}
