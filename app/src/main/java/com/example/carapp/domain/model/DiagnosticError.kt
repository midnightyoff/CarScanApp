package com.example.carapp.domain.model

data class DiagnosticError(
    val code: String,
    val title: String,
    val description: String,
    val isActive: Boolean
)