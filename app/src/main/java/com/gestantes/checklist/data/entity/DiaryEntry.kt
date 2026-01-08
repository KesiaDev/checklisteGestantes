package com.gestantes.checklist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entrada do Diário da Mamãe
 * Armazena memórias, sentimentos e reflexões da mãe
 */
@Entity(tableName = "diary_entries")
data class DiaryEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val content: String,
    val emotion: Emotion,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    // Resposta da IA (armazenada para offline)
    val aiResponse: String? = null,
    val aiAnalyzedAt: Long? = null,
    // EXPANSÃO: Momento compartilhado com acompanhante (ADITIVO)
    // Campo opcional - não altera lógica existente
    val involvesCompanion: Boolean = false
)

/**
 * Emoções que a mãe pode selecionar
 */
enum class Emotion(val displayName: String, val emoji: String, val color: Long) {
    HAPPY("Feliz", "😊", 0xFF4CAF50),
    GRATEFUL("Grata", "🙏", 0xFF9C27B0),
    CALM("Calma", "😌", 0xFF03A9F4),
    TIRED("Cansada", "😴", 0xFF607D8B),
    ANXIOUS("Ansiosa", "😰", 0xFFFF9800),
    INSECURE("Insegura", "😟", 0xFFFFEB3B),
    SAD("Triste", "😢", 0xFF2196F3),
    OVERWHELMED("Sobrecarregada", "😩", 0xFFF44336),
    HOPEFUL("Esperançosa", "🌟", 0xFFE91E63),
    LOVING("Amorosa", "💕", 0xFFE91E63)
}



