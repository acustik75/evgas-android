package com.example.evgas.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.evgas.data.ElectricRecord
import com.example.evgas.data.GasRecord
import com.example.evgas.viewmodel.ElectricViewModel
import com.example.evgas.viewmodel.GasViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveScreen(
    type: String,
    onBackClick: () -> Unit
) {
    val electricViewModel: ElectricViewModel = viewModel()
    val gasViewModel: GasViewModel = viewModel()
    
    val records = if (type == "electric") {
        electricViewModel.records.collectAsState().value
    } else {
        gasViewModel.records.collectAsState().value
    }
    
    var showDialog by remember { mutableStateOf(false) }
    var editingRecord by remember { mutableStateOf<Any?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var recordToDelete by remember { mutableStateOf<Any?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (type == "electric") "Архів Електро" else "Архів Газ") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { 
                editingRecord = null
                showDialog = true 
            }) {
                Icon(Icons.Default.Add, "Новий запис")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Заголовки таблиці
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Дата", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                Text("Пробіг, км", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                Text("Вартість, грн", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
            }
            
            HorizontalDivider()
            
            // Список записів
            LazyColumn {
                items(records) { record ->
                    if (type == "electric" && record is ElectricRecord) {
                        RecordItem(
                            date = record.date,
                            mileage = record.mileage,
                            cost = record.cost,
                            onClick = {
                                editingRecord = record
                                showDialog = true
                            },
                            onDeleteClick = {
                                recordToDelete = record
                                showDeleteDialog = true
                            }
                        )
                    } else if (type == "gas" && record is GasRecord) {
                        RecordItem(
                            date = record.date,
                            mileage = record.mileage,
                            cost = record.cost,
                            onClick = {
                                editingRecord = record
                                showDialog = true
                            },
                            onDeleteClick = {
                                recordToDelete = record
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }
    
    // Діалог додавання/редагування
    if (showDialog) {
        RecordDialog(
            type = type,
            record = editingRecord,
            onDismiss = { showDialog = false },
            onSave = { date, mileage, cost ->
                if (type == "electric") {
                    if (editingRecord != null) {
                        val old = editingRecord as ElectricRecord
                        electricViewModel.updateRecord(old.copy(date = date, mileage = mileage, cost = cost))
                    } else {
                        electricViewModel.insertRecord(ElectricRecord(date = date, mileage = mileage, cost = cost))
                    }
                } else {
                    if (editingRecord != null) {
                        val old = editingRecord as GasRecord
                        gasViewModel.updateRecord(old.copy(date = date, mileage = mileage, cost = cost))
                    } else {
                        gasViewModel.insertRecord(GasRecord(date = date, mileage = mileage, cost = cost))
                    }
                }
                showDialog = false
            }
        )
    }
    
    // Діалог підтвердження видалення
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Підтвердження") },
            text = { Text("Ви впевнені, що хочете видалити цей запис?") },
            confirmButton = {
                TextButton(onClick = {
                    if (type == "electric") {
                        electricViewModel.deleteRecord(recordToDelete as ElectricRecord)
                    } else {
                        gasViewModel.deleteRecord(recordToDelete as GasRecord)
                    }
                    showDeleteDialog = false
                    recordToDelete = null
                }) {
                    Text("Видалити")
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showDeleteDialog = false
                    recordToDelete = null
                }) {
                    Text("Скасувати")
                }
            }
        )
    }
}

@Composable
fun RecordItem(
    date: Long,
    mileage: Int,
    cost: Double,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { showMenu = true }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(dateFormat.format(Date(date)), modifier = Modifier.weight(1f))
            Text(mileage.toString(), modifier = Modifier.weight(1f))
            Text(cost.toInt().toString(), modifier = Modifier.weight(1f))
        }
        
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("Редагувати") },
                onClick = {
                    showMenu = false
                    onClick()
                }
            )
            DropdownMenuItem(
                text = { Text("Видалити") },
                onClick = {
                    showMenu = false
                    onDeleteClick()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordDialog(
    type: String,
    record: Any?,
    onDismiss: () -> Unit,
    onSave: (Long, Int, Double) -> Unit
) {
    val existingDate = when (record) {
        is ElectricRecord -> record.date
        is GasRecord -> record.date
        else -> System.currentTimeMillis()
    }
    val existingMileage = when (record) {
        is ElectricRecord -> record.mileage.toString()
        is GasRecord -> record.mileage.toString()
        else -> ""
    }
    val existingCost = when (record) {
        is ElectricRecord -> record.cost.toString()
        is GasRecord -> record.cost.toString()
        else -> ""
    }
    
    var selectedDate by remember { mutableStateOf(existingDate) }
    var mileage by remember { mutableStateOf(existingMileage) }
    var cost by remember { mutableStateOf(existingCost) }
    var showDatePicker by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (record != null) "Редагувати запис" else "Новий запис") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Дата
                OutlinedButton(onClick = { showDatePicker = true }) {
                    Text("Дата: ${dateFormat.format(Date(selectedDate))}")
                }
                
                // Пробіг
                OutlinedTextField(
                    value = mileage,
                    onValueChange = { mileage = it.filter { char -> char.isDigit() } },
                    label = { Text("Пробіг, км") },
                    singleLine = true
                )
                
                // Вартість
                OutlinedTextField(
                    value = cost,
                    onValueChange = { 
                        cost = it.filter { char -> char.isDigit() || char == '.' || char == ',' }
                    },
                    label = { Text("Вартість, грн") },
                    singleLine = true
                )
                
                if (errorMessage.isNotEmpty()) {
                    Text(errorMessage, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val mileageInt = mileage.toIntOrNull()
                val costDouble = cost.replace(',', '.').toDoubleOrNull()
                
                when {
                    mileageInt == null || mileageInt <= 0 -> {
                        errorMessage = "Введіть пробіг більше 0"
                    }
                    costDouble == null || costDouble <= 0 -> {
                        errorMessage = "Введіть коректну вартість"
                    }
                    else -> {
                        onSave(selectedDate, mileageInt, costDouble)
                    }
                }
            }) {
                Text("Зберегти")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Скасувати")
            }
        }
    )
    
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= System.currentTimeMillis()
                }
            }
        )
        
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { selectedDate = it }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Скасувати")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}