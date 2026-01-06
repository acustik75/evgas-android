package com.example.evgas.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// Entity для електро
@Entity(tableName = "electric_archive")
data class ElectricRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long, // timestamp
    val mileage: Int,
    val cost: Double
)

// Entity для газу
@Entity(tableName = "gas_archive")
data class GasRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long,
    val mileage: Int,
    val cost: Double
)

// DAO для електро
@Dao
interface ElectricDao {
    @Query("SELECT * FROM electric_archive ORDER BY date DESC")
    fun getAllRecords(): Flow<List<ElectricRecord>>
    
    @Insert
    suspend fun insert(record: ElectricRecord)
    
    @Update
    suspend fun update(record: ElectricRecord)
    
    @Delete
    suspend fun delete(record: ElectricRecord)
    
    @Query("SELECT COUNT(*) FROM electric_archive")
    fun getCount(): Flow<Int>
    
    @Query("SELECT SUM(mileage) FROM electric_archive")
    fun getTotalMileage(): Flow<Int?>
}

// DAO для газу
@Dao
interface GasDao {
    @Query("SELECT * FROM gas_archive ORDER BY date DESC")
    fun getAllRecords(): Flow<List<GasRecord>>
    
    @Insert
    suspend fun insert(record: GasRecord)
    
    @Update
    suspend fun update(record: GasRecord)
    
    @Delete
    suspend fun delete(record: GasRecord)
    
    @Query("SELECT COUNT(*) FROM gas_archive")
    fun getCount(): Flow<Int>
    
    @Query("SELECT SUM(mileage) FROM gas_archive")
    fun getTotalMileage(): Flow<Int?>
}

// Database
@Database(
    entities = [ElectricRecord::class, GasRecord::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun electricDao(): ElectricDao
    abstract fun gasDao(): GasDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "evgas_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}