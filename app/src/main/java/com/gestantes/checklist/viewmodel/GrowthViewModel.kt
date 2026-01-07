package com.gestantes.checklist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gestantes.checklist.data.database.ChecklistDatabase
import com.gestantes.checklist.data.entity.GrowthRecord
import com.gestantes.checklist.data.entity.GrowthReference
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GrowthViewModel(application: Application) : AndroidViewModel(application) {
    
    private val growthDao = ChecklistDatabase.getDatabase(application).growthDao()
    
    val allRecords: StateFlow<List<GrowthRecord>> = growthDao.getAllRecordsByAge()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    val latestRecord: StateFlow<GrowthRecord?> = allRecords
        .map { it.maxByOrNull { record -> record.date } }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
    
    val weightHistory: StateFlow<List<GrowthRecord>> = growthDao.getWeightHistory()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    val heightHistory: StateFlow<List<GrowthRecord>> = growthDao.getHeightHistory()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    fun saveRecord(
        ageInMonths: Int,
        weightKg: Float?,
        heightCm: Float?,
        headCircumferenceCm: Float?,
        notes: String,
        measuredBy: String,
        existingRecord: GrowthRecord? = null
    ) {
        viewModelScope.launch {
            val record = existingRecord?.copy(
                ageInMonths = ageInMonths,
                weightKg = weightKg,
                heightCm = heightCm,
                headCircumferenceCm = headCircumferenceCm,
                notes = notes,
                measuredBy = measuredBy,
                updatedAt = System.currentTimeMillis()
            ) ?: GrowthRecord(
                date = System.currentTimeMillis(),
                ageInMonths = ageInMonths,
                weightKg = weightKg,
                heightCm = heightCm,
                headCircumferenceCm = headCircumferenceCm,
                notes = notes,
                measuredBy = measuredBy
            )
            
            growthDao.insert(record)
        }
    }
    
    fun deleteRecord(record: GrowthRecord) {
        viewModelScope.launch {
            growthDao.delete(record)
        }
    }
    
    /**
     * Analisa o crescimento e retorna uma mensagem simples
     */
    fun analyzeGrowth(record: GrowthRecord): String {
        val messages = mutableListOf<String>()
        
        record.weightKg?.let { weight ->
            val referenceWeight = GrowthReference.weightByMonth[record.ageInMonths]
                ?: GrowthReference.weightByMonth.entries.minByOrNull { 
                    kotlin.math.abs(it.key - record.ageInMonths) 
                }?.value
            
            referenceWeight?.let { ref ->
                val percentDiff = ((weight - ref) / ref) * 100
                when {
                    percentDiff > 15 -> messages.add("O peso está acima da média para a idade.")
                    percentDiff < -15 -> messages.add("O peso está abaixo da média. Converse com o pediatra.")
                    else -> messages.add("O peso está dentro do esperado para a idade! 👍")
                }
            }
        }
        
        record.heightCm?.let { height ->
            val referenceHeight = GrowthReference.heightByMonth[record.ageInMonths]
                ?: GrowthReference.heightByMonth.entries.minByOrNull { 
                    kotlin.math.abs(it.key - record.ageInMonths) 
                }?.value
            
            referenceHeight?.let { ref ->
                val percentDiff = ((height - ref) / ref) * 100
                when {
                    percentDiff > 10 -> messages.add("A altura está acima da média para a idade.")
                    percentDiff < -10 -> messages.add("A altura está um pouco abaixo. Continue acompanhando.")
                    else -> messages.add("A altura está dentro do esperado! 📏")
                }
            }
        }
        
        return if (messages.isNotEmpty()) {
            messages.joinToString("\n\n") + "\n\n⚠️ Lembre-se: estas são apenas referências gerais. O pediatra é quem melhor avalia o desenvolvimento do seu bebê."
        } else {
            "Continue registrando as medições para acompanhar o crescimento do seu bebê!"
        }
    }
}



