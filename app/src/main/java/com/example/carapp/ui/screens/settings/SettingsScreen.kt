package com.example.carapp.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carapp.presentation.settings.viewmodel.SettingsViewModel
import com.example.carapp.presentation.settings.viewmodel.Units

@Composable
fun SettingsScreen() {
    val viewModel: SettingsViewModel = viewModel()
    val showAddCarDialog = remember { mutableStateOf(false) }
    val showFaqDialog = remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            SettingsCard(
                title = "FAQ",
                icon = Icons.AutoMirrored.Filled.Help,
                onClick = { showFaqDialog.value = true }
            )
        }

        item {
            UnitsCard(
                currentUnits = viewModel.units.value,
                onUnitsChanged = { viewModel.units.value = it }
            )
        }

        item {
            SettingsCard(
                title = "Bluetooth подключение",
                icon = Icons.Default.Bluetooth,
                onClick = { /* Заглушка */ }
            )
        }

        item {
            SettingsCard(
                title = "Добавить автомобиль",
                icon = Icons.Default.DirectionsCar,
                onClick = { showAddCarDialog.value = true }
            )
        }
    }

    if (showAddCarDialog.value) {
        AddCarDialog(
            onDismiss = { showAddCarDialog.value = false },
            onSave = { car -> viewModel.addCar(car) }
        )
    }

    if (showFaqDialog.value) {
        FaqDialog(onDismiss = { showFaqDialog.value = false })
    }
}

