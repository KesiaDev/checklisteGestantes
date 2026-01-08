package com.gestantes.checklist.data.dao

import androidx.room.*
import com.gestantes.checklist.data.entity.WeeklyCheckItem
import kotlinx.coroutines.flow.Flow

/**
 * DAO para o checklist semanal da gestação
 * 
 * ADITIVO - Novo DAO para persistir progresso do checklist semanal
 */
@Dao
interface WeeklyCheckDao {
    
    /**
     * Obtém todos os itens marcados de uma semana específica
     */
    @Query("SELECT * FROM weekly_check_items WHERE week = :week")
    fun getItemsForWeek(week: Int): Flow<List<WeeklyCheckItem>>
    
    /**
     * Obtém todos os itens marcados (todas as semanas)
     */
    @Query("SELECT * FROM weekly_check_items WHERE isChecked = 1")
    fun getAllCheckedItems(): Flow<List<WeeklyCheckItem>>
    
    /**
     * Obtém um item específico pelo ID
     */
    @Query("SELECT * FROM weekly_check_items WHERE id = :id")
    suspend fun getItemById(id: String): WeeklyCheckItem?
    
    /**
     * Verifica se um item está marcado
     */
    @Query("SELECT isChecked FROM weekly_check_items WHERE id = :id")
    suspend fun isItemChecked(id: String): Boolean?
    
    /**
     * Insere ou atualiza um item
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertItem(item: WeeklyCheckItem)
    
    /**
     * Marca ou desmarca um item
     */
    @Query("UPDATE weekly_check_items SET isChecked = :isChecked, checkedAt = :checkedAt WHERE id = :id")
    suspend fun updateItemChecked(id: String, isChecked: Boolean, checkedAt: Long?)
    
    /**
     * Deleta um item
     */
    @Delete
    suspend fun deleteItem(item: WeeklyCheckItem)
    
    /**
     * Conta quantos itens estão marcados em uma semana
     */
    @Query("SELECT COUNT(*) FROM weekly_check_items WHERE week = :week AND isChecked = 1")
    suspend fun getCheckedCountForWeek(week: Int): Int
    
    /**
     * Conta total de itens marcados
     */
    @Query("SELECT COUNT(*) FROM weekly_check_items WHERE isChecked = 1")
    suspend fun getTotalCheckedCount(): Int
    
    /**
     * Obtém as semanas que têm itens marcados
     */
    @Query("SELECT DISTINCT week FROM weekly_check_items WHERE isChecked = 1 ORDER BY week")
    fun getWeeksWithProgress(): Flow<List<Int>>
}
