package com.example.carapp.obd2

import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/*

ATZ
"AT Z\r\r\rELM327 v2.1"

03
"03 \rSEARCHING...\rUNABLE TO CONNECT"

AT H1
"AT H1\rOK"

03
"03 \rSEARCHING...\r43 00 \r43 00"

03
"03 \r43 00 \r43 00"

010D
"01 0D\r41 0D 00 \r41 0D 00"

0100
"01 00\r41 00 BF BF A8 93 \r41 00 88 18 00 03"

0902
"09 02\r014 \r0: 49 02 01 34 53 34 \r1: 42 53 45 4E 43 31 4A \r2: 33 33 33 37 39 39 39"

ATE0
"AT E0\rOK"

ATE0
"OK"

410D
"41 0D 00 \r41 0D 00"

0902
"014 \r0: 49 02 01 34 53 34 \r1: 42 53 45 4E 43 31 4A \r2: 33 33 33 37 39 39 39"

ATH0
"OK"

0902
"014 \r0: 49 02 01 34 53 34 \r1: 42 53 45 4E 43 31 4A \r2: 33 33 33 37 39 39 39"

*/

@RunWith(Parameterized::class)
class ObdStatusResponseTestt(private val input: String, private val expected: String) {
        companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun values() = listOf(
            arrayOf("03 \rSEARCHING...\rUNABLE TO CONNECT", "UNABLE TO CONNECT"),
            arrayOf("AT H1\rOK", "OK")
        )
    }

    @Test
    fun `decode speed`() {
        val response = ObdDecoder.parseResponse(input)
        Assertions.assertEquals(response.data, expected)
    }
}