package com.gestantes.checklist.notification

import android.content.Context
import androidx.work.*
import com.gestantes.checklist.data.database.ChecklistDatabase
import com.gestantes.checklist.data.preferences.UserPreferencesManager
import kotlinx.coroutines.flow.first
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Worker para enviar notificações diárias
 * 
 * ADITIVO - Novo worker para engajamento diário
 */
class DailyReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            val notificationHelper = NotificationHelper(context)
            val preferencesManager = UserPreferencesManager(context)
            val database = ChecklistDatabase.getDatabase(context)
            
            // Buscar dados do usuário
            val userData = preferencesManager.userData.first()
            
            // Buscar contagem de itens pendentes do checklist semanal
            val pendingCount = try {
                val weeklyDao = database.weeklyCheckDao()
                val totalChecked = weeklyDao.getTotalCheckedCount()
                // Estimar pendentes baseado na semana atual (cada semana tem ~6 itens)
                val estimatedTotal = userData.currentWeek * 6
                maxOf(0, estimatedTotal - totalChecked)
            } catch (e: Exception) {
                0
            }
            
            // Tipo de notificação baseado no input
            val notificationType = inputData.getString("notification_type") ?: "daily"
            
            when (notificationType) {
                "daily" -> {
                    notificationHelper.showDailyReminder(
                        momName = userData.momName,
                        companionName = userData.companionName.takeIf { it.isNotBlank() }
                    )
                }
                "pending" -> {
                    if (pendingCount > 0) {
                        notificationHelper.showPendingTasksReminder(
                            pendingCount = pendingCount,
                            currentWeek = userData.currentWeek
                        )
                    }
                }
                "tip" -> {
                    val tips = getTipsForWeek(userData.currentWeek)
                    if (tips.isNotEmpty()) {
                        val randomTip = tips.random()
                        notificationHelper.showDailyTip(randomTip, userData.currentWeek)
                    }
                }
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
    
    /**
     * Retorna dicas baseadas na semana atual
     */
    private fun getTipsForWeek(week: Int): List<String> {
        return when {
            week <= 12 -> listOf(
                "Primeiro trimestre: é normal sentir mais sono e enjoos. Descanse sempre que puder!",
                "Comece a tomar ácido fólico se ainda não começou. É essencial para o bebê!",
                "Evite alimentos crus e mal passados. Seu sistema imunológico está mais sensível.",
                "Hidrate-se bem! Água é essencial para você e o bebê."
            )
            week <= 26 -> listOf(
                "Segundo trimestre: a energia costuma voltar! Aproveite para organizar as coisas.",
                "Já sentiu o bebê mexer? É uma das sensações mais mágicas da gestação!",
                "Que tal começar a pensar no quartinho do bebê?",
                "Mantenha uma alimentação equilibrada. Seu bebê está crescendo muito!"
            )
            else -> listOf(
                "Terceiro trimestre: a reta final! O bebê está quase pronto para chegar.",
                "Já preparou a mala da maternidade? É hora de conferir!",
                "Descanse bastante e aproveite os últimos momentos antes do bebê chegar.",
                "Converse com seu bebê. Ele já reconhece sua voz!",
                "Fique atenta aos sinais do corpo. Em breve vocês se conhecerão!"
            )
        }
    }
    
    companion object {
        const val WORK_NAME_DAILY = "daily_reminder_work"
        const val WORK_NAME_PENDING = "pending_tasks_work"
        const val WORK_NAME_TIP = "daily_tip_work"
        
        /**
         * Agenda notificação diária para um horário específico
         */
        fun scheduleDailyReminder(context: Context, hour: Int = 8, minute: Int = 0) {
            val workManager = WorkManager.getInstance(context)
            
            // Calcular delay até o próximo horário agendado
            val now = Calendar.getInstance()
            val target = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                
                // Se já passou do horário hoje, agenda para amanhã
                if (before(now)) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }
            
            val delayMillis = target.timeInMillis - now.timeInMillis
            
            val dailyRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
                24, TimeUnit.HOURS
            )
                .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                .setInputData(workDataOf("notification_type" to "daily"))
                .setConstraints(
                    Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .build()
                )
                .build()
            
            workManager.enqueueUniquePeriodicWork(
                WORK_NAME_DAILY,
                ExistingPeriodicWorkPolicy.UPDATE,
                dailyRequest
            )
        }
        
        /**
         * Agenda notificação de pendências (à tarde)
         */
        fun schedulePendingReminder(context: Context, hour: Int = 15, minute: Int = 0) {
            val workManager = WorkManager.getInstance(context)
            
            val now = Calendar.getInstance()
            val target = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                
                if (before(now)) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }
            
            val delayMillis = target.timeInMillis - now.timeInMillis
            
            val pendingRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
                24, TimeUnit.HOURS
            )
                .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                .setInputData(workDataOf("notification_type" to "pending"))
                .setConstraints(
                    Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .build()
                )
                .build()
            
            workManager.enqueueUniquePeriodicWork(
                WORK_NAME_PENDING,
                ExistingPeriodicWorkPolicy.UPDATE,
                pendingRequest
            )
        }
        
        /**
         * Agenda dica diária (à noite)
         */
        fun scheduleDailyTip(context: Context, hour: Int = 20, minute: Int = 0) {
            val workManager = WorkManager.getInstance(context)
            
            val now = Calendar.getInstance()
            val target = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                
                if (before(now)) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }
            
            val delayMillis = target.timeInMillis - now.timeInMillis
            
            val tipRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
                24, TimeUnit.HOURS
            )
                .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                .setInputData(workDataOf("notification_type" to "tip"))
                .setConstraints(
                    Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .build()
                )
                .build()
            
            workManager.enqueueUniquePeriodicWork(
                WORK_NAME_TIP,
                ExistingPeriodicWorkPolicy.UPDATE,
                tipRequest
            )
        }
        
        /**
         * Cancela todas as notificações agendadas
         */
        fun cancelAllReminders(context: Context) {
            val workManager = WorkManager.getInstance(context)
            workManager.cancelUniqueWork(WORK_NAME_DAILY)
            workManager.cancelUniqueWork(WORK_NAME_PENDING)
            workManager.cancelUniqueWork(WORK_NAME_TIP)
        }
        
        /**
         * Agenda todas as notificações com horários padrão
         */
        fun scheduleAllReminders(context: Context) {
            scheduleDailyReminder(context, 8, 0)    // 8:00 - Bom dia
            schedulePendingReminder(context, 15, 0) // 15:00 - Pendências
            scheduleDailyTip(context, 20, 0)        // 20:00 - Dica da noite
        }
    }
}
