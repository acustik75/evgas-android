package com.example.evgas.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.evgas.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComparisonScreen(
    onElectricClick: () -> Unit,
    onGasClick: () -> Unit,
    viewModel: MainViewModel = viewModel()
) {
    val electricStats by viewModel.electricStats.collectAsState()
    val gasStats by viewModel.gasStats.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Порівняння") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Кнопки навігації
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onElectricClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Електро")
                }
                Button(
                    onClick = onGasClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("ГАЗ")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Порівняння показників
            ComparisonRow(
                label = "Вартість 1 км пробігу, грн",
                electricValue = electricStats.costPer1Km,
                gasValue = gasStats.costPer1Km,
                formatValue = { "%.2f".format(it) }
            )
            
            ComparisonRow(
                label = "Вартість 100 км пробігу, грн",
                electricValue = electricStats.costPer100Km,
                gasValue = gasStats.costPer100Km,
                formatValue = { "%.2f".format(it) }
            )
            
            ComparisonRow(
                label = "Пробіг, км",
                electricValue = electricStats.totalMileage.toDouble(),
                gasValue = gasStats.totalMileage.toDouble(),
                formatValue = { it.toInt().toString() }
            )
            
            ComparisonRow(
                label = "Записів всього, разів",
                electricValue = electricStats.recordCount.toDouble(),
                gasValue = gasStats.recordCount.toDouble(),
                formatValue = { it.toInt().toString() }
            )
        }
    }
}

@Composable
fun ComparisonRow(
    label: String,
    electricValue: Double,
    gasValue: Double,
    formatValue: (Double) -> String
) {
    val electricColor = when {
        electricValue == 0.0 && gasValue == 0.0 -> Color.Gray
        electricValue < gasValue -> Color.Green
        electricValue > gasValue -> Color.Red
        else -> Color.Gray
    }
    
    val gasColor = when {
        electricValue == 0.0 && gasValue == 0.0 -> Color.Gray
        gasValue < electricValue -> Color.Green
        gasValue > electricValue -> Color.Red
        else -> Color.Gray
    }
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                border = BorderStroke(2.dp, electricColor)
            ) {
                Text(
                    text = formatValue(electricValue),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
            
            Card(
                modifier = Modifier.weight(1f),
                border = BorderStroke(2.dp, gasColor)
            ) {
                Text(
                    text = formatValue(gasValue),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}