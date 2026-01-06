package com.example.evgas.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.evgas.viewmodel.GasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GasScreen(
    onArchiveClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: GasViewModel = viewModel()
) {
    val statistics by viewModel.statistics.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ГАЗ") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                }
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
            Button(
                onClick = onArchiveClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Архів")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            StatisticRow(
                label = "Вартість 1 км пробігу",
                value = "%.2f грн".format(statistics.costPer1Km)
            )
            
            StatisticRow(
                label = "Вартість 100 км пробігу",
                value = "%.2f грн".format(statistics.costPer100Km)
            )
            
            StatisticRow(
                label = "Загальний пробіг",
                value = "${statistics.totalMileage} км"
            )
            
            StatisticRow(
                label = "Кількість записів",
                value = "${statistics.recordCount}"
            )
        }
    }
}