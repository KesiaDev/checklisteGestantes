package com.gestantes.checklist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gestantes.checklist.ai.AIResponse
import com.gestantes.checklist.ai.AIService
import com.gestantes.checklist.data.database.ChecklistDatabase
import com.gestantes.checklist.data.entity.DiaryEntry
import com.gestantes.checklist.data.entity.Emotion
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DiaryViewModel(application: Application) : AndroidViewModel(application) {
    
    private val diaryDao = ChecklistDatabase.getDatabase(application).diaryDao()
    private val aiService = AIService.getInstance(application)
    
    val allEntries: StateFlow<List<DiaryEntry>> = diaryDao.getAllEntries()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    private val _selectedEntry = MutableStateFlow<DiaryEntry?>(null)
    val selectedEntry: StateFlow<DiaryEntry?> = _selectedEntry.asStateFlow()
    
    private val _aiResponse = MutableStateFlow<AIResponse?>(null)
    val aiResponse: StateFlow<AIResponse?> = _aiResponse.asStateFlow()
    
    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    val filteredEntries: StateFlow<List<DiaryEntry>> = combine(
        allEntries,
        searchQuery
    ) { entries, query ->
        if (query.isBlank()) entries
        else entries.filter { 
            it.content.contains(query, ignoreCase = true) ||
            it.title.contains(query, ignoreCase = true) ||
            it.emotion.displayName.contains(query, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun saveEntry(
        title: String,
        content: String,
        emotion: Emotion,
        existingEntry: DiaryEntry? = null
    ) {
        viewModelScope.launch {
            val entry = existingEntry?.copy(
                title = title,
                content = content,
                emotion = emotion,
                updatedAt = System.currentTimeMillis()
            ) ?: DiaryEntry(
                title = title,
                content = content,
                emotion = emotion
            )
            
            val id = diaryDao.insert(entry)
            
            // Analisar com IA automaticamente
            val savedEntry = diaryDao.getEntryById(id) ?: entry.copy(id = id)
            analyzeEntry(savedEntry)
        }
    }
    
    fun deleteEntry(entry: DiaryEntry) {
        viewModelScope.launch {
            diaryDao.delete(entry)
        }
    }
    
    fun selectEntry(entry: DiaryEntry?) {
        _selectedEntry.value = entry
        entry?.aiResponse?.let {
            _aiResponse.value = AIResponse(it, false, true)
        }
    }
    
    fun analyzeEntry(entry: DiaryEntry) {
        viewModelScope.launch {
            _isAnalyzing.value = true
            try {
                val response = aiService.analyzeDiaryEntry(entry)
                _aiResponse.value = response
                
                // Salvar resposta da IA no banco
                diaryDao.update(entry.copy(
                    aiResponse = response.message,
                    aiAnalyzedAt = System.currentTimeMillis()
                ))
            } finally {
                _isAnalyzing.value = false
            }
        }
    }
    
    fun clearAIResponse() {
        _aiResponse.value = null
    }
    
    fun getEntriesByEmotion(emotion: Emotion): Flow<List<DiaryEntry>> {
        return diaryDao.getEntriesByEmotion(emotion)
    }
}



