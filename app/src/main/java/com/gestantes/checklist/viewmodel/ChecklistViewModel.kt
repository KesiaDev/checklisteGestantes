package com.gestantes.checklist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gestantes.checklist.data.database.ChecklistDatabase
import com.gestantes.checklist.data.entity.ChecklistCategory
import com.gestantes.checklist.data.entity.ChecklistItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChecklistViewModel(application: Application) : AndroidViewModel(application) {
    
    private val dao = ChecklistDatabase.getDatabase(application).checklistDao()
    
    private val _items = MutableStateFlow<List<ChecklistItem>>(emptyList())
    val items: StateFlow<List<ChecklistItem>> = _items.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun loadItems(category: ChecklistCategory) {
        viewModelScope.launch {
            _isLoading.value = true
            dao.getItemsByCategory(category.name).collect { itemList ->
                _items.value = itemList
                _isLoading.value = false
            }
        }
    }
    
    fun toggleItem(item: ChecklistItem) {
        viewModelScope.launch {
            dao.updateItem(item.copy(checked = !item.checked))
        }
    }
    
    fun resetCategory(category: ChecklistCategory) {
        viewModelScope.launch {
            dao.resetCategory(category.name)
        }
    }
    
    fun getProgress(): Pair<Int, Int> {
        val total = _items.value.size
        val completed = _items.value.count { it.checked }
        return Pair(completed, total)
    }
}

