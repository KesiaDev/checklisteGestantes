package com.gestantes.checklist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidade para armazenar itens do plano de parto
 */
@Entity(tableName = "birth_plan_items")
data class BirthPlanItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String, // ambiente, trabalho_parto, parto, bebe, pos_parto
    val title: String,
    val description: String = "",
    val preference: String = "", // sim, não, talvez, personalizado
    val customNote: String = "",
    val isSelected: Boolean = false,
    val order: Int = 0
)

/**
 * Categorias do plano de parto
 */
enum class BirthPlanCategory(val displayName: String, val emoji: String) {
    AMBIENTE("Ambiente", "🏥"),
    TRABALHO_PARTO("Trabalho de Parto", "⏰"),
    PARTO("Parto", "👶"),
    BEBE("Cuidados com o Bebê", "🍼"),
    POS_PARTO("Pós-Parto", "💜")
}
