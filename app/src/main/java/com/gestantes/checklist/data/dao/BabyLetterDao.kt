package com.gestantes.checklist.data.dao

import androidx.room.*
import com.gestantes.checklist.data.entity.BabyLetter
import kotlinx.coroutines.flow.Flow

@Dao
interface BabyLetterDao {
    @Query("SELECT * FROM baby_letters ORDER BY createdAt DESC")
    fun getAllLetters(): Flow<List<BabyLetter>>
    
    @Query("SELECT * FROM baby_letters WHERE id = :id")
    suspend fun getLetterById(id: Long): BabyLetter?
    
    @Query("SELECT COUNT(*) FROM baby_letters")
    suspend fun getLetterCount(): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(letter: BabyLetter): Long
    
    @Update
    suspend fun update(letter: BabyLetter)
    
    @Delete
    suspend fun delete(letter: BabyLetter)
}
