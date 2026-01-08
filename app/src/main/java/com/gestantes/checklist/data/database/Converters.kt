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
    
    // ============ LETTER MOOD ============
    @TypeConverter
    fun fromLetterMood(mood: LetterMood): String = mood.name
    
    @TypeConverter
    fun toLetterMood(value: String): LetterMood = LetterMood.valueOf(value)
    
    // ============ SHOWER CATEGORY ============
    @TypeConverter
    fun fromShowerCategory(category: ShowerCategory): String = category.name
    
    @TypeConverter
    fun toShowerCategory(value: String): ShowerCategory = ShowerCategory.valueOf(value)
    
    // ============ ITEM PRIORITY ============
    @TypeConverter
    fun fromItemPriority(priority: ItemPriority): String = priority.name
    
    @TypeConverter
    fun toItemPriority(value: String): ItemPriority = ItemPriority.valueOf(value)
    
    // ============ CONTRACTION INTENSITY ============
    @TypeConverter
    fun fromContractionIntensity(intensity: ContractionIntensity): String = intensity.name
    
    @TypeConverter
    fun toContractionIntensity(value: String): ContractionIntensity = ContractionIntensity.valueOf(value)
    
    // ============ REMINDER TYPE ============
    @TypeConverter
    fun fromReminderType(type: ReminderType): String = type.name
    
    @TypeConverter
    fun toReminderType(value: String): ReminderType = ReminderType.valueOf(value)
    
    // ============ REPEAT TYPE ============
    @TypeConverter
    fun fromRepeatType(type: RepeatType): String = type.name
    
    @TypeConverter
    fun toRepeatType(value: String): RepeatType = RepeatType.valueOf(value)
}



