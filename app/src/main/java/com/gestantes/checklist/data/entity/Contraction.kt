package com.gestantes.checklist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidade para registro de contrações
 */
@Entity(tableName = "contractions")
data class Contraction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startTime: Long,
    val endTime: Long? = null, // null se ainda em andamento
    val durationSeconds: Int? = null,
    val intensity: ContractionIntensity = ContractionIntensity.MEDIUM,
    val notes: String = "",
    val sessionId: String // Para agrupar contrações de uma mesma sessão
)

enum class ContractionIntensity(val displayName: String, val emoji: String, val color: Long) {
    LIGHT("Leve", "😊", 0xFF4CAF50),
    MEDIUM("Moderada", "😐", 0xFFFF9800),
    STRONG("Forte", "😣", 0xFFE53935),
    VERY_STRONG("Muito Forte", "😰", 0xFF9C27B0)
}

/**
 * Entidade para lembretes
 */
@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val type: ReminderType,
    val dateTime: Long,
    val isCompleted: Boolean = false,
    val isNotified: Boolean = false,
    val repeatType: RepeatType = RepeatType.NONE,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ReminderType(val emoji: String, val displayName: String, val color: Long) {
    CONSULTATION("👩‍⚕️", "Consulta", 0xFF2196F3),
    EXAM("🔬", "Exame", 0xFF9C27B0),
    VACCINE("💉", "Vacina", 0xFF4CAF50),
    MEDICATION("💊", "Medicamento", 0xFFFF5722),
    APPOINTMENT("📅", "Compromisso", 0xFF607D8B),
    OTHER("📌", "Outro", 0xFF795548)
}

enum class RepeatType(val displayName: String) {
    NONE("Não repetir"),
    DAILY("Diariamente"),
    WEEKLY("Semanalmente"),
    MONTHLY("Mensalmente")
}
