package com.gestantes.checklist.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gestantes.checklist.data.entity.ChecklistItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistDao {
    
    @Query("SELECT * FROM checklist_items WHERE category = :category ORDER BY id")
    fun getItemsByCategory(category: String): Flow<List<ChecklistItem>>
    
    @Query("SELECT COUNT(*) FROM checklist_items WHERE category = :category")
    suspend fun getItemCount(category: String): Int
    
    @Update
    suspend fun updateItem(item: ChecklistItem)
    
    @Query("UPDATE checklist_items SET checked = 0 WHERE category = :category")
    suspend fun resetCategory(category: String)
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<ChecklistItem>)
    
    @Query("SELECT COUNT(*) FROM checklist_items")
    suspend fun getTotalCount(): Int
}

