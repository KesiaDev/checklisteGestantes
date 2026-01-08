package com.gestantes.checklist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidade para cartas escritas para o bebê
 */
@Entity(tableName = "baby_letters")
data class BabyLetter(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val week: Int? = null, // Semana da gestação (opcional)
    val month: Int? = null, // Mês do bebê (opcional)
    val mood: LetterMood = LetterMood.LOVE,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class LetterMood(val emoji: String, val displayName: String) {
    LOVE("💕", "Amor"),
    JOY("🎉", "Alegria"),
    HOPE("🌟", "Esperança"),
    GRATITUDE("🙏", "Gratidão"),
    DREAM("✨", "Sonhos"),
    MEMORY("📸", "Memória"),
    ADVICE("💝", "Conselho"),
    PROMISE("🤝", "Promessa")
}
