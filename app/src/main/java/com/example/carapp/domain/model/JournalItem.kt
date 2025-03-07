package com.example.carapp.domain.model

import java.util.Date

sealed class JournalItem(
    open val id: String,
    open val carId: Int,
    open val title: String,
    open val date: Date,
    open val mileage: Int,
    open val price: Double,
    open val description: String
) {
    data class Maintenance(
        override val id: String,
        override val carId: Int,
        override val title: String,
        override val date: Date,
        override val mileage: Int,
        override val price: Double,
        override val description: String
    ) : JournalItem(id, carId, title, date, mileage, price, description)

    data class Refuel(
        override val id: String,
        override val carId: Int,
        override val title: String,
        override val date: Date,
        override val mileage: Int,
        val fuelType: String,
        val fuelVolume: Double,
        val pricePerLiter: Double,
        override val description: String
    ) : JournalItem(
        id,
        carId,
        title,
        date,
        mileage,
        price = fuelVolume * pricePerLiter,
        description
    )
}