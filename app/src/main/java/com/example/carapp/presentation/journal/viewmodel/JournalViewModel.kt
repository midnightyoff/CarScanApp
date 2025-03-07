package com.example.carapp.presentation.journal.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.carapp.data.local.AppDatabase
import com.example.carapp.data.model.JournalItemEntity
import com.example.carapp.domain.model.JournalItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Date


class JournalViewModel(application: Application, val carId: Int?) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    private val dao = database.journalDao()


    val journalItems: Flow<List<JournalItem>> = if (carId != null) {
        dao.getByCarId(carId).map { entities ->
            entities.map { it.toJournalItem() }
        }
    } else {
        flowOf(emptyList())
    }

    var showDialog = mutableStateOf(false)
    var selectedItem = mutableStateOf<JournalItem?>(null)
    var currentType = mutableStateOf<JournalType?>(null)
    var currentFilter = mutableStateOf<JournalType?>(null)

    fun addItem(item: JournalItem) {
        if (carId == null) return
        viewModelScope.launch {
            dao.insert(item.toEntity())
        }
    }

    fun deleteItem(itemId: String) {
        viewModelScope.launch {
            dao.deleteById(itemId)
        }
    }
}

enum class JournalType { MAINTENANCE, REFUEL }


private fun JournalItem.toEntity() = when (this) {
    is JournalItem.Maintenance -> JournalItemEntity(
        id = id,
        carId = carId,
        type = "maintenance",
        title = title,
        date = date.time,
        mileage = mileage,
        price = price,
        description = description
    )
    is JournalItem.Refuel -> JournalItemEntity(
        id = id,
        carId = carId,
        type = "refuel",
        title = title,
        date = date.time,
        mileage = mileage,
        price = price,
        description = description,
        fuelType = fuelType,
        fuelVolume = fuelVolume,
        pricePerLiter = pricePerLiter
    )
}

private fun JournalItemEntity.toJournalItem() = when (type) {
    "maintenance" -> JournalItem.Maintenance(
        id = id,
        carId = carId,
        title = title,
        date = Date(date),
        mileage = mileage,
        price = price,
        description = description
    )
    "refuel" -> JournalItem.Refuel(
        id = id,
        carId = carId,
        title = title,
        date = Date(date),
        mileage = mileage,
        fuelType = fuelType!!,
        fuelVolume = fuelVolume!!,
        pricePerLiter = pricePerLiter!!,
        description = description
    )
    else -> throw IllegalArgumentException("Unknown type")
}

