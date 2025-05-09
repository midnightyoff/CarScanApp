package com.example.carapp.ui.screens.showcurrentdata

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.carapp.obd2.ObdConnection
import com.example.carapp.presentation.ObdConnectionViewModel
import com.example.carapp.presentation.mainviewmodel.MainViewModel
import com.example.carapp.presentation.showcurrentdata.ShowCurrentDataViewModel

@Composable
fun ShowCurrentDataScreen(navController: NavController, obdViewModel: ObdConnectionViewModel) {
    val connection: ObdConnection = obdViewModel.getConnection() ?: throw Exception()
    val viewModel = ShowCurrentDataViewModel(connection)
    val sensors by remember { viewModel.sensors }.collectAsStateWithLifecycle()
    viewModel.startMonitoring()
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = {
                viewModel.stopMonitoring()
                navController.popBackStack()
                      },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Назад",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sensors) { sensor ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column {
                        Text(sensor.pid)
                        Text(sensor.value)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}