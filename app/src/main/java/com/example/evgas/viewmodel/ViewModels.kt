package com.example.evgas.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.evgas.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val electricRepository = ElectricRepository(database.electricDao())
    private val gasRepository = GasRepository(database.gasDao())
    
    val electricStats: StateFlow<Statistics> = electricRepository.statistics
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Statistics())
    
    val gasStats: StateFlow<Statistics> = gasRepository.statistics
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Statistics())
}

class ElectricViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = ElectricRepository(database.electricDao())
    
    val records: StateFlow<List<ElectricRecord>> = repository.allRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val statistics: StateFlow<Statistics> = repository.statistics
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Statistics())
    
    fun insertRecord(record: ElectricRecord) {
        viewModelScope.launch {
            repository.insert(record)
        }
    }
    
    fun updateRecord(record: ElectricRecord) {
        viewModelScope.launch {
            repository.update(record)
        }
    }
    
    fun deleteRecord(record: ElectricRecord) {
        viewModelScope.launch {
            repository.delete(record)
        }
    }
}

class GasViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = GasRepository(database.gasDao())
    
    val records: StateFlow<List<GasRecord>> = repository.allRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val statistics: StateFlow<Statistics> = repository.statistics
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Statistics())
    
    fun insertRecord(record: GasRecord) {
        viewModelScope.launch {
            repository.insert(record)
        }
    }
    
    fun updateRecord(record: GasRecord) {
        viewModelScope.launch {
            repository.update(record)
        }
    }
    
    fun deleteRecord(record: GasRecord) {
        viewModelScope.launch {
            repository.delete(record)
        }
    }
}