package com.gestantes.checklist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidade para itens da lista de chá de bebê
 */
@Entity(tableName = "baby_shower_items")
data class BabyShowerItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: ShowerCategory,
    val quantity: Int = 1,
    val quantityReceived: Int = 0,
    val priority: ItemPriority = ItemPriority.MEDIUM,
    val link: String = "", // Link para compra (opcional)
    val notes: String = "",
    val giftedBy: String = "", // Quem presenteou
    val isReceived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ShowerCategory(val emoji: String, val displayName: String) {
    CLOTHING("👕", "Roupinhas"),
    HYGIENE("🧴", "Higiene"),
    FEEDING("🍼", "Alimentação"),
    BEDROOM("🛏️", "Quarto"),
    STROLLER("🚼", "Passeio"),
    TOYS("🧸", "Brinquedos"),
    HEALTH("💊", "Saúde"),
    BATH("🛁", "Banho"),
    OTHER("📦", "Outros")
}

enum class ItemPriority(val displayName: String, val color: Long) {
    HIGH("Essencial", 0xFFE53935),
    MEDIUM("Importante", 0xFFFF9800),
    LOW("Opcional", 0xFF4CAF50)
}
