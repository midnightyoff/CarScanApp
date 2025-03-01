package com.example.carapp.data.model

data class Car(
    val id: Int = 0,
    val brand: String,
    val model: String,
    val year: Int,
    val description: String? = null
)