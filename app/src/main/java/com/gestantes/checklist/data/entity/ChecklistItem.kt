package com.gestantes.checklist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklist_items")
data class ChecklistItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: String,
    val title: String,
    val checked: Boolean = false
)

enum class ChecklistCategory(val displayName: String) {
    MATERNIDADE("Mala da Maternidade"),
    PRE_NATAL("Pré-natal"),
    POS_PARTO("Pós-parto")
}

