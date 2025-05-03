package com.example.carapp.obd2.mod3

import com.example.carapp.obd2.mod1.EngineSpeed
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class DiagnosticTroubleCodes(private val input: List<String>, private val expected: Double) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun values() = listOf(
            arrayOf(listOf("03 \rSEARCHING...\rUNABLE TO CONNECT"), ),
            arrayOf(listOf("03 \rSEARCHING...\r43 00 \r43 00"), ),
            arrayOf(listOf("03 \r43 00 \r43 00"), )
        )
    }

    @Test
    fun `decode DTCs (Diagnostic trouble codes) responses`() {
        val response = ShowDiagnosticTroubleCodes().decode(input)
//        val rpm = response.data.toDouble()
//        Assertions.assertEquals(rpm, expected, 0.01)
    }
}