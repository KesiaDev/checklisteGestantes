package com.gestantes.checklist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidade para armazenar nomes de bebê salvos pelo usuário
 */
@Entity(tableName = "baby_names")
data class BabyName(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val meaning: String = "",
    val origin: String = "",
    val gender: String = "neutral", // male, female, neutral
    val isFavorite: Boolean = false,
    val notes: String = "",
    val rating: Int = 0, // 0-5 estrelas
    val addedAt: Long = System.currentTimeMillis()
)
