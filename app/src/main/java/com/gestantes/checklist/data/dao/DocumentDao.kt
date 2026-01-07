package com.gestantes.checklist.data.dao

import androidx.room.*
import com.gestantes.checklist.data.entity.BabyDocument
import com.gestantes.checklist.data.entity.DocumentType
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {
    
    @Query("SELECT * FROM baby_documents ORDER BY createdAt DESC")
    fun getAllDocuments(): Flow<List<BabyDocument>>
    
    @Query("SELECT * FROM baby_documents WHERE id = :id")
    suspend fun getDocumentById(id: Long): BabyDocument?
    
    @Query("SELECT * FROM baby_documents WHERE documentType = :type ORDER BY documentDate DESC")
    fun getDocumentsByType(type: DocumentType): Flow<List<BabyDocument>>
    
    @Query("""
        SELECT * FROM baby_documents 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%' 
        OR tags LIKE '%' || :query || '%' 
        OR notes LIKE '%' || :query || '%'
        OR aiExtractedText LIKE '%' || :query || '%'
        OR aiKeywords LIKE '%' || :query || '%'
        ORDER BY createdAt DESC
    """)
    fun searchDocuments(query: String): Flow<List<BabyDocument>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(document: BabyDocument): Long
    
    @Update
    suspend fun update(document: BabyDocument)
    
    @Delete
    suspend fun delete(document: BabyDocument)
    
    @Query("DELETE FROM baby_documents WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    @Query("SELECT COUNT(*) FROM baby_documents")
    suspend fun getCount(): Int
    
    @Query("SELECT COUNT(*) FROM baby_documents WHERE documentType = :type")
    suspend fun getCountByType(type: DocumentType): Int
    
    // Para busca inteligente global
    @Query("""
        SELECT * FROM baby_documents 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%' 
        OR tags LIKE '%' || :query || '%' 
        OR notes LIKE '%' || :query || '%'
        OR aiExtractedText LIKE '%' || :query || '%'
        LIMIT :limit
    """)
    suspend fun searchForAI(query: String, limit: Int = 10): List<BabyDocument>
}



