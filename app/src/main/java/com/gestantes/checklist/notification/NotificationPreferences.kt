package com.gestantes.checklist.notification

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.notificationDataStore by preferencesDataStore(name = "notification_preferences")

/**
 * Gerenciador de preferências de notificações
 * 
 * ADITIVO - Salva configurações de notificação do usuário
 */
class NotificationPreferencesManager(private val context: Context) {
    
    companion object {
        // Chaves de preferência
        private val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        private val DAILY_REMINDER_ENABLED = booleanPreferencesKey("daily_reminder_enabled")
        private val DAILY_REMINDER_HOUR = intPreferencesKey("daily_reminder_hour")
        private val DAILY_REMINDER_MINUTE = intPreferencesKey("daily_reminder_minute")
        private val PENDING_REMINDER_ENABLED = booleanPreferencesKey("pending_reminder_enabled")
        private val PENDING_REMINDER_HOUR = intPreferencesKey("pending_reminder_hour")
        private val PENDING_REMINDER_MINUTE = intPreferencesKey("pending_reminder_minute")
        private val TIPS_ENABLED = booleanPreferencesKey("tips_enabled")
        private val TIPS_HOUR = intPreferencesKey("tips_hour")
        private val TIPS_MINUTE = intPreferencesKey("tips_minute")
        private val WEEK_UPDATE_ENABLED = booleanPreferencesKey("week_update_enabled")
    }
    
    /**
     * Modelo de dados das preferências de notificação
     */
    data class NotificationSettings(
        val notificationsEnabled: Boolean = true,
        val dailyReminderEnabled: Boolean = true,
        val dailyReminderHour: Int = 8,
        val dailyReminderMinute: Int = 0,
        val pendingReminderEnabled: Boolean = true,
        val pendingReminderHour: Int = 15,
        val pendingReminderMinute: Int = 0,
        val tipsEnabled: Boolean = true,
        val tipsHour: Int = 20,
        val tipsMinute: Int = 0,
        val weekUpdateEnabled: Boolean = true
    )
    
    /**
     * Flow das configurações de notificação
     */
    val settings: Flow<NotificationSettings> = context.notificationDataStore.data.map { preferences ->
        NotificationSettings(
            notificationsEnabled = preferences[NOTIFICATIONS_ENABLED] ?: true,
            dailyReminderEnabled = preferences[DAILY_REMINDER_ENABLED] ?: true,
            dailyReminderHour = preferences[DAILY_REMINDER_HOUR] ?: 8,
            dailyReminderMinute = preferences[DAILY_REMINDER_MINUTE] ?: 0,
            pendingReminderEnabled = preferences[PENDING_REMINDER_ENABLED] ?: true,
            pendingReminderHour = preferences[PENDING_REMINDER_HOUR] ?: 15,
            pendingReminderMinute = preferences[PENDING_REMINDER_MINUTE] ?: 0,
            tipsEnabled = preferences[TIPS_ENABLED] ?: true,
            tipsHour = preferences[TIPS_HOUR] ?: 20,
            tipsMinute = preferences[TIPS_MINUTE] ?: 0,
            weekUpdateEnabled = preferences[WEEK_UPDATE_ENABLED] ?: true
        )
    }
    
    /**
     * Habilita/desabilita todas as notificações
     */
    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.notificationDataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED] = enabled
        }
    }
    
    /**
     * Configura lembrete diário
     */
    suspend fun setDailyReminder(enabled: Boolean, hour: Int = 8, minute: Int = 0) {
        context.notificationDataStore.edit { preferences ->
            preferences[DAILY_REMINDER_ENABLED] = enabled
            preferences[DAILY_REMINDER_HOUR] = hour
            preferences[DAILY_REMINDER_MINUTE] = minute
        }
    }
    
    /**
     * Configura lembrete de pendências
     */
    suspend fun setPendingReminder(enabled: Boolean, hour: Int = 15, minute: Int = 0) {
        context.notificationDataStore.edit { preferences ->
            preferences[PENDING_REMINDER_ENABLED] = enabled
            preferences[PENDING_REMINDER_HOUR] = hour
            preferences[PENDING_REMINDER_MINUTE] = minute
        }
    }
    
    /**
     * Configura dicas diárias
     */
    suspend fun setTips(enabled: Boolean, hour: Int = 20, minute: Int = 0) {
        context.notificationDataStore.edit { preferences ->
            preferences[TIPS_ENABLED] = enabled
            preferences[TIPS_HOUR] = hour
            preferences[TIPS_MINUTE] = minute
        }
    }
    
    /**
     * Configura notificação de nova semana
     */
    suspend fun setWeekUpdateEnabled(enabled: Boolean) {
        context.notificationDataStore.edit { preferences ->
            preferences[WEEK_UPDATE_ENABLED] = enabled
        }
    }
    
    /**
     * Aplica as configurações ao WorkManager
     */
    suspend fun applySettings(settings: NotificationSettings) {
        if (!settings.notificationsEnabled) {
            DailyReminderWorker.cancelAllReminders(context)
            return
        }
        
        if (settings.dailyReminderEnabled) {
            DailyReminderWorker.scheduleDailyReminder(
                context, 
                settings.dailyReminderHour, 
                settings.dailyReminderMinute
            )
        }
        
        if (settings.pendingReminderEnabled) {
            DailyReminderWorker.schedulePendingReminder(
                context,
                settings.pendingReminderHour,
                settings.pendingReminderMinute
            )
        }
        
        if (settings.tipsEnabled) {
            DailyReminderWorker.scheduleDailyTip(
                context,
                settings.tipsHour,
                settings.tipsMinute
            )
        }
    }
}
