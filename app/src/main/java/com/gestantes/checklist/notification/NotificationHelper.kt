package com.gestantes.checklist.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gestantes.checklist.MainActivity
import com.gestantes.checklist.R
import kotlin.random.Random

/**
 * Helper para gerenciar notificações do app
 * 
 * ADITIVO - Novo sistema de notificações para engajamento
 */
class NotificationHelper(private val context: Context) {
    
    companion object {
        // Canais de notificação
        const val CHANNEL_DAILY_REMINDER = "daily_reminder"
        const val CHANNEL_PENDING_TASKS = "pending_tasks"
        const val CHANNEL_WEEK_UPDATE = "week_update"
        const val CHANNEL_TIPS = "tips"
        const val CHANNEL_COMFORT = "comfort_channel" // Para compatibilidade
        
        // IDs de notificação
        const val NOTIFICATION_DAILY = 1001
        const val NOTIFICATION_PENDING = 1002
        const val NOTIFICATION_WEEK = 1003
        const val NOTIFICATION_TIP = 1004
        const val NOTIFICATION_COMFORT = 1005
        
        /**
         * Cria canal de notificação (método estático para compatibilidade)
         */
        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_COMFORT,
                    "Mensagens de Conforto",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Mensagens diárias de apoio e carinho para mamães"
                }
                
                val notificationManager = context.getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }
        }
        
        /**
         * Mostra notificação de conforto (método estático para compatibilidade)
         */
        fun showComfortNotification(context: Context) {
            createNotificationChannel(context)
            
            val message = ComfortMessages.getDailyMessage()
            val title = ComfortMessages.getRandomTitle()
            
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            val notification = NotificationCompat.Builder(context, CHANNEL_COMFORT)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
            
            try {
                NotificationManagerCompat.from(context).notify(NOTIFICATION_COMFORT, notification)
            } catch (e: SecurityException) {
                // Permissão não concedida
            }
        }
    }
    
    /**
     * Cria todos os canais de notificação necessários
     */
    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            
            // Canal de lembrete diário
            val dailyChannel = NotificationChannel(
                CHANNEL_DAILY_REMINDER,
                "Lembrete Diário",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Mensagens diárias de carinho e motivação"
                enableVibration(true)
            }
            
            // Canal de tarefas pendentes
            val pendingChannel = NotificationChannel(
                CHANNEL_PENDING_TASKS,
                "Tarefas Pendentes",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Lembretes sobre itens do checklist pendentes"
                enableVibration(true)
            }
            
            // Canal de atualização de semana
            val weekChannel = NotificationChannel(
                CHANNEL_WEEK_UPDATE,
                "Nova Semana",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificação quando você entra em uma nova semana de gestação"
                enableVibration(true)
            }
            
            // Canal de dicas
            val tipsChannel = NotificationChannel(
                CHANNEL_TIPS,
                "Dicas da Semana",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Dicas e informações sobre sua gestação"
            }
            
            notificationManager.createNotificationChannels(
                listOf(dailyChannel, pendingChannel, weekChannel, tipsChannel)
            )
        }
    }
    
    /**
     * Envia notificação de lembrete diário com mensagem carinhosa
     */
    fun showDailyReminder(momName: String, companionName: String? = null) {
        val message = getDailyMessage(momName, companionName)
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_DAILY_REMINDER)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Bom dia, ${momName.ifEmpty { "mamãe" }}! 💕")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_DAILY, notification)
        } catch (e: SecurityException) {
            // Permissão não concedida
        }
    }
    
    /**
     * Envia notificação de tarefas pendentes
     */
    fun showPendingTasksReminder(pendingCount: Int, currentWeek: Int) {
        if (pendingCount <= 0) return
        
        val title = when {
            pendingCount == 1 -> "Você tem 1 tarefa pendente 📋"
            pendingCount <= 3 -> "Você tem $pendingCount tarefas pendentes 📋"
            else -> "Você tem $pendingCount tarefas te esperando! 📋"
        }
        
        val message = when {
            pendingCount == 1 -> "Que tal completar agora? É rapidinho! 💪"
            pendingCount <= 3 -> "Vamos dar uma olhadinha juntas? Estou aqui para te ajudar!"
            else -> "Sem pressa, mamãe! Cada item no seu tempo. Vamos ver o que temos?"
        }
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("open_checklist", true)
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 1, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_PENDING_TASKS)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_PENDING, notification)
        } catch (e: SecurityException) {
            // Permissão não concedida
        }
    }
    
    /**
     * Envia notificação de nova semana de gestação
     */
    fun showNewWeekNotification(newWeek: Int, weekEmoji: String, weekDescription: String) {
        val title = "🎉 Parabéns! Semana $newWeek!"
        val message = "$weekEmoji $weekDescription\n\nVem ver o que preparamos para essa nova fase!"
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("open_weekly", true)
            putExtra("current_week", newWeek)
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 2, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_WEEK_UPDATE)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_WEEK, notification)
        } catch (e: SecurityException) {
            // Permissão não concedida
        }
    }
    
    /**
     * Envia dica do dia
     */
    fun showDailyTip(tip: String, currentWeek: Int) {
        val title = "💡 Dica da Semana $currentWeek"
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 3, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_TIPS)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(tip)
            .setStyle(NotificationCompat.BigTextStyle().bigText(tip))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_TIP, notification)
        } catch (e: SecurityException) {
            // Permissão não concedida
        }
    }
    
    /**
     * Gera mensagem diária personalizada
     */
    private fun getDailyMessage(momName: String, companionName: String?): String {
        val messages = listOf(
            "Cada dia é um passo mais perto do seu bebê. Você está fazendo um trabalho incrível! 🌟",
            "Lembre-se de beber bastante água hoje e descansar quando precisar. Você merece! 💧",
            "Seu corpo está criando uma vida. Que milagre! Dê a si mesma o carinho que merece. 💕",
            "Hoje é um ótimo dia para se conectar com seu bebê. Que tal uma conversinha? 🥰",
            "Respire fundo e aproveite cada momento dessa jornada mágica. ✨",
            "Você é forte, você é capaz, você é uma mãe incrível! 💪",
            "Cada semana traz novas descobertas. Vamos ver o que temos de novo? 📋",
            "Seu bebê está crescendo e você também está se transformando. Celebre isso! 🎉",
            "Lembre-se: não existe mãe perfeita, existe mãe presente e amorosa. E você é! 💗",
            "Hoje é um bom dia para anotar algo no seu diário. O que você está sentindo? 📝"
        )
        
        val baseMessage = messages[Random.nextInt(messages.size)]
        
        return if (!companionName.isNullOrBlank()) {
            "$baseMessage\n\n${companionName} está com você nessa jornada! 👨‍👩‍👧"
        } else {
            baseMessage
        }
    }
}
