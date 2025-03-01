package com.example.carapp.presentation.mainviewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.carapp.data.local.AppDatabase
import com.example.carapp.data.local.CarEntity
import com.example.carapp.data.model.Car
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val carDao = AppDatabase.getInstance(application).carDao()
    private val prefs = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    val cars = carDao.getAll().map { it.map(CarEntity::toCar) }
    val selectedCar = mutableStateOf<Car?>(null)

    init {
        loadSelectedCar()
    }

    private fun loadSelectedCar() {
        viewModelScope.launch {
            val carId = prefs.getInt("selected_car_id", -1)
            if (carId != -1) {
                carDao.getById(carId)?.let {
                    selectedCar.value = it.toCar()
                }
            }
        }
    }

    fun selectCar(car: Car) {
        selectedCar.value = car
        viewModelScope.launch {
            prefs.edit().putInt("selected_car_id", car.id).apply()
        }
    }

    fun deleteCar(carId: Int) {
        viewModelScope.launch {
            carDao.deleteById(carId)
        }
    }
}

private fun CarEntity.toCar() = Car(
    id = id,
    brand = brand,
    model = model,
    year = year,
    description = description
)

private fun Car.toEntity() = CarEntity(
    id = id,
    brand = brand,
    model = model,
    year = year,
    description = description
)

