package com.example.carapp.ui.screens.mainscreen

import android.content.Context
import android.widget.Toast
import com.github.eltonvs.obd.command.engine.SpeedCommand
import com.github.eltonvs.obd.connection.ObdDeviceConnection
import java.io.InputStream
import java.io.OutputStream


class ObdConnection(private val inputStream: InputStream,
                    private val outputStream: OutputStream
) {
    val obdConnection = ObdDeviceConnection(inputStream, outputStream)
    suspend fun initialize(context: Context) {
        val response = obdConnection.run(SpeedCommand())
        Toast.makeText(context, response.value, Toast.LENGTH_SHORT).show()
    }
}