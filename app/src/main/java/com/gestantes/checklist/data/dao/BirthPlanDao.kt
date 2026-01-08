package com.gestantes.checklist.data.dao

import androidx.room.*
import com.gestantes.checklist.data.entity.BirthPlanItem
import kotlinx.coroutines.flow.Flow

@Dao
interface BirthPlanDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: BirthPlanItem): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<BirthPlanItem>)
    
    @Update
    suspend fun update(item: BirthPlanItem)
    
    @Delete
    suspend fun delete(item: BirthPlanItem)
    
    @Query("SELECT * FROM birth_plan_items ORDER BY category, `order`")
    fun getAllItems(): Flow<List<BirthPlanItem>>
    
    @Query("SELECT * FROM birth_plan_items WHERE category = :category ORDER BY `order`")
    fun getItemsByCategory(category: String): Flow<List<BirthPlanItem>>
    
    @Query("SELECT * FROM birth_plan_items WHERE isSelected = 1 ORDER BY category, `order`")
    fun getSelectedItems(): Flow<List<BirthPlanItem>>
    
    @Query("UPDATE birth_plan_items SET preference = :preference, customNote = :note, isSelected = :isSelected WHERE id = :id")
    suspend fun updatePreference(id: Long, preference: String, note: String, isSelected: Boolean)
    
    @Query("SELECT COUNT(*) FROM birth_plan_items")
    suspend fun getCount(): Int
}
