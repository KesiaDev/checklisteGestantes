package com.gestantes.checklist.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension para criar o DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

/**
 * Gênero do bebê para personalização de cores
 */
enum class BabyGender(val displayName: String, val emoji: String) {
    GIRL("Menina", "👧"),
    BOY("Menino", "👦"),
    UNKNOWN("Ainda não sei", "👶")
}

/**
 * Tema de cores do app - MODERNO E INCLUSIVO
 * As cores NÃO estão associadas a gênero - você escolhe o que mais gosta!
 */
enum class AppTheme(val displayName: String, val emoji: String) {
    // Cores clássicas renomeadas de forma neutra
    GIRL("Rosa Suave", "🌸"),           // Rosa e verde (mantido para compatibilidade)
    BOY("Azul Sereno", "💙"),           // Azul e verde (mantido para compatibilidade)
    NEUTRAL("Verde Natureza", "🌿"),    // Verde e amarelo (mantido para compatibilidade)
    
    // NOVAS paletas modernas
    LAVENDER("Lavanda", "💜"),          // Roxo/Lavanda suave
    CORAL("Coral Sunset", "🧡"),        // Coral/Pêssego
    MINT("Menta Fresh", "🍃"),          // Menta/Turquesa
    PEACH("Pêssego Bloom", "🍑"),       // Pêssego/Rosa quente
    OCEAN("Oceano", "🌊"),              // Azul oceano profundo
    SUNSET("Pôr do Sol", "🌅"),         // Laranja/Rosa gradiente
    FOREST("Floresta", "🌲"),           // Verde escuro/Musgo
    
    CUSTOM("Personalizado", "🎨")       // Deixa o usuário escolher
}

// ============ EXPANSÃO DA PESSOA ACOMPANHANTE (ADITIVO) ============

/**
 * Tipos de apoio que a pessoa acompanhante oferece
 * ADITIVO - Apenas para personalização de textos
 */
enum class CompanionSupportType(val displayName: String, val emoji: String, val description: String) {
    EMOTIONAL("Apoio emocional", "💗", "Oferece carinho, escuta e suporte emocional"),
    PLANNING("Organização e planejamento", "📋", "Ajuda a organizar e planejar a chegada do bebê"),
    APPOINTMENTS("Presença em consultas", "🏥", "Acompanha nas consultas e exames"),
    POSTPARTUM("Apoio no pós-parto", "🤱", "Estará presente no pós-parto")
}

/**
 * Dados expandidos da pessoa acompanhante (ADITIVO)
 * Tudo é OPCIONAL e serve apenas para personalização
 */
data class CompanionData(
    val name: String = "",
    val supportTypes: Set<CompanionSupportType> = emptySet()
) {
    val hasCompanion: Boolean get() = name.isNotBlank()
    
    /**
     * Retorna o nome do acompanhante ou um texto genérico
     */
    fun getDisplayName(fallback: String = "sua rede de apoio"): String {
        return if (name.isNotBlank()) name else fallback
    }
}

/**
 * Modelo de dados para um bebê
 */
data class Baby(
    val id: String = System.currentTimeMillis().toString(),
    val name: String,
    val gender: BabyGender = BabyGender.UNKNOWN,
    val birthDate: String? = null, // Formato: "dd/MM/yyyy"
    val isExpecting: Boolean = false // Se ainda está esperando (grávida)
)

/**
 * Modelo de dados completo do usuário
 */
data class UserData(
    val momName: String = "",
    val babies: List<Baby> = emptyList(),
    val onboardingCompleted: Boolean = false,
    val appTheme: AppTheme = AppTheme.GIRL, // Tema padrão
    // Campos para inclusão familiar (ADITIVOS)
    val companionName: String = "", // Nome da pessoa acompanhante (opcional)
    val expectedDueDate: String? = null, // Data prevista do parto (formato: "dd/MM/yyyy")
    val currentWeek: Int = 0, // Semana atual da gestação (calculada ou informada)
    // EXPANSÃO DA PESSOA ACOMPANHANTE (ADITIVO)
    val companionSupportTypes: Set<CompanionSupportType> = emptySet() // Tipos de apoio (opcional)
) {
    /**
     * Retorna os dados completos do acompanhante
     * ADITIVO - Conveniência para acessar dados do acompanhante
     */
    val companion: CompanionData get() = CompanionData(
        name = companionName,
        supportTypes = companionSupportTypes
    )
}

/**
 * Gerenciador de preferências do usuário usando DataStore
 */
class UserPreferencesManager(private val context: Context) {
    
    private val gson = Gson()
    
    companion object {
        private val MOM_NAME_KEY = stringPreferencesKey("mom_name")
        private val BABIES_JSON_KEY = stringPreferencesKey("babies_json")
        private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")
        private val APP_THEME_KEY = stringPreferencesKey("app_theme")
        // Chaves para inclusão familiar (ADITIVAS)
        private val COMPANION_NAME_KEY = stringPreferencesKey("companion_name")
        private val EXPECTED_DUE_DATE_KEY = stringPreferencesKey("expected_due_date")
        private val CURRENT_WEEK_KEY = stringPreferencesKey("current_week")
        // EXPANSÃO DA PESSOA ACOMPANHANTE (ADITIVA)
        private val COMPANION_SUPPORT_TYPES_KEY = stringPreferencesKey("companion_support_types")
    }
    
    /**
     * Flow que emite os dados do usuário sempre que mudarem
     */
    val userData: Flow<UserData> = context.dataStore.data.map { preferences ->
        val momName = preferences[MOM_NAME_KEY] ?: ""
        val babiesJson = preferences[BABIES_JSON_KEY] ?: "[]"
        val onboardingCompleted = preferences[ONBOARDING_COMPLETED_KEY] ?: false
        val themeStr = preferences[APP_THEME_KEY] ?: AppTheme.GIRL.name
        // Campos para inclusão familiar
        val companionName = preferences[COMPANION_NAME_KEY] ?: ""
        val expectedDueDate = preferences[EXPECTED_DUE_DATE_KEY]
        val currentWeekStr = preferences[CURRENT_WEEK_KEY] ?: "0"
        // EXPANSÃO: tipos de suporte do acompanhante
        val companionSupportTypesStr = preferences[COMPANION_SUPPORT_TYPES_KEY] ?: ""
        
        val babies: List<Baby> = try {
            val type = object : TypeToken<List<Baby>>() {}.type
            gson.fromJson(babiesJson, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
        
        val appTheme = try {
            AppTheme.valueOf(themeStr)
        } catch (e: Exception) {
            AppTheme.GIRL
        }
        
        val currentWeek = try {
            currentWeekStr.toInt()
        } catch (e: Exception) {
            0
        }
        
        // EXPANSÃO: parse dos tipos de suporte
        val companionSupportTypes = try {
            if (companionSupportTypesStr.isBlank()) {
                emptySet()
            } else {
                companionSupportTypesStr.split(",")
                    .mapNotNull { name -> 
                        try { CompanionSupportType.valueOf(name.trim()) } 
                        catch (e: Exception) { null }
                    }
                    .toSet()
            }
        } catch (e: Exception) {
            emptySet()
        }
        
        UserData(
            momName = momName,
            babies = babies,
            onboardingCompleted = onboardingCompleted,
            appTheme = appTheme,
            companionName = companionName,
            expectedDueDate = expectedDueDate,
            currentWeek = currentWeek,
            companionSupportTypes = companionSupportTypes
        )
    }
    
    /**
     * Salva o nome da mamãe
     */
    suspend fun saveMomName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[MOM_NAME_KEY] = name
        }
    }
    
    /**
     * Salva a lista de bebês
     */
    suspend fun saveBabies(babies: List<Baby>) {
        context.dataStore.edit { preferences ->
            preferences[BABIES_JSON_KEY] = gson.toJson(babies)
        }
    }
    
    /**
     * Salva o tema do app
     */
    suspend fun saveAppTheme(theme: AppTheme) {
        context.dataStore.edit { preferences ->
            preferences[APP_THEME_KEY] = theme.name
        }
    }
    
    /**
     * Adiciona um bebê à lista
     */
    suspend fun addBaby(baby: Baby) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[BABIES_JSON_KEY] ?: "[]"
            val currentBabies: MutableList<Baby> = try {
                val type = object : TypeToken<MutableList<Baby>>() {}.type
                gson.fromJson(currentJson, type) ?: mutableListOf()
            } catch (e: Exception) {
                mutableListOf()
            }
            currentBabies.add(baby)
            preferences[BABIES_JSON_KEY] = gson.toJson(currentBabies)
        }
    }
    
    /**
     * Remove um bebê da lista
     */
    suspend fun removeBaby(babyId: String) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[BABIES_JSON_KEY] ?: "[]"
            val currentBabies: MutableList<Baby> = try {
                val type = object : TypeToken<MutableList<Baby>>() {}.type
                gson.fromJson(currentJson, type) ?: mutableListOf()
            } catch (e: Exception) {
                mutableListOf()
            }
            currentBabies.removeAll { it.id == babyId }
            preferences[BABIES_JSON_KEY] = gson.toJson(currentBabies)
        }
    }
    
    /**
     * Marca o onboarding como concluído
     */
    suspend fun completeOnboarding() {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED_KEY] = true
        }
    }
    
    /**
     * Salva todos os dados de uma vez (útil para o onboarding)
     */
    suspend fun saveUserData(momName: String, babies: List<Baby>, theme: AppTheme = AppTheme.GIRL) {
        context.dataStore.edit { preferences ->
            preferences[MOM_NAME_KEY] = momName
            preferences[BABIES_JSON_KEY] = gson.toJson(babies)
            preferences[ONBOARDING_COMPLETED_KEY] = true
            preferences[APP_THEME_KEY] = theme.name
        }
    }
    
    /**
     * NOTA: Esta função foi mantida apenas para compatibilidade.
     * No novo sistema, o gênero do bebê NÃO define a cor do app.
     * O usuário escolhe livremente a paleta de cores que mais gostar!
     */
    @Deprecated("Use a paleta de cores escolhida pelo usuário diretamente")
    fun determineThemeFromBabies(babies: List<Baby>): AppTheme {
        // Retorna o tema padrão - o usuário escolhe a cor que quiser
        return AppTheme.GIRL // Rosa Suave como padrão inicial
    }
    
    /**
     * Reseta todos os dados (para testes ou logout)
     */
    suspend fun clearAllData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    
    // ============ NOVOS MÉTODOS PARA INCLUSÃO FAMILIAR (ADITIVOS) ============
    
    /**
     * Salva o nome da pessoa acompanhante (opcional)
     * Pode ser parceiro(a), familiar, amigo(a) - quem apoiar na jornada
     */
    suspend fun saveCompanionName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[COMPANION_NAME_KEY] = name
        }
    }
    
    /**
     * Salva a data prevista do parto
     * Formato esperado: "dd/MM/yyyy"
     */
    suspend fun saveExpectedDueDate(date: String?) {
        context.dataStore.edit { preferences ->
            if (date != null) {
                preferences[EXPECTED_DUE_DATE_KEY] = date
            } else {
                preferences.remove(EXPECTED_DUE_DATE_KEY)
            }
        }
    }
    
    /**
     * Salva a semana atual da gestação
     */
    suspend fun saveCurrentWeek(week: Int) {
        context.dataStore.edit { preferences ->
            preferences[CURRENT_WEEK_KEY] = week.toString()
        }
    }
    
    /**
     * Salva dados da gestação (data prevista e semana são calculados/informados)
     */
    suspend fun savePregnancyData(
        expectedDueDate: String? = null,
        currentWeek: Int = 0,
        companionName: String = ""
    ) {
        context.dataStore.edit { preferences ->
            if (expectedDueDate != null) {
                preferences[EXPECTED_DUE_DATE_KEY] = expectedDueDate
            }
            preferences[CURRENT_WEEK_KEY] = currentWeek.toString()
            preferences[COMPANION_NAME_KEY] = companionName
        }
    }
    
    // ============ EXPANSÃO DA PESSOA ACOMPANHANTE (ADITIVO) ============
    
    /**
     * Salva os tipos de apoio que a pessoa acompanhante oferece
     * ADITIVO - Não altera nenhuma lógica existente
     */
    suspend fun saveCompanionSupportTypes(supportTypes: Set<CompanionSupportType>) {
        context.dataStore.edit { preferences ->
            preferences[COMPANION_SUPPORT_TYPES_KEY] = supportTypes.joinToString(",") { it.name }
        }
    }
    
    /**
     * Salva todos os dados do acompanhante de uma vez
     * ADITIVO - Conveniência para salvar nome e tipos de apoio juntos
     */
    suspend fun saveCompanionData(name: String, supportTypes: Set<CompanionSupportType> = emptySet()) {
        context.dataStore.edit { preferences ->
            preferences[COMPANION_NAME_KEY] = name
            preferences[COMPANION_SUPPORT_TYPES_KEY] = supportTypes.joinToString(",") { it.name }
        }
    }
}
