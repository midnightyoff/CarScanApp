package com.example.carapp.obd2

import kotlinx.coroutines.delay

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
interface MeasurementCommandListener {
    fun onNewData(response: MeasurementResponse)
}

class ObdPollingManager(
    private val obdConnection: ObdConnection,
) {
    //    private val sensors = HashMap<String /* PID */, Mod1Sensor>()
    private val measurementListeners =
        HashMap<ObdCommand, MutableList<MeasurementCommandListener>>()
    private var isActive = false

    fun subscribe(command: ObdCommand, listener: MeasurementCommandListener) { // TODO thread safe
        measurementListeners.getOrPut(command) { mutableListOf() }.add(listener)
    }

    fun unsubscribe(command: ObdCommand, listener: MeasurementCommandListener) {
        measurementListeners[command]?.remove(listener)
    }

    private fun updateListeners(command: ObdCommand, response: MeasurementResponse) {
        val listeners = measurementListeners[command] ?: return
        for (listener in listeners)
            listener.onNewData(response)
    }

    /**
        \param[in] interval time in ms
    */
    suspend fun start(interval: Long = 500L) {
        if (isActive) return
        isActive = true
        while (isActive) {
            measurementListeners.keys.forEach { command ->
                try {
                    val response = obdConnection.send(command)
                    when (response) {
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

                } catch (e: Exception) {
                    System.err.println("Error reading command: $command - ${e.message}")
                }
            }
            delay(interval)
        }
    }

    fun stop() {
        isActive = false
    }

    fun clear() {
        measurementListeners.clear()
    }
}