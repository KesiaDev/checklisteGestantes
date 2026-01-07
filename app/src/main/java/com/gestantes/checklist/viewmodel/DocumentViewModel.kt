package com.gestantes.checklist.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gestantes.checklist.data.database.ChecklistDatabase
import com.gestantes.checklist.data.entity.BabyDocument
import com.gestantes.checklist.data.entity.DocumentType
import com.gestantes.checklist.data.entity.FileType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class DocumentViewModel(application: Application) : AndroidViewModel(application) {
    
    private val documentDao = ChecklistDatabase.getDatabase(application).documentDao()
    private val context = application.applicationContext
    
    val allDocuments: StateFlow<List<BabyDocument>> = documentDao.getAllDocuments()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _selectedFilter = MutableStateFlow<DocumentType?>(null)
    val selectedFilter: StateFlow<DocumentType?> = _selectedFilter.asStateFlow()
    
    val filteredDocuments: StateFlow<List<BabyDocument>> = combine(
        allDocuments,
        searchQuery,
        selectedFilter
    ) { documents, query, filter ->
        var result = documents
        
        if (filter != null) {
            result = result.filter { it.documentType == filter }
        }
        
        if (query.isNotBlank()) {
            result = result.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true) ||
                it.tags.contains(query, ignoreCase = true) ||
                it.notes.contains(query, ignoreCase = true) ||
                it.documentType.displayName.contains(query, ignoreCase = true)
            }
        }
        
        result
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun setFilter(type: DocumentType?) {
        _selectedFilter.value = type
    }
    
    fun saveDocument(
        uri: Uri,
        title: String,
        description: String,
        documentType: DocumentType,
        tags: String,
        notes: String,
        documentDate: Long?,
        existingDocument: BabyDocument? = null
    ) {
        viewModelScope.launch {
            try {
                // Copiar arquivo para armazenamento interno
                val filePath = if (existingDocument != null && uri.toString() == existingDocument.filePath) {
                    existingDocument.filePath
                } else {
                    copyFileToInternalStorage(uri)
                }
                
                val fileType = if (uri.toString().lowercase().endsWith(".pdf") || 
                    context.contentResolver.getType(uri)?.contains("pdf") == true) {
                    FileType.PDF
                } else {
                    FileType.IMAGE
                }
                
                val document = existingDocument?.copy(
                    title = title,
                    description = description,
                    documentType = documentType,
                    filePath = filePath,
                    fileType = fileType,
                    tags = tags,
                    notes = notes,
                    documentDate = documentDate,
                    updatedAt = System.currentTimeMillis()
                ) ?: BabyDocument(
                    title = title,
                    description = description,
                    documentType = documentType,
                    filePath = filePath,
                    fileType = fileType,
                    tags = tags,
                    notes = notes,
                    documentDate = documentDate
                )
                
                documentDao.insert(document)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun deleteDocument(document: BabyDocument) {
        viewModelScope.launch {
            // Deletar arquivo local
            try {
                File(document.filePath).delete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            documentDao.delete(document)
        }
    }
    
    private fun copyFileToInternalStorage(uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw Exception("Não foi possível abrir o arquivo")
        
        val fileName = "${UUID.randomUUID()}_${System.currentTimeMillis()}"
        val extension = context.contentResolver.getType(uri)?.let {
            when {
                it.contains("pdf") -> ".pdf"
                it.contains("png") -> ".png"
                it.contains("jpeg") || it.contains("jpg") -> ".jpg"
                else -> ""
            }
        } ?: ""
        
        val documentsDir = File(context.filesDir, "baby_documents").apply {
            if (!exists()) mkdirs()
        }
        
        val outputFile = File(documentsDir, "$fileName$extension")
        
        FileOutputStream(outputFile).use { output ->
            inputStream.copyTo(output)
        }
        
        inputStream.close()
        
        return outputFile.absolutePath
    }
    
    fun getDocumentsByType(type: DocumentType): Flow<List<BabyDocument>> {
        return documentDao.getDocumentsByType(type)
    }
}



