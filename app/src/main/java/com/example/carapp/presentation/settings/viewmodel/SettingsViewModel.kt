package com.example.carapp.presentation.settings.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.carapp.data.local.AppDatabase
import com.example.carapp.data.model.CarEntity
import com.example.carapp.domain.model.Car
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val carDao = AppDatabase.getInstance(application).carDao()

    val units = mutableStateOf(Units.KILOMETERS)

    fun addCar(car: Car) {
        viewModelScope.launch {
            carDao.insert(car.toEntity())
        }
    }

}

enum class Units { KILOMETERS, MILES }


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

