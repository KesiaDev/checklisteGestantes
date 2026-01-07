package com.gestantes.checklist.data.database

import androidx.room.TypeConverter
import com.gestantes.checklist.data.entity.*

/**
 * Conversores de tipo para o Room Database
 * Converte enums para strings e vice-versa
 */
class Converters {
    
    // ============ EMOTION ============
    @TypeConverter
    fun fromEmotion(emotion: Emotion): String = emotion.name
    
    @TypeConverter
    fun toEmotion(value: String): Emotion = Emotion.valueOf(value)
    
    // ============ DOCUMENT TYPE ============
    @TypeConverter
    fun fromDocumentType(type: DocumentType): String = type.name
    
    @TypeConverter
    fun toDocumentType(value: String): DocumentType = DocumentType.valueOf(value)
    
    // ============ FILE TYPE ============
    @TypeConverter
    fun fromFileType(type: FileType): String = type.name
    
    @TypeConverter
    fun toFileType(value: String): FileType = FileType.valueOf(value)
    
    // ============ MEDICAL RECORD TYPE ============
    @TypeConverter
    fun fromMedicalRecordType(type: MedicalRecordType): String = type.name
    
    @TypeConverter
    fun toMedicalRecordType(value: String): MedicalRecordType = MedicalRecordType.valueOf(value)
    
    // ============ MILESTONE TYPE ============
    @TypeConverter
    fun fromMilestoneType(type: MilestoneType): String = type.name
    
    @TypeConverter
    fun toMilestoneType(value: String): MilestoneType = MilestoneType.valueOf(value)
    
    // ============ AGE GROUP ============
    @TypeConverter
    fun fromAgeGroup(ageGroup: AgeGroup): String = ageGroup.name
    
    @TypeConverter
    fun toAgeGroup(value: String): AgeGroup = AgeGroup.valueOf(value)
}



