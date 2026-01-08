package com.gestantes.checklist.data.dao

import androidx.room.*
import com.gestantes.checklist.data.entity.Contraction
import kotlinx.coroutines.flow.Flow

@Dao
interface ContractionDao {
    @Query("SELECT * FROM contractions WHERE sessionId = :sessionId ORDER BY startTime ASC")
    fun getContractionsBySession(sessionId: String): Flow<List<Contraction>>
    
    @Query("SELECT DISTINCT sessionId FROM contractions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<String>>
    
    @Query("SELECT * FROM contractions ORDER BY startTime DESC LIMIT 50")
    fun getRecentContractions(): Flow<List<Contraction>>
    
    @Query("SELECT * FROM contractions WHERE sessionId = :sessionId ORDER BY startTime DESC LIMIT 1")
    suspend fun getLastContraction(sessionId: String): Contraction?
    
    @Query("SELECT COUNT(*) FROM contractions WHERE sessionId = :sessionId")
    suspend fun getContractionCount(sessionId: String): Int
    
    @Query("SELECT AVG(durationSeconds) FROM contractions WHERE sessionId = :sessionId AND durationSeconds IS NOT NULL")
    suspend fun getAverageDuration(sessionId: String): Float?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contraction: Contraction): Long
    
    @Update
    suspend fun update(contraction: Contraction)
    
    @Delete
    suspend fun delete(contraction: Contraction)
    
    @Query("DELETE FROM contractions WHERE sessionId = :sessionId")
    suspend fun deleteSession(sessionId: String)
}
