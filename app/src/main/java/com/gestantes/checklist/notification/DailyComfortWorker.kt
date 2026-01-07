package com.gestantes.checklist.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Worker que envia notificações diárias de conforto para mamães
 */
class DailyComfortWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        NotificationHelper.showComfortNotification(applicationContext)
        return Result.success()
    }

    companion object {
        private const val WORK_NAME = "daily_comfort_notification"
        
        // Horário padrão: 9h da manhã (horário bom para começar o dia com carinho)
        private const val DEFAULT_HOUR = 9
        private const val DEFAULT_MINUTE = 0

        /**
         * Agenda as notificações diárias de conforto
         */
        fun schedule(context: Context) {
            // Cria o canal de notificação primeiro
            NotificationHelper.createNotificationChannel(context)

            // Calcula o delay inicial para a próxima ocorrência do horário desejado
            val initialDelay = calculateInitialDelay()

            // Cria a requisição de trabalho periódico (a cada 24 horas)
            val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyComfortWorker>(
                24, TimeUnit.HOURS
            )
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .build()

            // Agenda o trabalho (mantém o existente se já estiver agendado)
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                dailyWorkRequest
            )
        }

        /**
         * Cancela as notificações diárias
         */
        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }

        /**
         * Reagenda as notificações (útil após mudança de configuração)
         */
        fun reschedule(context: Context) {
            cancel(context)
            schedule(context)
        }

        /**
         * Calcula o delay inicial até o próximo horário de notificação
         */
        private fun calculateInitialDelay(): Long {
            val now = Calendar.getInstance()
            val targetTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, DEFAULT_HOUR)
                set(Calendar.MINUTE, DEFAULT_MINUTE)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // Se o horário já passou hoje, agenda para amanhã
            if (now.after(targetTime)) {
                targetTime.add(Calendar.DAY_OF_MONTH, 1)
            }

            return targetTime.timeInMillis - now.timeInMillis
        }

        /**
         * Envia uma notificação imediatamente (para teste ou primeira vez)
         */
        fun sendNow(context: Context) {
            NotificationHelper.createNotificationChannel(context)
            NotificationHelper.showComfortNotification(context)
        }
    }
}

