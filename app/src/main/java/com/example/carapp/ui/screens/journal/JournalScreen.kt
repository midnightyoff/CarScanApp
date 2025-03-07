package com.example.carapp.ui.screens.journal

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carapp.domain.model.JournalItem
import com.example.carapp.presentation.journal.viewmodel.JournalType
import com.example.carapp.presentation.journal.viewmodel.JournalViewModel

@Composable
fun JournalScreen(carId: Int?) {
    if (carId == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Сначала выберите автомобиль в настройках",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }

    val context = LocalContext.current
    val viewModel: JournalViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(JournalViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return JournalViewModel(
                        context.applicationContext as Application,
                        carId = carId
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    )
        val items by viewModel.journalItems.collectAsState(initial = emptyList())

        Scaffold(
            topBar = {
                FilterBar(
                    currentFilter = viewModel.currentFilter.value,
                    onFilterSelected = { viewModel.currentFilter.value = it }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.showDialog.value = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) { Icon(Icons.Default.Add, "Create New") }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = items.filteredByType(viewModel.currentFilter.value)) { item ->
                    JournalCard(
                        item = item,
                        onClick = { viewModel.selectedItem.value = it },
                        onDelete = { viewModel.deleteItem(item.id) }
                    )
                }
            }
        }

        if (viewModel.showDialog.value) {
            TypeSelectionDialog(
                onDismiss = { viewModel.showDialog.value = false },
                onTypeSelected = { type ->
                    viewModel.currentType.value = type
                    viewModel.showDialog.value = false
                }
            )
        }

        viewModel.currentType.value?.let { type ->
            when (type) {
                JournalType.MAINTENANCE -> MaintenanceDialog(
                    viewModel = viewModel,
                    carId = viewModel.carId ?: return@let
                )
                JournalType.REFUEL -> RefuelDialog(
                    viewModel = viewModel,
                    carId = viewModel.carId ?: return@let
                )
            }
        }

        viewModel.selectedItem.value?.let { item ->
            ItemDetailsDialog(
                item = item,
                onDismiss = { viewModel.selectedItem.value = null }
            )
        }
    }


private fun List<JournalItem>.filteredByType(type: JournalType?) = when(type) {
    JournalType.MAINTENANCE -> filterIsInstance<JournalItem.Maintenance>()
    JournalType.REFUEL -> filterIsInstance<JournalItem.Refuel>()
    null -> this
}.sortedByDescending { it.date }



