package com.example.carapp.ui.screens.showcurrentdata

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.carapp.obd2.ObdConnection
import com.example.carapp.presentation.ObdConnectionViewModel
import com.example.carapp.presentation.mainviewmodel.MainViewModel
import com.example.carapp.presentation.showcurrentdata.ShowCurrentDataViewModel
import com.example.carapp.ui.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun ShowCurrentDataScreen(navController: NavController, obdViewModel: ObdConnectionViewModel) {

    val connection = obdViewModel.getConnection() ?: run {
        navController.popBackStack()
        return
    }

    val viewModel: ShowCurrentDataViewModel = remember {
        ShowCurrentDataViewModel(connection)
    }

    DisposableEffect(Unit) {
        viewModel.startMonitoring()
        onDispose {
            viewModel.stopMonitoring()
        }
    }



    val sensorsList = listOf(
        Triple("Скорость",  viewModel.speed.value.toFloatOrNull() ?: 0f, "km/h" to (0f..220f)),
        Triple("Обороты",  viewModel.rpm.value.toFloatOrNull() ?: 0f, "RPM" to (0f..8000f)),
        Triple("Температура",  viewModel.coolantTemp.value.toFloatOrNull() ?: 0f, "°C" to (-40f..120f))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "Данные в реальном времени",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp))
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(sensorsList) { (title, value, unitWithRange) ->
                DashboardItem(
                    title = title,
                    value = value,
                    unit = unitWithRange.first,
                    range = unitWithRange.second
                )
            }
        }
    }
}