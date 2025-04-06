package com.example.carapp.obd2

import com.example.carapp.obd2.mod1.VehicleSpeed
import com.example.carapp.obd2.mod3.DtcDecoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream

class ObdConnection(
    private val inputStream: InputStream,
    private val outputStream: OutputStream
) {
    fun readResponse(): String {
        val result = StringBuffer()
        while (inputStream.available() > 0) {
            val byte = inputStream.read().toByte()
            if (byte < 0) // это типа EOF?
                break

            val char = byte.toInt().toChar()
            if ('>' == char)
                break

            result.append(char)
        }
        return result.toString().trim()
    }

    suspend fun send(command: ObdCommand): ObdResponse = runBlocking {
        withContext(Dispatchers.IO) {
            outputStream.write("${command.value}\r".toByteArray())
            outputStream.flush()
            val response = readResponse()
            ObdDecoder.parseResponse(response)
        }
    }

//    suspend fun readDtc(): ObdResponse = runBlocking { // TODO вынести выше, класс будет содержать connection?
//        withContext(Dispatchers.IO) {
//            outputStream.write("03\r".toByteArray())
//            outputStream.flush()
//            val response = readResponse()
////            val frame = ObdFrame.parseResponse(response)
//            val troubleCodes = DtcDecoder.decode(response)
//            DtcResponse("", troubleCodes) // TODO rawValue
//        }
//    }
}