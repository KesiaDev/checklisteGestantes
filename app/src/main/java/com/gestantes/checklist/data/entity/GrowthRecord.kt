package com.gestantes.checklist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Registro de Crescimento do Bebê
 * Peso, altura, perímetro cefálico
 */
@Entity(tableName = "growth_records")
data class GrowthRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Long, // Data da medição
    val ageInMonths: Int, // Idade em meses na medição
    val weightKg: Float? = null, // Peso em kg
    val heightCm: Float? = null, // Altura em cm
    val headCircumferenceCm: Float? = null, // Perímetro cefálico em cm
    val notes: String = "", // Observações da mãe
    val measuredBy: String = "", // Quem mediu (pediatra, em casa, etc)
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Dados de referência para curvas de crescimento (OMS)
 * Simplificado para exibição básica
 */
object GrowthReference {
    // Peso médio por idade (meses) - meninos/meninas combinado
    val weightByMonth = mapOf(
        0 to 3.3f,
        1 to 4.5f,
        2 to 5.6f,
        3 to 6.4f,
        4 to 7.0f,
        5 to 7.5f,
        6 to 7.9f,
        9 to 8.9f,
        12 to 9.6f,
        18 to 10.9f,
        24 to 12.2f,
        36 to 14.3f,
        48 to 16.3f
    )
    
    // Altura média por idade (meses)
    val heightByMonth = mapOf(
        0 to 50f,
        1 to 54f,
        2 to 58f,
        3 to 61f,
        4 to 63f,
        5 to 65f,
        6 to 67f,
        9 to 72f,
        12 to 76f,
        18 to 82f,
        24 to 87f,
        36 to 96f,
        48 to 103f
    )
}



