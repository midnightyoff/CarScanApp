package com.example.carapp.obd2

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class ObdAsyncSender(private val connection: ObdConnection) {
    suspend fun send(
        command: ObdCommand,
        timeout: Long = 5000 /* ms */
    ): ObdResponse = runBlocking {
        withContext(Dispatchers.IO) {
            clearBuffer(connection.inputStream)
            connection.writeAndFlush(command)
            val response = readResponseWithTimeout(connection.inputStream, timeout)
            ObdDecoder.parseResponse(response)
        }
    }
}