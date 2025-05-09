package com.example.carapp.presentation.showcurrentdata

import androidx.lifecycle.ViewModel
import com.example.carapp.domain.model.DiagnosticError
import com.example.carapp.obd2.MeasurementCallback
import com.example.carapp.obd2.ObdConnection
import com.example.carapp.obd2.ObdPollingManager
import com.example.carapp.obd2.mod1.EngineSpeed
import com.example.carapp.obd2.mod1.VehicleSpeed
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class ObdSensorData(val pid: String, var value: String)

class ShowCurrentDataViewModel(connection: ObdConnection) : ViewModel() {
    private val _sensors = MutableStateFlow<List<ObdSensorData>>(listOf(ObdSensorData("OD", ""))) // постоянно обновляющиеся датчики привязанные к какой либо команде из Mode 01 Show current data
    val sensors: StateFlow<List<ObdSensorData>> = _sensors
//    private var monitoringJob: Job? = null

    private val monitor: ObdPollingManager = ObdPollingManager(connection)
    init {
        monitor.subscribe(VehicleSpeed()) { response ->
            val newList = listOf(ObdSensorData("OD", response.data))
            _sensors.update { value -> newList }
            _sensors.value = newList
        }
    }

//    fun updateSensor(pid: String, data: String) {
//        for (sensor in _sensors.value) {
//            if (pid == sensor.pid) {
//                sensor.value = data
//            }
//        }
//    }
    fun startMonitoring() {
    monitor.start()
//        monitoringJob?.cancel()
//    monitoringJob?.start()
    }

    fun stopMonitoring() {
        monitor.stop()
//        monitoringJob?.cancel()
    }
}