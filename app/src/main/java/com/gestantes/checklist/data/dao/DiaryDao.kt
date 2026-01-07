package com.gestantes.checklist.data.dao

import androidx.room.*
import com.gestantes.checklist.data.entity.DiaryEntry
import com.gestantes.checklist.data.entity.Emotion
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    
    @Query("SELECT * FROM diary_entries ORDER BY createdAt DESC")
    fun getAllEntries(): Flow<List<DiaryEntry>>
    
    @Query("SELECT * FROM diary_entries WHERE id = :id")
    suspend fun getEntryById(id: Long): DiaryEntry?
    
    @Query("SELECT * FROM diary_entries WHERE emotion = :emotion ORDER BY createdAt DESC")
    fun getEntriesByEmotion(emotion: Emotion): Flow<List<DiaryEntry>>
    
    @Query("SELECT * FROM diary_entries WHERE content LIKE '%' || :query || '%' OR title LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchEntries(query: String): Flow<List<DiaryEntry>>
    
    @Query("SELECT * FROM diary_entries WHERE createdAt BETWEEN :startDate AND :endDate ORDER BY createdAt DESC")
    fun getEntriesByDateRange(startDate: Long, endDate: Long): Flow<List<DiaryEntry>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: DiaryEntry): Long
    
    @Update
    suspend fun update(entry: DiaryEntry)
    
    @Delete
    suspend fun delete(entry: DiaryEntry)
    
    @Query("DELETE FROM diary_entries WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("SELECT COUNT(*) FROM diary_entries")
    suspend fun getCount(): Int
    
    // Para busca inteligente global
    @Query("SELECT * FROM diary_entries WHERE content LIKE '%' || :query || '%' OR title LIKE '%' || :query || '%' OR aiResponse LIKE '%' || :query || '%' LIMIT :limit")
    suspend fun searchForAI(query: String, limit: Int = 10): List<DiaryEntry>
}



