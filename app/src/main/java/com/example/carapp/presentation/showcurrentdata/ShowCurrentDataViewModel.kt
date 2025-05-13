package com.example.carapp.presentation.showcurrentdata

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carapp.obd2.ObdConnection
import com.example.carapp.obd2.ObdPollingManager
import com.example.carapp.obd2.mod1.EngineSpeed
import com.example.carapp.obd2.mod1.VehicleSpeed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


class ShowCurrentDataViewModel(connection: ObdConnection) : ViewModel() {
//    private val _sensors = MutableStateFlow<List<ObdSensorData>>(listOf(ObdSensorData("OD", ""))) // постоянно обновляющиеся датчики привязанные к какой либо команде из Mode 01 Show current data
//    val sensors: StateFlow<List<ObdSensorData>> = _sensors


    private val monitor: ObdPollingManager = ObdPollingManager(connection)

    private val _speed = mutableStateOf("0")
    val speed: State<String> = _speed

    private val _rpm = mutableStateOf("0")
    val rpm: State<String> = _rpm

    private val _coolantTemp = mutableStateOf("0")
    val coolantTemp: State<String> = _coolantTemp

//    init {
//        monitor.subscribe(VehicleSpeed()) { response ->
//            val newList = listOf(ObdSensorData("OD", response.data))
//            _sensors.update { value -> newList }
//            _sensors.value = newList
//        }
//    }

    init {
        setupSubscriptions()
    }

    private fun setupSubscriptions() {
        monitor.subscribe(VehicleSpeed()) { response ->
            _speed.value = response.data
        }

        monitor.subscribe(EngineSpeed()) { response ->
            _rpm.value = response.data
        }

//        monitor.subscribe(CoolantTemp()) { response ->
//            _coolantTemp.value = response.data
//        }
    }

    fun startMonitoring() {
        monitor.start()

    }

    fun stopMonitoring() {
        monitor.stop()
    }

//    private fun startSimulation() {
//        viewModelScope.launch {
//            var speed = 40f
//            var rpm = 1500f
//            var temp = 85f
//
//            while (true) {
//                // Плавное изменение значений
//                speed = (speed + Random.nextFloat() * 6 - 3).coerceIn(0f, 220f)
//                rpm = (rpm + Random.nextFloat() * 400 - 200).coerceIn(0f, 8000f)
//                temp = (temp + Random.nextFloat() * 4 - 2).coerceIn(-40f, 120f)
//
//                _speed.value = speed.toString()
//                _rpm.value = rpm.toString()
//                _coolantTemp.value = temp.toString()
//
//                delay(500) // Обновление 2 раза в секунду
//            }
//        }
//    }
}