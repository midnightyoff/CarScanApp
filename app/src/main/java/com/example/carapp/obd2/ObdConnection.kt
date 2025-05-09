package com.example.carapp.obd2

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.io.InputStream
import java.io.OutputStream

fun readResponse(inputStream: InputStream): String {
    val result = StringBuffer()
    while (inputStream.available() > 0) {
        val byte = inputStream.read().toByte()
        if (byte < 0)
            break

        val char = byte.toInt().toChar()
        if ('>' == char)
            break

        result.append(char)
    }
    return result.toString().trim()
}

suspend fun readResponseWithTimeout(inputStream: InputStream, timeout: Long): String = withContext(Dispatchers.IO) {
    require(timeout > 0) { "Timeout must be positive" }
    withTimeout(timeout) {
        val result = StringBuffer()
        while (true) {
            if (inputStream.available() > 0) {
                val byte = inputStream.read().toByte()
                if (byte < 0) { // end of stream reached
                    break
                }

                val char = byte.toInt().toChar()
                if ('>' == char) { // response was read in full
                    break
                }

                result.append(char)
            } else {
                delay(100)
            }
        }
        result.toString()
    }
}

fun clearBuffer(inputStream: InputStream) {
    while (inputStream.available() > 0) {
        inputStream.read()
    }
}

class ObdConnection(
    val inputStream: InputStream,
    val outputStream: OutputStream
) {
    init {
//        ATZ reset all
//        ATL0 linefeeds off
//        ATE1 echo on
//        ATH1 Headers  on
//        ATAT1 adaptive timing auto1
//        ATSTFF
//        ATDP describe current protocol
//        ATSP00 save protocol to Auto and save it
//        val initializeCommands = listOf<ObdCommand>("ATZ", "ATL0", "ATE1", "ATH1", "ATAT1", "ATSTFF", "ATDP", "ATSP0")
//        for (cmd in initializeCommands)
//            send(cmd)
    }
    fun writeAndFlush(command: ObdCommand) {
        outputStream.write("${command.value}\r".toByteArray())
        outputStream.flush()
    }
//    fun readResponse(): String {
//        val result = StringBuffer()
//        while (inputStream.available() > 0) {
//            val byte = inputStream.read().toByte()
//            if (byte < 0) // это типа EOF?
//                break
//
//            val char = byte.toInt().toChar()
//            if ('>' == char)
//                break
//
//            result.append(char)
//        }
//        return result.toString().trim()
//    }
//    }

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