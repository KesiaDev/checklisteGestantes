package com.gestantes.checklist.data.dao

import androidx.room.*
import com.gestantes.checklist.data.entity.BabyName
import kotlinx.coroutines.flow.Flow

@Dao
interface BabyNameDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(name: BabyName): Long
    
    @Update
    suspend fun update(name: BabyName)
    
    @Delete
    suspend fun delete(name: BabyName)
    
    @Query("SELECT * FROM baby_names ORDER BY isFavorite DESC, rating DESC, name ASC")
    fun getAllNames(): Flow<List<BabyName>>
    
    @Query("SELECT * FROM baby_names WHERE gender = :gender ORDER BY isFavorite DESC, rating DESC, name ASC")
    fun getNamesByGender(gender: String): Flow<List<BabyName>>
    
    @Query("SELECT * FROM baby_names WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteNames(): Flow<List<BabyName>>
    
    @Query("SELECT * FROM baby_names WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchNames(query: String): Flow<List<BabyName>>
    
    @Query("UPDATE baby_names SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)
    
    @Query("UPDATE baby_names SET rating = :rating WHERE id = :id")
    suspend fun updateRating(id: Long, rating: Int)
}
