package com.gestantes.checklist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidade para fotos da barriga por semana
 */
@Entity(tableName = "belly_photos")
data class BellyPhoto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val week: Int,
    val photoUri: String,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
