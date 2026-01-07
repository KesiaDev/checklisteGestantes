package com.gestantes.checklist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.gestantes.checklist.data.preferences.AppTheme
import com.gestantes.checklist.data.preferences.UserPreferencesManager
import com.gestantes.checklist.navigation.AppNavigation
import com.gestantes.checklist.ui.theme.ChecklistGestantesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            val context = LocalContext.current
            val preferencesManager = remember { UserPreferencesManager(context) }
            val userData by preferencesManager.userData.collectAsState(initial = null)
            
            // Pega o tema das preferências ou usa o padrão
            val appTheme = userData?.appTheme ?: AppTheme.GIRL
            
            ChecklistGestantesTheme(appTheme = appTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}
