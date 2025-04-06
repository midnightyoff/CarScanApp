package com.example.carapp.obd2

import org.junit.Test
import org.junit.jupiter.api.Assertions

class ObdDecoderTest {
    @Test
    fun `decode parse responses`() {
        run {
            val response = ObdDecoder.parseResponse("OK\r\r")
            Assertions.assertEquals(response.data, "OK")
        }
        run {
            val response = ObdDecoder.parseResponse("NO DATA\r\r")
            Assertions.assertEquals(response.data, "NO DATA")
        }
        run {
            val response = ObdDecoder.parseResponse("41 0D 40\r\r")
            Assertions.assertEquals(response.data, "64")
        }
        run {
            val response = ObdDecoder.parseResponse("7EA 03 41 0D 40\r\r")
            Assertions.assertEquals(response.data, "64")
        }
    }
}
