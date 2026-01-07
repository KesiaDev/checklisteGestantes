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
 * Tema de cores do app
 */
enum class AppTheme(val displayName: String) {
    GIRL("Tema Menina (Rosa)"),      // Rosa e verde claro
    BOY("Tema Menino (Azul)"),       // Azul e verde
    NEUTRAL("Tema Neutro (Verde)"),  // Verde e amarelo
    CUSTOM("Personalizado")           // Deixa o usuário escolher
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
    val appTheme: AppTheme = AppTheme.GIRL // Tema padrão
)

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
    }
    
    /**
     * Flow que emite os dados do usuário sempre que mudarem
     */
    val userData: Flow<UserData> = context.dataStore.data.map { preferences ->
        val momName = preferences[MOM_NAME_KEY] ?: ""
        val babiesJson = preferences[BABIES_JSON_KEY] ?: "[]"
        val onboardingCompleted = preferences[ONBOARDING_COMPLETED_KEY] ?: false
        val themeStr = preferences[APP_THEME_KEY] ?: AppTheme.GIRL.name
        
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
        
        UserData(
            momName = momName,
            babies = babies,
            onboardingCompleted = onboardingCompleted,
            appTheme = appTheme
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
     * Determina o tema baseado nos bebês cadastrados
     */
    fun determineThemeFromBabies(babies: List<Baby>): AppTheme {
        if (babies.isEmpty()) return AppTheme.GIRL
        
        val hasGirl = babies.any { it.gender == BabyGender.GIRL }
        val hasBoy = babies.any { it.gender == BabyGender.BOY }
        
        return when {
            hasGirl && hasBoy -> AppTheme.NEUTRAL // Tem menino e menina
            hasBoy -> AppTheme.BOY                 // Só menino(s)
            hasGirl -> AppTheme.GIRL               // Só menina(s)
            else -> AppTheme.GIRL                  // Padrão ou não sabe
        }
    }
    
    /**
     * Reseta todos os dados (para testes ou logout)
     */
    suspend fun clearAllData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
