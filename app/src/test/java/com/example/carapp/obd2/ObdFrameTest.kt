package com.example.carapp.obd2

import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ObdFrameTest(private val input: String, private val expected: ObdFrame) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun values() = listOf(
            // 41 replaced to 01
            arrayOf("41 0D 0C \r\r>", ObdFrame("", 3, "01", "0D", listOf("0C"))), // header
            arrayOf("7EA 03 41 0D 0C \r\r>", ObdFrame("7EA", 3, "01", "0D", listOf("0C"))),
            arrayOf("18DAF110 03 41 0D 09 \r\r>", ObdFrame("18DAF110", 3, "01", "0D", listOf("09")))
        )
    }

    @Test
    fun `parse obd frame`() {
        val frame = ObdFrame.parseResponse(input)
        Assertions.assertEquals(frame.identifier, expected.identifier)
        Assertions.assertEquals(frame.length, expected.length)
        Assertions.assertEquals(frame.mode, expected.mode)
        Assertions.assertEquals(frame.pid, expected.pid)
        Assertions.assertEquals(frame.data, expected.data)
    }
    @Test
    fun `obd frame test isCANIdentifier method`() {
        val validCanIdentifiers = arrayOf(0x7DF, 0x7E8, 0x7EF, 0x7E8, 0x18DB33F1, 0x18DAF100, 0x18DAF1FF, 0x18DAF110, 0x18DAF11E)
        for (canIdentifier in validCanIdentifiers)
            Assertions.assertTrue(ObdFrame.isCANIdentifier(canIdentifier))

        val invalidCanIdentifiers = arrayOf(0x01, 0x41, 0x0D)
        for (canIdentifier in invalidCanIdentifiers)
            Assertions.assertFalse(ObdFrame.isCANIdentifier(canIdentifier))
    }
}

