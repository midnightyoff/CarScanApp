package com.example.carapp.ui.screens.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carapp.data.bluetooth.BluetoothManager
import com.example.carapp.presentation.settings.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen() {
    val viewModel: SettingsViewModel = viewModel()
    val showAddCarDialog = remember { mutableStateOf(false) }
    val showFaqDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as Activity
    val bluetoothManager = remember { BluetoothManager(context) }

    val appSettingsIntent = remember {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
    }

    val settingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { }



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
                onClick = {
                    if (bluetoothManager.hasPermissions()) {
                        openBluetoothSettings(activity)
                    } else {
                        settingsLauncher.launch(appSettingsIntent)
                    }
                }
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

private fun openBluetoothSettings(activity: Activity) {
    val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
    if (intent.resolveActivity(activity.packageManager) != null) {
        activity.startActivity(intent)
    } else {
        Toast.makeText(activity, "Не удалось открыть настройки Bluetooth", Toast.LENGTH_SHORT).show()
    }
}


