package com.example.evgas.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class Statistics(
    val costPer1Km: Double = 0.0,
    val costPer100Km: Double = 0.0,
    val totalMileage: Int = 0,
    val recordCount: Int = 0
)

class ElectricRepository(private val dao: ElectricDao) {
    val allRecords: Flow<List<ElectricRecord>> = dao.getAllRecords()
    
    val statistics: Flow<Statistics> = allRecords.map { records ->
        if (records.isEmpty()) {
            Statistics()
        } else {
            val avgCostPer1Km = records.map { it.cost / it.mileage }.average()
            val avgCostPer100Km = avgCostPer1Km * 100
            val totalMileage = records.sumOf { it.mileage }
            
            Statistics(
                costPer1Km = avgCostPer1Km,
                costPer100Km = avgCostPer100Km,
                totalMileage = totalMileage,
                recordCount = records.size
            )
        }
    }
    
    suspend fun insert(record: ElectricRecord) = dao.insert(record)
    suspend fun update(record: ElectricRecord) = dao.update(record)
    suspend fun delete(record: ElectricRecord) = dao.delete(record)
}

class GasRepository(private val dao: GasDao) {
    val allRecords: Flow<List<GasRecord>> = dao.getAllRecords()
    
    val statistics: Flow<Statistics> = allRecords.map { records ->
        if (records.isEmpty()) {
            Statistics()
        } else {
            val avgCostPer1Km = records.map { it.cost / it.mileage }.average()
            val avgCostPer100Km = avgCostPer1Km * 100
            val totalMileage = records.sumOf { it.mileage }
            
            Statistics(
                costPer1Km = avgCostPer1Km,
                costPer100Km = avgCostPer100Km,
                totalMileage = totalMileage,
                recordCount = records.size
            )
        }
    }
    
    suspend fun insert(record: GasRecord) = dao.insert(record)
    suspend fun update(record: GasRecord) = dao.update(record)
    suspend fun delete(record: GasRecord) = dao.delete(record)
}