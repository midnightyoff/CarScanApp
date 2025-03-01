package com.example.carapp.ui.screens.journal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carapp.presentation.journal.viewmodel.JournalType

@Composable
fun FilterBar(
    currentFilter: JournalType?,
    onFilterSelected: (JournalType?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = currentFilter == null,
            onClick = { onFilterSelected(null) },
            label = { Text("Все") }
        )
        FilterChip(
            selected = currentFilter == JournalType.MAINTENANCE,
            onClick = { onFilterSelected(JournalType.MAINTENANCE) },
            label = { Text("Обслуживание") }
        )
        FilterChip(
            selected = currentFilter == JournalType.REFUEL,
            onClick = { onFilterSelected(JournalType.REFUEL) },
            label = { Text("Заправка") }
        )
    }
}