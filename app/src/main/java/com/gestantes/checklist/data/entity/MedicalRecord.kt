package com.gestantes.checklist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Registro Médico/Clínico do Bebê
 * Consultas, vacinas, doenças, medicamentos
 */
@Entity(tableName = "medical_records")
data class MedicalRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val recordType: MedicalRecordType,
    val title: String,
    val description: String = "",
    val date: Long, // Data do evento
    val doctorName: String = "",
    val location: String = "", // Hospital/Clínica
    val notes: String = "", // Observações da mãe
    val ageGroup: AgeGroup, // Faixa etária do bebê
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Tipos de registro médico
 */
enum class MedicalRecordType(val displayName: String, val icon: String) {
    CONSULTATION("Consulta Médica", "👨‍⚕️"),
    VACCINE("Vacina", "💉"),
    ILLNESS("Doença/Intercorrência", "🤒"),
    MEDICATION("Medicamento", "💊"),
    HOSPITALIZATION("Internação", "🏥"),
    EMERGENCY("Emergência", "🚑"),
    OTHER("Outro", "📝")
}

/**
 * Registro de Desenvolvimento/Pedagógico do Bebê
 * Marcos, primeiras palavras, comportamentos
 */
@Entity(tableName = "development_records")
data class DevelopmentRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val milestoneType: MilestoneType,
    val title: String,
    val description: String = "",
    val date: Long, // Data do marco
    val ageGroup: AgeGroup,
    val notes: String = "",
    val photoPath: String? = null, // Foto opcional do momento
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Tipos de marcos do desenvolvimento
 */
enum class MilestoneType(val displayName: String, val icon: String) {
    MOTOR("Marco Motor", "🏃"),
    LANGUAGE("Linguagem", "💬"),
    SOCIAL("Social/Emocional", "👋"),
    COGNITIVE("Cognitivo", "🧠"),
    FIRST_WORD("Primeira Palavra", "🗣️"),
    FIRST_STEP("Primeiro Passo", "👣"),
    FIRST_TOOTH("Primeiro Dente", "🦷"),
    FIRST_FOOD("Primeira Comida", "🍼"),
    SLEEP("Sono", "😴"),
    OTHER("Outro", "⭐")
}

/**
 * Faixas etárias do bebê (0-4 anos)
 */
enum class AgeGroup(val displayName: String, val months: IntRange) {
    NEWBORN("0-6 meses", 0..6),
    INFANT("6-12 meses", 7..12),
    TODDLER_1("1-2 anos", 13..24),
    TODDLER_2("2-3 anos", 25..36),
    PRESCHOOL("3-4 anos", 37..48);
    
    companion object {
        fun fromMonths(months: Int): AgeGroup {
            return entries.find { months in it.months } ?: PRESCHOOL
        }
    }
}



