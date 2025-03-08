package com.example.carapp.ui.screens.mainscreen

import android.content.Context
import android.widget.Toast
import com.example.carapp.obd2.ObdConnection
import com.example.carapp.obd2.mod1.VehicleSpeed
import java.io.InputStream
import java.io.OutputStream


//class Obd2Connection(private val inputStream: InputStream,
//                     private val outputStream: OutputStream
//) {
//    val obdConnection = ObdConnection(inputStream, outputStream)
//    suspend fun initialize(context: Context) {
//        val response = obdConnection.send(VehicleSpeed())
//        Toast.makeText(context, response.value, Toast.LENGTH_SHORT).show()
//    }
//}