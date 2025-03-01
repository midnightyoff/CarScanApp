package com.example.carapp.ui.screens.mainscreen

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.carapp.data.model.Car
import com.example.carapp.presentation.mainviewmodel.MainViewModel
import com.example.carapp.ui.navigation.Screen

@Composable
fun MainScreen(navController: NavController) {
    val viewModel: MainViewModel = viewModel()
    val showCarDialog = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        CarSelectorButton(
            selectedCar = viewModel.selectedCar.value,
            onClick = { showCarDialog.value = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ButtonCard(
                text = "Терминал",
                icon = Icons.Default.Terminal,
                onClick = { navController.navigate(Screen.Terminal.route) },
                modifier = Modifier.fillMaxWidth(0.6f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ButtonCard(
                    text = "Реальные данные",
                    icon = Icons.Default.Wifi,
                    onClick = { /* Данные в реальном времени */ }
                )
                ButtonCard(
                    text = "Считать ошибки",
                    icon = Icons.Default.Warning,
                    onClick = { /* Считать ошибки */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ButtonCard(
                    text = "Сбросить ошибки",
                    icon = Icons.Default.RestartAlt,
                    onClick = { /* Сбросить ошибки */ }
                )
                ButtonCard(
                    text = "Инфо по авто",
                    icon = Icons.Default.Info,
                    onClick = { /* Информация по авто */ }
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
fun ButtonCard(text: String, icon: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier.size(180.dp, 80.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = text, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(16.dp))
            Text(text, style = MaterialTheme.typography.titleMedium)
        }
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
                modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                text = selectedCar?.let { "${it.brand} ${it.model}" } ?: "Выберите автомобиль",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

