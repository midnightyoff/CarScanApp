package com.example.carapp.ui.screens.settings

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun FaqDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Необходим адаптер ELM327") },
        text = {
            Text("Для работы с диагностикой автомобиля требуется:\n\n" +
                    "• Адаптер ELM327 с поддержкой Bluetooth\n" +
                    "• Совместимый автомобиль (2001 г.в. и новее)\n" +
                    "• Включенный OBD-II разъем в автомобиле")
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Закрыть") }
        }
    )
}