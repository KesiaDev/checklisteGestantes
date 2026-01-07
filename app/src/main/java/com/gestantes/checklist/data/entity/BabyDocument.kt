package com.gestantes.checklist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Documento do Bebê
 * Armazena arquivos importantes como certidão, vacinas, exames, etc.
 */
@Entity(tableName = "baby_documents")
data class BabyDocument(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val documentType: DocumentType,
    val filePath: String, // Caminho local do arquivo (foto ou PDF)
    val fileType: FileType,
    val tags: String = "", // Tags separadas por vírgula para busca
    val notes: String = "", // Observações da mãe
    val documentDate: Long? = null, // Data do documento (ex: data da vacina)
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    // Metadados extraídos por IA para busca inteligente
    val aiExtractedText: String? = null,
    val aiKeywords: String? = null
)

/**
 * Tipos de documentos do bebê
 */
enum class DocumentType(val displayName: String, val icon: String) {
    VACCINATION_CARD("Caderneta de Vacinação", "💉"),
    BIRTH_CERTIFICATE("Certidão de Nascimento", "📜"),
    HEALTH_CARD("Cartão SUS/Convênio", "🏥"),
    MEDICAL_RECORD("Prontuário Médico", "📋"),
    PRESCRIPTION("Receita Médica", "💊"),
    EXAM("Exame", "🔬"),
    PHOTO("Foto/Memória", "📷"),
    OTHER("Outro", "📄")
}

/**
 * Tipo de arquivo
 */
enum class FileType {
    IMAGE,
    PDF
}



