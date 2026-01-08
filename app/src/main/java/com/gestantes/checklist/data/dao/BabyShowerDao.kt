package com.gestantes.checklist.data.dao

import androidx.room.*
import com.gestantes.checklist.data.entity.BabyShowerItem
import com.gestantes.checklist.data.entity.ShowerCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface BabyShowerDao {
    @Query("SELECT * FROM baby_shower_items ORDER BY category, priority DESC, name")
    fun getAllItems(): Flow<List<BabyShowerItem>>
    
    @Query("SELECT * FROM baby_shower_items WHERE category = :category ORDER BY priority DESC, name")
    fun getItemsByCategory(category: ShowerCategory): Flow<List<BabyShowerItem>>
    
    @Query("SELECT * FROM baby_shower_items WHERE isReceived = 0 ORDER BY priority DESC")
    fun getPendingItems(): Flow<List<BabyShowerItem>>
    
    @Query("SELECT * FROM baby_shower_items WHERE isReceived = 1 ORDER BY name")
    fun getReceivedItems(): Flow<List<BabyShowerItem>>
    
    @Query("SELECT COUNT(*) FROM baby_shower_items")
    suspend fun getTotalCount(): Int
    
    @Query("SELECT COUNT(*) FROM baby_shower_items WHERE isReceived = 1")
    suspend fun getReceivedCount(): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: BabyShowerItem): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<BabyShowerItem>)
    
    @Update
    suspend fun update(item: BabyShowerItem)
    
    @Delete
    suspend fun delete(item: BabyShowerItem)
    
    @Query("DELETE FROM baby_shower_items")
    suspend fun deleteAll()
}
