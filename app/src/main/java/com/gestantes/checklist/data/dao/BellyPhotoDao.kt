package com.gestantes.checklist.data.dao

import androidx.room.*
import com.gestantes.checklist.data.entity.BellyPhoto
import kotlinx.coroutines.flow.Flow

@Dao
interface BellyPhotoDao {
    @Query("SELECT * FROM belly_photos ORDER BY week ASC")
    fun getAllPhotos(): Flow<List<BellyPhoto>>
    
    @Query("SELECT * FROM belly_photos WHERE week = :week LIMIT 1")
    suspend fun getPhotoByWeek(week: Int): BellyPhoto?
    
    @Query("SELECT * FROM belly_photos ORDER BY week DESC LIMIT 1")
    suspend fun getLatestPhoto(): BellyPhoto?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: BellyPhoto): Long
    
    @Update
    suspend fun update(photo: BellyPhoto)
    
    @Delete
    suspend fun delete(photo: BellyPhoto)
    
    @Query("DELETE FROM belly_photos WHERE week = :week")
    suspend fun deleteByWeek(week: Int)
}
