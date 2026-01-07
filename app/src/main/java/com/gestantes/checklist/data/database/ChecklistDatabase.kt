package com.gestantes.checklist.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gestantes.checklist.data.dao.*
import com.gestantes.checklist.data.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ChecklistItem::class,
        DiaryEntry::class,
        BabyDocument::class,
        MedicalRecord::class,
        DevelopmentRecord::class,
        GrowthRecord::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ChecklistDatabase : RoomDatabase() {
    
    abstract fun checklistDao(): ChecklistDao
    abstract fun diaryDao(): DiaryDao
    abstract fun documentDao(): DocumentDao
    abstract fun medicalDao(): MedicalDao
    abstract fun growthDao(): GrowthDao
    
    companion object {
        @Volatile
        private var INSTANCE: ChecklistDatabase? = null
        
        fun getDatabase(context: Context): ChecklistDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChecklistDatabase::class.java,
                    "checklist_database"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
        
        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.checklistDao())
                    }
                }
            }
        }
        
        private suspend fun populateDatabase(dao: ChecklistDao) {
            // Só popula se estiver vazio
            if (dao.getTotalCount() > 0) return
            
            val items = mutableListOf<ChecklistItem>()
            
            // Mala da Maternidade
            val maternidadeItems = listOf(
                "Documentos pessoais",
                "Cartão do pré-natal",
                "Camisolas",
                "Produtos de higiene",
                "Chinelo",
                "Roupinhas do bebê",
                "Fraldas RN",
                "Manta",
                "Meias",
                "Saída de maternidade"
            )
            maternidadeItems.forEach { title ->
                items.add(ChecklistItem(category = ChecklistCategory.MATERNIDADE.name, title = title))
            }
            
            // Pré-natal
            val prenatalItems = listOf(
                "Consultas realizadas",
                "Exames solicitados",
                "Vacinas em dia",
                "Ultrassons realizados",
                "Escolha da maternidade",
                "Plano de parto organizado"
            )
            prenatalItems.forEach { title ->
                items.add(ChecklistItem(category = ChecklistCategory.PRE_NATAL.name, title = title))
            }
            
            // Pós-parto
            val pospartoItems = listOf(
                "Registro do bebê",
                "Primeira consulta pediátrica",
                "Rede de apoio organizada",
                "Cuidados com a mãe",
                "Acompanhamento emocional",
                "Retorno médico agendado"
            )
            pospartoItems.forEach { title ->
                items.add(ChecklistItem(category = ChecklistCategory.POS_PARTO.name, title = title))
            }
            
            dao.insertAll(items)
        }
    }
}
