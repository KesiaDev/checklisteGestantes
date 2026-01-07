package com.gestantes.checklist.data.dao

import androidx.room.*
import com.gestantes.checklist.data.entity.GrowthRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface GrowthDao {
    
    @Query("SELECT * FROM growth_records ORDER BY date DESC")
    fun getAllRecords(): Flow<List<GrowthRecord>>
    
    @Query("SELECT * FROM growth_records ORDER BY ageInMonths ASC")
    fun getAllRecordsByAge(): Flow<List<GrowthRecord>>
    
    @Query("SELECT * FROM growth_records WHERE id = :id")
    suspend fun getRecordById(id: Long): GrowthRecord?
    
    @Query("SELECT * FROM growth_records ORDER BY date DESC LIMIT 1")
    suspend fun getLatestRecord(): GrowthRecord?
    
    @Query("SELECT * FROM growth_records WHERE ageInMonths = :ageInMonths")
    suspend fun getRecordByAge(ageInMonths: Int): GrowthRecord?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: GrowthRecord): Long
    
    @Update
    suspend fun update(record: GrowthRecord)
    
    @Delete
    suspend fun delete(record: GrowthRecord)
    
    @Query("DELETE FROM growth_records WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("SELECT COUNT(*) FROM growth_records")
    suspend fun getCount(): Int
    
    // Dados para gráficos
    @Query("SELECT * FROM growth_records WHERE weightKg IS NOT NULL ORDER BY ageInMonths ASC")
    fun getWeightHistory(): Flow<List<GrowthRecord>>
    
    @Query("SELECT * FROM growth_records WHERE heightCm IS NOT NULL ORDER BY ageInMonths ASC")
    fun getHeightHistory(): Flow<List<GrowthRecord>>
    
    // Para busca inteligente global
    @Query("""
        SELECT * FROM growth_records 
        WHERE notes LIKE '%' || :query || '%' 
        OR measuredBy LIKE '%' || :query || '%'
        LIMIT :limit
    """)
    suspend fun searchForAI(query: String, limit: Int = 10): List<GrowthRecord>
}



