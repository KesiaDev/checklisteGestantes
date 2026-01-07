package com.gestantes.checklist

import android.app.Application
import com.gestantes.checklist.data.database.ChecklistDatabase
import com.gestantes.checklist.notification.DailyComfortWorker

class ChecklistApp : Application() {
    
    val database: ChecklistDatabase by lazy {
        ChecklistDatabase.getDatabase(this)
    }
    
    override fun onCreate() {
        super.onCreate()
        // Inicializa o banco de dados em background
        database
        
        // Agenda as notificações diárias de conforto para mamães 💕
        DailyComfortWorker.schedule(this)
    }
}

