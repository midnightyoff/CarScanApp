package com.example.carapp.obd2

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.InputStream
import java.util.LinkedList

/*interface Observer {
    fun update()
}

open class Observable {
    private var observers = mutableListOf<Observer>()

    fun notifyALl() {
        for(obs in observers)
            obs.update()
    }

    fun add(obs : Observer) {
        observers.add(obs)
    }
    fun remove(obs : Observer) {
        observers.remove(obs)
    }
}

class Mod1Sensor : Observable() {
    var response: ObdMod1Response = ObdMod1Response("", "", "")
        set(value) {
            field = value
            notifyALl()
        }
}*/
/* TODO
class Monitor(val sensor: Mod1Sensor) : Observer {
    init {
        sensor.add(this)
    }
    override fun update() { // update UI
        val response = sensor.response
        println("update Monitor")
    }
}
*/
typealias MeasurementCallback = (response: MeasurementResponse) -> Unit

//interface MeasurementCommandListener {
//    fun onNewData(response: MeasurementResponse)
//}

class ObdResponsesBuffer(private val inputStream: InputStream) {
    private val responses: MutableList<String> = mutableListOf()
    private val mutex = Mutex()
    private var partialResponse = StringBuilder() // read data
    suspend fun readAvailableResponses() {
        mutex.withLock {
            while (inputStream.available() > 0) {
                val byte = inputStream.read().toByte()
                if (byte < 0) break

                val char = byte.toInt().toChar()
                if (char == '>') {
                    val completeResponse = partialResponse.toString().trim()
                    if (completeResponse.isNotEmpty()) {
                        responses.add(completeResponse)
                    }
                    partialResponse.clear()
                } else {
                    partialResponse.append(char)
                }
            }
        }
    }
    suspend fun takeAllResponses(): List<String> {
        mutex.withLock {
            val result = responses.toList()
            responses.clear()
            return result
        }
    }
}

class ObdPollingManager(
    private val obdConnection: ObdConnection,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    val responseBuffer = ObdResponsesBuffer(obdConnection.inputStream)
    //    private val sensors = HashMap<String /* PID */, Mod1Sensor>()
    private val measurementListeners =
        HashMap<ObdCommand, MutableList<MeasurementCallback>>()

    fun subscribe(command: ObdCommand, callback: MeasurementCallback) { // TODO thread safe
        measurementListeners.getOrPut(command) { mutableListOf() }.add(callback)
    }

    fun unsubscribe(command: ObdCommand, callback: MeasurementCallback) {
        measurementListeners[command]?.remove(callback)
    }

    private fun updateListeners(command: ObdCommand, response: MeasurementResponse) {
        val listeners = measurementListeners[command] ?: return
        for (listener in listeners)
            listener.invoke(response)
    }
    /**
        \param[in] interval time in ms
    */
    fun start(interval: Long = 500L) { // startPolling
        coroutineScope.launch {
            while (isActive) {
                for (command in measurementListeners.keys) {
                    try {
                        obdConnection.writeAndFlush(command)
                        delay(interval)
                        responseBuffer.readAvailableResponses()
                        for (data in responseBuffer.takeAllResponses()) {
                            when (val response = ObdDecoder.parseResponse(data)) {
                                is MeasurementResponse -> {
                                    updateListeners(command, response)
                                }

                                is ObdErrorResponse -> {
                                    // TODO
                                }

                                is DtcResponse -> {
                                    // TODO
                                }
                            }
                        }

                    } catch (e: Exception) {
                        System.err.println("Error reading command: $command - ${e.message}")
                    }
                }
                delay(interval)
            }
        }
    }

    fun stop() {
        coroutineScope.cancel()
    }

    fun destroy() {
        clear()
        stop()
    }

    private fun clear() {
        measurementListeners.clear()
    }
}