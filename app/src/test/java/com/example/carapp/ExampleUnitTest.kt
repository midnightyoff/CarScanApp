package com.example.carapp

import com.example.carapp.obd2.ObdCommand
import com.example.carapp.obd2.ObdConnection
import com.example.carapp.obd2.mod1.VehicleSpeed
import org.junit.Test

import org.junit.Assert.*
import java.io.InputStream
import java.io.OutputStream

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        val input = "7E8 03 41 0D 2C \r7EA 03 41 0D 2C \r\r>"
//        val result = input.substringAfter('\r')
//
//        val cleanedResponse = "410CA5"
//        // 410C 0A 50
//        val mode = /*"0" + */cleanedResponse.substring(1,2)
//        val pid = cleanedResponse.substring(2,4)
//        val data = cleanedResponse.substring(4)
        assertEquals(4, 2 + 2)
//         val inputStream = InputStream()
//         val outputStream = OutputStream()
//
//        val obdConnection = ObdConnection(inputStream, outputStream)
//        val response = obdConnection.send(VehicleSpeed())
//        response.value // скорость автомобиля

    }
}