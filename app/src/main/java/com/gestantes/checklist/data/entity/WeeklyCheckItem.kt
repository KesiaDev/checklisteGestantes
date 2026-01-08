package com.gestantes.checklist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidade para persistir os itens do checklist semanal da gestação
 * 
 * ADITIVO - Nova entidade para salvar o progresso do checklist semanal
 */
@Entity(tableName = "weekly_check_items")
data class WeeklyCheckItem(
    @PrimaryKey
    val id: String, // Formato: "week-itemText" ex: "8-Agendar primeira consulta"
    val week: Int,
    val itemText: String,
    val isChecked: Boolean = false,
    val checkedAt: Long? = null // Quando foi marcado
) {
    companion object {
        /**
         * Cria o ID único para um item
         */
        fun createId(week: Int, itemText: String): String {
            return "$week-$itemText"
        }
    }
}
