package com.example.carapp.obd2

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ObdSyncSender(private val connection: ObdConnection) {
    suspend fun send(
        command: ObdCommand,
        timeout: Long = 1000 /* ms */
    ): ObdResponse = runBlocking {
        require(timeout > 0) { "Timeout must be positive" }
        withContext(Dispatchers.IO) {
            clearBuffer(connection.inputStream)
            connection.writeAndFlush(command)
            val response = readResponseWithTimeout(timeout)
            ObdDecoder.parseResponse(response)
        }
    }

    suspend fun runCommand(
        command: ObdCommand,
        timeout: Long = 1000 /* ms */
    ): ObdResponse = runBlocking {
        require(timeout > 0) { "Timeout must be positive" }
        withContext(Dispatchers.IO) {
            clearBuffer(connection.inputStream)
            connection.writeAndFlush(command)
            val response = readResponseWithTimeout(timeout)
            ObdResponse(response)
        }
    }

    private fun readResponseWithTimeout(timeout: Long): String {
        val inputStream = connection.inputStream
        val result = StringBuffer()
        val startTime = System.currentTimeMillis()
        while (System.currentTimeMillis() - startTime < timeout || inputStream.available() > 0) {
            while (inputStream.available() > 0) {
                val byte = inputStream.read().toByte()
                if (byte < 0) // это типа EOF?
                    return result.toString()

                val char = byte.toInt().toChar()
                if ('>' == char)
                    return result.toString()

                result.append(char)
            }
        }
        return result.toString()
    }

}