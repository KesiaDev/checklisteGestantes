package com.gestantes.checklist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gestantes.checklist.data.database.ChecklistDatabase
import com.gestantes.checklist.data.entity.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    
    private val medicalDao = ChecklistDatabase.getDatabase(application).medicalDao()
    
    // Medical Records
    val allMedicalRecords: StateFlow<List<MedicalRecord>> = medicalDao.getAllMedicalRecords()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    // Development Records
    val allDevelopmentRecords: StateFlow<List<DevelopmentRecord>> = medicalDao.getAllDevelopmentRecords()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    private val _selectedAgeGroup = MutableStateFlow<AgeGroup?>(null)
    val selectedAgeGroup: StateFlow<AgeGroup?> = _selectedAgeGroup.asStateFlow()
    
    val filteredMedicalRecords: StateFlow<List<MedicalRecord>> = combine(
        allMedicalRecords,
        selectedAgeGroup
    ) { records, ageGroup ->
        if (ageGroup != null) records.filter { it.ageGroup == ageGroup }
        else records
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    val filteredDevelopmentRecords: StateFlow<List<DevelopmentRecord>> = combine(
        allDevelopmentRecords,
        selectedAgeGroup
    ) { records, ageGroup ->
        if (ageGroup != null) records.filter { it.ageGroup == ageGroup }
        else records
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    fun setAgeGroupFilter(ageGroup: AgeGroup?) {
        _selectedAgeGroup.value = ageGroup
    }
    
    // Medical Records CRUD
    fun saveMedicalRecord(
        recordType: MedicalRecordType,
        title: String,
        description: String,
        date: Long,
        doctorName: String,
        location: String,
        notes: String,
        ageGroup: AgeGroup,
        existingRecord: MedicalRecord? = null
    ) {
        viewModelScope.launch {
            val record = existingRecord?.copy(
                recordType = recordType,
                title = title,
                description = description,
                date = date,
                doctorName = doctorName,
                location = location,
                notes = notes,
                ageGroup = ageGroup,
                updatedAt = System.currentTimeMillis()
            ) ?: MedicalRecord(
                recordType = recordType,
                title = title,
                description = description,
                date = date,
                doctorName = doctorName,
                location = location,
                notes = notes,
                ageGroup = ageGroup
            )
            
            medicalDao.insertMedicalRecord(record)
        }
    }
    
    fun deleteMedicalRecord(record: MedicalRecord) {
        viewModelScope.launch {
            medicalDao.deleteMedicalRecord(record)
        }
    }
    
    // Development Records CRUD
    fun saveDevelopmentRecord(
        milestoneType: MilestoneType,
        title: String,
        description: String,
        date: Long,
        ageGroup: AgeGroup,
        notes: String,
        photoPath: String? = null,
        existingRecord: DevelopmentRecord? = null
    ) {
        viewModelScope.launch {
            val record = existingRecord?.copy(
                milestoneType = milestoneType,
                title = title,
                description = description,
                date = date,
                ageGroup = ageGroup,
                notes = notes,
                photoPath = photoPath,
                updatedAt = System.currentTimeMillis()
            ) ?: DevelopmentRecord(
                milestoneType = milestoneType,
                title = title,
                description = description,
                date = date,
                ageGroup = ageGroup,
                notes = notes,
                photoPath = photoPath
            )
            
            medicalDao.insertDevelopmentRecord(record)
        }
    }
    
    fun deleteDevelopmentRecord(record: DevelopmentRecord) {
        viewModelScope.launch {
            medicalDao.deleteDevelopmentRecord(record)
        }
    }
}



