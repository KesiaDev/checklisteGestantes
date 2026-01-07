package com.gestantes.checklist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gestantes.checklist.ai.AISearchResponse
import com.gestantes.checklist.ai.AIService
import com.gestantes.checklist.ai.SearchResult
import com.gestantes.checklist.data.database.ChecklistDatabase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = ChecklistDatabase.getDatabase(application)
    private val aiService = AIService.getInstance(application)
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()
    
    private val _searchResponse = MutableStateFlow<AISearchResponse?>(null)
    val searchResponse: StateFlow<AISearchResponse?> = _searchResponse.asStateFlow()
    
    private val _recentQueries = MutableStateFlow<List<String>>(emptyList())
    val recentQueries: StateFlow<List<String>> = _recentQueries.asStateFlow()
    
    val suggestedQueries = listOf(
        "Onde está a certidão de nascimento?",
        "Quando foi a última vacina?",
        "O que escrevi quando ele nasceu?",
        "Qual foi o último peso do bebê?",
        "Onde guardei a receita médica?",
        "Quando foi a primeira consulta?"
    )
    
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun search(query: String) {
        if (query.isBlank()) return
        
        viewModelScope.launch {
            _isSearching.value = true
            _searchQuery.value = query
            
            try {
                // Buscar em todas as fontes de dados
                val diaryEntries = database.diaryDao().searchForAI(query)
                val documents = database.documentDao().searchForAI(query)
                val medicalRecords = database.medicalDao().searchMedicalForAI(query)
                val developmentRecords = database.medicalDao().searchDevelopmentForAI(query)
                val growthRecords = database.growthDao().searchForAI(query)
                
                // Processar com IA
                val response = aiService.answerQuestion(
                    question = query,
                    diaryEntries = diaryEntries,
                    documents = documents,
                    medicalRecords = medicalRecords,
                    developmentRecords = developmentRecords,
                    growthRecords = growthRecords
                )
                
                _searchResponse.value = response
                
                // Adicionar às queries recentes
                val recent = _recentQueries.value.toMutableList()
                if (!recent.contains(query)) {
                    recent.add(0, query)
                    if (recent.size > 5) recent.removeAt(recent.lastIndex)
                    _recentQueries.value = recent
                }
                
            } catch (e: Exception) {
                _searchResponse.value = AISearchResponse(
                    message = "Desculpe, não consegui processar sua busca. Tente novamente.",
                    results = emptyList()
                )
            } finally {
                _isSearching.value = false
            }
        }
    }
    
    fun clearSearch() {
        _searchQuery.value = ""
        _searchResponse.value = null
    }
}

