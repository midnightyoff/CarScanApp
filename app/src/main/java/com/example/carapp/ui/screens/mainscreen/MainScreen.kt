package com.example.carapp.ui.screens.mainscreen

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Airplay
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.carapp.domain.model.Car
import com.example.carapp.data.bluetooth.BluetoothManager
import com.example.carapp.obd2.ObdConnection
import com.example.carapp.presentation.ObdConnectionViewModel
import com.example.carapp.presentation.mainviewmodel.MainViewModel
import com.example.carapp.ui.navigation.Screen
import kotlinx.coroutines.launch
import java.io.IOException

@Composable
fun MainScreen(navController: NavController, obdViewModel: ObdConnectionViewModel) {
    val viewModel: MainViewModel = viewModel()
    val context = LocalContext.current
    val activity = context as Activity
    val scope = rememberCoroutineScope()

    val showCarDialog = remember { mutableStateOf(false) }
    val bluetoothManager = remember { BluetoothManager(context) }
    val showDeviceDialog = remember { mutableStateOf(false) }
    val showPermissionDialog = remember { mutableStateOf(false) }

    val showDevices = {
        viewModel.bondedDevices.clear()
        viewModel.bondedDevices.addAll(bluetoothManager.getBondedDevices(context))
    }

    LaunchedEffect(Unit) {
        viewModel.isConnected.value = bluetoothManager.isConnected()
    }

    LaunchedEffect(showDeviceDialog.value) {
        if (showDeviceDialog.value) {
            showDevices()
        }
    }

    val permissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            showDevices()
        } else {
            showPermissionDialog.value = true
        }
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        CarSelectorButton(
            selectedCar = viewModel.selectedCar.value,
            onClick = { showCarDialog.value = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Box(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .size(150.dp)
                .shadow(8.dp, CircleShape)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    when {
                        !bluetoothManager.hasPermissions() -> {
                            permissionsLauncher.launch(BluetoothManager.requiredPermissions())
                            showDeviceDialog.value = false
                        }
                        !bluetoothManager.isBluetoothEnabled() -> {
                            bluetoothManager.enableBluetooth(activity)
                            showDeviceDialog.value = false
                        }
                        else -> {
                            showDeviceDialog.value = true
                        }
                    }
                },
            contentAlignment = Center
        ) {
            Icon(
                imageVector = Icons.Default.Bluetooth,
                contentDescription = "Подключение",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(40.dp)
            )
        }
        Text(
            "Нажмите для подключения",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        // Диалог запроса разрешений
        if (showPermissionDialog.value) {
            AlertDialog(
                onDismissRequest = { showPermissionDialog.value = false },
                title = { Text("Требуются разрешения") },
                text = { Text("Для работы с Bluetooth необходимо предоставить разрешения. Для этого перейдите в настройки приложения") },
                confirmButton = {
                    TextButton({
                        permissionsLauncher.launch(BluetoothManager.requiredPermissions())
                        showPermissionDialog.value = false
                    }) { Text("Повторить") }
                },
                dismissButton = {
                    TextButton({ showPermissionDialog.value = false }) { Text("Отмена") }
                }
            )
        }

        // Диалог списка устройств
        if (showDeviceDialog.value && bluetoothManager.isBluetoothEnabled()
            && bluetoothManager.hasPermissions()
        ) {
            AlertDialog(
                onDismissRequest = {
                    showDeviceDialog.value = false
                },
                title = { Text("Доступные устройства") },
                text = {
                    Column {
                        Text("Сопряженные устройства:", modifier = Modifier.padding(bottom = 8.dp))
                        LazyColumn {
                            items(viewModel.bondedDevices) { device ->
                                BluetoothDeviceItem(
                                    device = device,
                                    isConnected = bluetoothManager.isConnected(device),
                                    onConnect = {
                                        scope.launch {
//                                            if (ActivityCompat.checkSelfPermission(
//                                                    context,
//                                                    Manifest.permission.BLUETOOTH_CONNECT
//                                                ) != PackageManager.PERMISSION_GRANTED
//                                            ) {
//                                                //
//                                                //    ActivityCompat#requestPermissions
//                                                // here to request the missing permissions, and then overriding
//                                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                //                                          int[] grantResults)
//                                                // to handle the case where the user grants the permission. See the documentation
//                                                // for ActivityCompat#requestPermissions for more details.
//                                                return@launch
//                                            }
//                                            try {
//                                                val socket = device.createRfcommSocketToServiceRecord(Elm327BluetoothAdapter.uuid())
//                                                socket.connect()
//                                                obdAdapter = Elm327BluetoothAdapter(socket)
//                                                showDeviceDialog.value = false
//                                            } catch (_: IOException) {
//                                                Toast.makeText(
//                                                    context,
//                                                    "Ошибка подключения",
//                                                    Toast.LENGTH_SHORT
//                                                ).show()
//                                            }

                                            val success = bluetoothManager.connectToDevice(context, device)
                                            if (success) {
                                                viewModel.isConnected.value = true
                                                val input = bluetoothManager.getInputStream()
                                                val output = bluetoothManager.getOutputStream()
                                                if (input != null && output != null) {
                                                    obdViewModel.setConnection(ObdConnection(input, output))
                                                    showDeviceDialog.value = false
                                                }
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Ошибка подключения",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton({
                        showDeviceDialog.value = false
                    }) { Text("Закрыть") }
                }
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ButtonCard(
                    text = "Терминал",
                    icon = Icons.Default.Terminal,
                    onClick = {
                        if (viewModel.isConnected.value)
                            navController.navigate(Screen.Terminal.route)
                        else
                            Toast.makeText(context,"Нет установленного подключения", Toast.LENGTH_SHORT).show()
                              },
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ButtonCard(
                    text = "Данные в реальном времени",
                    icon = Icons.Default.Wifi,
                    onClick = {
                        if (viewModel.isConnected.value)
                            navController.navigate(Screen.ShowCurrentData.route)
                        else
                            Toast.makeText(context,"Нет установленного подключения", Toast.LENGTH_SHORT).show()
                    }
                )
                ButtonCard(
                    text = "Считать ошибки",
                    icon = Icons.Default.Warning,
                    onClick = {
                        if (viewModel.isConnected.value)
                            navController.navigate(Screen.ErrorCodes.route)
                        else
                            Toast.makeText(context,"Нет установленного подключения", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ButtonCard(
                    text = "Сбросить ошибки",
                    icon = Icons.Default.RestartAlt,
                    onClick = {
                        if (viewModel.isConnected.value)
                            navController.navigate(Screen.ErrorCodes.route)
                        else
                            Toast.makeText(context,"Нет установленного подключения", Toast.LENGTH_SHORT).show()
                    }
                )
                ButtonCard(
                    text = "Инфо по авто",
                    icon = Icons.Default.Info,
                    onClick = {
                        if (viewModel.isConnected.value)
                            navController.navigate(Screen.ErrorCodes.route)
                        else
                            Toast.makeText(context,"Нет установленного подключения", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    if (showCarDialog.value) {
        CarSelectionDialog(
            viewModel = viewModel,
            onDismiss = { showCarDialog.value = false }
        )
    }
}

@Composable
private fun CarSelectorButton(
    selectedCar: Car?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = selectedCar?.let { "${it.brand} ${it.model}" } ?: "Выберите автомобиль",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}



