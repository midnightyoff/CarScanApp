package com.example.carapp.obd2

import com.example.carapp.obd2.mod1.VehicleSpeed
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

class ObdDecoderTest {
    @Test
    fun `decode parse responses`() {
        run {
            val response = ObdDecoder.parseResponse("OK\r\r")
            Assertions.assertEquals(response.value, "OK")
        }
        run {
            val response = ObdDecoder.parseResponse("NO DATA\r\r")
            Assertions.assertEquals(response.value, "NO DATA")
        }
        run {
            val response = ObdDecoder.parseResponse("41 0D 40\r\r")
            Assertions.assertEquals(response.value, "64")
        }
        run {
            val response = ObdDecoder.parseResponse("7EA 03 41 0D 40\r\r")
            Assertions.assertEquals(response.value, "64")
        }
    }
}