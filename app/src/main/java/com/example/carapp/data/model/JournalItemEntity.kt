package com.example.carapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "journal_items",
    foreignKeys = [ForeignKey(
        entity = CarEntity::class,
        parentColumns = ["id"],
        childColumns = ["carId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class JournalItemEntity(
    @PrimaryKey val id: String,
    val carId: Int,
    val type: String, // "maintenance" или "refuel"
    val title: String,
    val date: Long,
    val mileage: Int,
    val price: Double,
    val description: String,
    val fuelType: String? = null,
    val fuelVolume: Double? = null,
    val pricePerLiter: Double? = null
)