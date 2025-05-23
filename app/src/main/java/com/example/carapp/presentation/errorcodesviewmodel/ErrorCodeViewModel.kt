package com.example.carapp.presentation.errorcodesviewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.carapp.domain.model.DiagnosticError
import com.example.carapp.obd2.mod3.ObdDtc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ErrorCodesViewModel(application: Application) : AndroidViewModel(application) {
    private val _errors = MutableStateFlow<List<DiagnosticError>>(emptyList())
    val errors: StateFlow<List<DiagnosticError>> = _errors

    init {
        loadMockData()

    }
    fun setErrors(troubleCodes: List<String>) {
        var errors = mutableListOf<DiagnosticError>()
        for (troubleCode in troubleCodes) {
            errors.add(DiagnosticError(troubleCode, "", "", true))
        }
        _errors.value = errors
    }
    private fun loadMockData() {
        _errors.value = listOf(
            DiagnosticError(
                code = "P0171",
                title = "Система слишком бедная",
                description = "Слишком бедная топливовоздушная смесь",
                isActive = true
            ),
            DiagnosticError(
                code = "P0300",
                title = "Пропуски зажигания",
                description = "Обнаружены случайные пропуски зажигания",
                isActive = false
            )
        )
    }
}