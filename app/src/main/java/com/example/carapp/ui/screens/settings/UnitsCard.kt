package com.example.carapp.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carapp.presentation.settings.viewmodel.Units

@Composable
fun UnitsCard(
    currentUnits: Units,
    onUnitsChanged: (Units) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Единицы измерения", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Row {
                FilterChip(
                    selected = currentUnits == Units.KILOMETERS,
                    onClick = { onUnitsChanged(Units.KILOMETERS) },
                    label = { Text("Километры") }
                )
                Spacer(Modifier.width(8.dp))
                FilterChip(
                    selected = currentUnits == Units.MILES,
                    onClick = { onUnitsChanged(Units.MILES) },
                    label = { Text("Мили") }
                )
            }
        }
    }
}