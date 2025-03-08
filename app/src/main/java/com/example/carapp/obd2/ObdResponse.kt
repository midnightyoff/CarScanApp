package com.example.carapp.obd2

open class ObdResponse(
    val value: String
) {
    override fun toString(): String {
        return value
    }
}