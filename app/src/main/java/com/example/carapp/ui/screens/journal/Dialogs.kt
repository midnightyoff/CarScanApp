package com.example.carapp.ui.screens.journal


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.carapp.domain.model.JournalItem
import com.example.carapp.presentation.journal.viewmodel.JournalType
import com.example.carapp.presentation.journal.viewmodel.JournalViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TypeSelectionDialog(
    onDismiss: () -> Unit,
    onTypeSelected: (JournalType) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите тип записи") },
        confirmButton = {
            TextButton(onClick = { onTypeSelected(JournalType.REFUEL) }) {
                Text("Заправка")
            }
        },
        dismissButton = {
            TextButton(onClick = { onTypeSelected(JournalType.MAINTENANCE) }) {
                Text("Обслуживание")
            }
        }
    )
}

@Composable
fun MaintenanceDialog(viewModel: JournalViewModel, carId: Int) {
    var title by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf(Date()) }
    var mileage by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { viewModel.currentType.value = null },
        title = { Text("Новое обслуживание") },
        text = {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Название") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                DatePickerField(date, onDateChange = { date = it })
                NumberInputField(
                    value = mileage,
                    onValueChange = { mileage = it },
                    label = "Пробег (км)",
                    icon = Icons.Default.Speed
                )
                NumberInputField(
                    value = price,
                    onValueChange = { price = it },
                    label = "Цена",
                    icon = Icons.Default.AttachMoney,
                    keyboardType = KeyboardType.Number
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.addItem(
                        JournalItem.Maintenance(
                            id = UUID.randomUUID().toString(),
                            carId = carId,
                            title = title,
                            date = date,
                            mileage = mileage.toIntOrNull() ?: 0,
                            price = price.toDoubleOrNull() ?: 0.0,
                            description = description
                        )
                    )
                    viewModel.currentType.value = null
                },
                enabled = title.isNotBlank() && mileage.isNotBlank() && price.isNotBlank()
            ) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.currentType.value = null }) { Text("Назад") }
        }
    )
}

@Composable
fun RefuelDialog(viewModel: JournalViewModel, carId: Int) {
    var title by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf(Date()) }
    var mileage by rememberSaveable { mutableStateOf("") }
    var fuelType by rememberSaveable { mutableStateOf("") }
    var fuelVolume by rememberSaveable { mutableStateOf("") }
    var pricePerLiter by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { viewModel.currentType.value = null },
        title = { Text("Новая заправка") },
        text = {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Название") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                DatePickerField(date, onDateChange = { date = it })
                NumberInputField(
                    value = mileage,
                    onValueChange = { mileage = it },
                    label = "Пробег (км)",
                    icon = Icons.Default.Speed
                )
                OutlinedTextField(
                    value = fuelType,
                    onValueChange = { fuelType = it },
                    label = { Text("Тип топлива") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                NumberInputField(
                    value = fuelVolume,
                    onValueChange = { fuelVolume = it },
                    label = "Объем (л)",
                    icon = Icons.Default.LocalGasStation,
                    keyboardType = KeyboardType.Number
                )
                NumberInputField(
                    value = pricePerLiter,
                    onValueChange = { pricePerLiter = it },
                    label = "Цена за литр",
                    icon = Icons.Default.AttachMoney,
                    keyboardType = KeyboardType.Number
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.addItem(
                        JournalItem.Refuel(
                            id = UUID.randomUUID().toString(),
                            carId = carId,
                            title = title,
                            date = date,
                            mileage = mileage.toIntOrNull() ?: 0,
                            fuelType = fuelType,
                            fuelVolume = fuelVolume.toDoubleOrNull() ?: 0.0,
                            pricePerLiter = pricePerLiter.toDoubleOrNull() ?: 0.0,
                            description = description
                        )
                    )
                    viewModel.currentType.value = null
                },
                enabled = title.isNotBlank() && mileage.isNotBlank() &&
                        fuelType.isNotBlank() && fuelVolume.isNotBlank() &&
                        pricePerLiter.isNotBlank()
            ) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.currentType.value = null }) { Text("Назад") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerField(
    date: Date,
    onDateChange: (Date) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = SimpleDateFormat("dd.MM.yyyy").format(date),
        onValueChange = {},
        readOnly = true,
        label = { Text("Дата") },
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Default.DateRange, "Выбрать дату")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date.time)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateChange(Date(it))
                    }
                    showDatePicker = false
                }) { Text("OK") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun NumberInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Number
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.isEmpty() || newValue.toDoubleOrNull() != null) {
                onValueChange(newValue)
            }
        },
        label = { Text(label) },
        leadingIcon = { Icon(icon, null) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ItemDetailsDialog(
    item: JournalItem,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Детали записи") },
        text = {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Тип: ${when (item) {
                    is JournalItem.Maintenance -> "Обслуживание"
                    is JournalItem.Refuel -> "Заправка"
                }}")
                Text("Дата: ${SimpleDateFormat("dd.MM.yyyy").format(item.date)}")
                Text("Пробег: ${item.mileage} км")
                Text("Цена: ${"%.2f".format(item.price)} ₽")

                when (item) {
                    is JournalItem.Maintenance -> {
                        // Нет дополнительных полей
                    }
                    is JournalItem.Refuel -> {
                        Text("Тип топлива: ${item.fuelType}")
                        Text("Объем: ${"%.2f".format(item.fuelVolume)} л")
                        Text("Цена за литр: ${"%.2f".format(item.pricePerLiter)} ₽")
                    }
                }

                Text("Описание: ${item.description}")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Закрыть") }
        }
    )
}