package com.example.carapp.ui.screens.mainscreen

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.carapp.obd2.ObdConnection
import java.util.UUID

class Elm327BluetoothAdapter(val socket: BluetoothSocket/*device: BluetoothDevice*/) {
    val connection: ObdConnection
    init {
        connection = ObdConnection(socket.inputStream, socket.outputStream)
    }
    // TODO вызвать в деструкторе, в kotlin нет деструкторов, найти замену
    protected fun close() {
        socket.close()
    }
    companion object {
        // UUID для ELM327
        private val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        fun uuid(): UUID {
            return uuid
        }
    }
}