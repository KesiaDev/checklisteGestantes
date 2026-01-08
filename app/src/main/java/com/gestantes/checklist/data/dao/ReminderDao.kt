package com.gestantes.checklist.data.dao

import androidx.room.*
import com.gestantes.checklist.data.entity.Reminder
import com.gestantes.checklist.data.entity.ReminderType
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders WHERE isCompleted = 0 ORDER BY dateTime ASC")
    fun getUpcomingReminders(): Flow<List<Reminder>>
    
    @Query("SELECT * FROM reminders ORDER BY dateTime DESC")
    fun getAllReminders(): Flow<List<Reminder>>
    
    @Query("SELECT * FROM reminders WHERE type = :type ORDER BY dateTime ASC")
    fun getRemindersByType(type: ReminderType): Flow<List<Reminder>>
    
    @Query("SELECT * FROM reminders WHERE dateTime BETWEEN :start AND :end ORDER BY dateTime ASC")
    fun getRemindersBetween(start: Long, end: Long): Flow<List<Reminder>>
    
    @Query("SELECT * FROM reminders WHERE id = :id")
    suspend fun getReminderById(id: Long): Reminder?
    
    @Query("SELECT * FROM reminders WHERE isCompleted = 0 AND dateTime <= :now AND isNotified = 0")
    suspend fun getDueReminders(now: Long): List<Reminder>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminder: Reminder): Long
    
    @Update
    suspend fun update(reminder: Reminder)
    
    @Delete
    suspend fun delete(reminder: Reminder)
    
    @Query("UPDATE reminders SET isCompleted = 1 WHERE id = :id")
    suspend fun markAsCompleted(id: Long)
    
    @Query("UPDATE reminders SET isNotified = 1 WHERE id = :id")
    suspend fun markAsNotified(id: Long)
}
