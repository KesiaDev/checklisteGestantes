package com.gestantes.checklist.data.dao

import androidx.room.*
import com.gestantes.checklist.data.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicalDao {
    
    // ============ MEDICAL RECORDS ============
    
    @Query("SELECT * FROM medical_records ORDER BY date DESC")
    fun getAllMedicalRecords(): Flow<List<MedicalRecord>>
    
    @Query("SELECT * FROM medical_records WHERE id = :id")
    suspend fun getMedicalRecordById(id: Long): MedicalRecord?
    
    @Query("SELECT * FROM medical_records WHERE recordType = :type ORDER BY date DESC")
    fun getMedicalRecordsByType(type: MedicalRecordType): Flow<List<MedicalRecord>>
    
    @Query("SELECT * FROM medical_records WHERE ageGroup = :ageGroup ORDER BY date DESC")
    fun getMedicalRecordsByAgeGroup(ageGroup: AgeGroup): Flow<List<MedicalRecord>>
    
    @Query("""
        SELECT * FROM medical_records 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%' 
        OR notes LIKE '%' || :query || '%'
        OR doctorName LIKE '%' || :query || '%'
        ORDER BY date DESC
    """)
    fun searchMedicalRecords(query: String): Flow<List<MedicalRecord>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicalRecord(record: MedicalRecord): Long
    
    @Update
    suspend fun updateMedicalRecord(record: MedicalRecord)
    
    @Delete
    suspend fun deleteMedicalRecord(record: MedicalRecord)
    
    @Query("SELECT COUNT(*) FROM medical_records WHERE recordType = :type")
    suspend fun getMedicalRecordCountByType(type: MedicalRecordType): Int
    
    // Para busca inteligente global
    @Query("""
        SELECT * FROM medical_records 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%' 
        OR notes LIKE '%' || :query || '%'
        LIMIT :limit
    """)
    suspend fun searchMedicalForAI(query: String, limit: Int = 10): List<MedicalRecord>
    
    // ============ DEVELOPMENT RECORDS ============
    
    @Query("SELECT * FROM development_records ORDER BY date DESC")
    fun getAllDevelopmentRecords(): Flow<List<DevelopmentRecord>>
    
    @Query("SELECT * FROM development_records WHERE id = :id")
    suspend fun getDevelopmentRecordById(id: Long): DevelopmentRecord?
    
    @Query("SELECT * FROM development_records WHERE milestoneType = :type ORDER BY date DESC")
    fun getDevelopmentRecordsByType(type: MilestoneType): Flow<List<DevelopmentRecord>>
    
    @Query("SELECT * FROM development_records WHERE ageGroup = :ageGroup ORDER BY date DESC")
    fun getDevelopmentRecordsByAgeGroup(ageGroup: AgeGroup): Flow<List<DevelopmentRecord>>
    
    @Query("""
        SELECT * FROM development_records 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%' 
        OR notes LIKE '%' || :query || '%'
        ORDER BY date DESC
    """)
    fun searchDevelopmentRecords(query: String): Flow<List<DevelopmentRecord>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevelopmentRecord(record: DevelopmentRecord): Long
    
    @Update
    suspend fun updateDevelopmentRecord(record: DevelopmentRecord)
    
    @Delete
    suspend fun deleteDevelopmentRecord(record: DevelopmentRecord)
    
    // Para busca inteligente global
    @Query("""
        SELECT * FROM development_records 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%' 
        OR notes LIKE '%' || :query || '%'
        LIMIT :limit
    """)
    suspend fun searchDevelopmentForAI(query: String, limit: Int = 10): List<DevelopmentRecord>
}



