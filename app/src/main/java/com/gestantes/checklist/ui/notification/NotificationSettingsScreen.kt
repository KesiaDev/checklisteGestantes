package com.gestantes.checklist.ui.notification

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.gestantes.checklist.notification.DailyReminderWorker
import com.gestantes.checklist.notification.NotificationHelper
import com.gestantes.checklist.notification.NotificationPreferencesManager
import kotlinx.coroutines.launch

/**
 * Tela de configurações de notificações
 * 
 * ADITIVA - Nova tela para configurar lembretes e notificações
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val preferencesManager = remember { NotificationPreferencesManager(context) }
    val notificationHelper = remember { NotificationHelper(context) }
    
    // Criar canais de notificação
    LaunchedEffect(Unit) {
        notificationHelper.createNotificationChannels()
    }
    
    // Estado das configurações
    val settings by preferencesManager.settings.collectAsState(
        initial = NotificationPreferencesManager.NotificationSettings()
    )
    
    // Estado da permissão de notificação
    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else true
        )
    }
    
    // Launcher para solicitar permissão
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
        if (isGranted) {
            scope.launch {
                preferencesManager.setNotificationsEnabled(true)
                preferencesManager.applySettings(settings.copy(notificationsEnabled = true))
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF8FA),
                        Color(0xFFFFF0F5),
                        Color(0xFFFFE4EC)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFE91E63),
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Voltar",
                                tint = Color.White
                            )
                        }
                        
                        Text(
                            text = "Notificações 🔔",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Configure seus lembretes diários",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Card de permissão (se necessário)
                if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    item {
                        PermissionCard(
                            onRequestPermission = {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        )
                    }
                }
                
                // Switch principal
                item {
                    MainNotificationSwitch(
                        enabled = settings.notificationsEnabled && hasNotificationPermission,
                        onToggle = { enabled ->
                            if (enabled && !hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                scope.launch {
                                    preferencesManager.setNotificationsEnabled(enabled)
                                    if (enabled) {
                                        preferencesManager.applySettings(settings.copy(notificationsEnabled = true))
                                    } else {
                                        DailyReminderWorker.cancelAllReminders(context)
                                    }
                                }
                            }
                        }
                    )
                }
                
                // Configurações individuais (apenas se notificações habilitadas)
                if (settings.notificationsEnabled && hasNotificationPermission) {
                    item {
                        Text(
                            text = "Tipos de Lembretes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    
                    // Lembrete Diário
                    item {
                        NotificationTypeCard(
                            title = "Bom Dia Carinhoso ☀️",
                            description = "Mensagem motivacional todas as manhãs",
                            icon = Icons.Outlined.WbSunny,
                            iconColor = Color(0xFFFFA726),
                            enabled = settings.dailyReminderEnabled,
                            hour = settings.dailyReminderHour,
                            minute = settings.dailyReminderMinute,
                            onToggle = { enabled ->
                                scope.launch {
                                    preferencesManager.setDailyReminder(
                                        enabled,
                                        settings.dailyReminderHour,
                                        settings.dailyReminderMinute
                                    )
                                    preferencesManager.applySettings(
                                        settings.copy(dailyReminderEnabled = enabled)
                                    )
                                }
                            },
                            onTimeChange = { hour, minute ->
                                scope.launch {
                                    preferencesManager.setDailyReminder(
                                        settings.dailyReminderEnabled,
                                        hour,
                                        minute
                                    )
                                    preferencesManager.applySettings(
                                        settings.copy(
                                            dailyReminderHour = hour,
                                            dailyReminderMinute = minute
                                        )
                                    )
                                }
                            }
                        )
                    }
                    
                    // Lembrete de Pendências
                    item {
                        NotificationTypeCard(
                            title = "Tarefas Pendentes 📋",
                            description = "Lembrete das tarefas que faltam completar",
                            icon = Icons.Outlined.Checklist,
                            iconColor = Color(0xFF4CAF50),
                            enabled = settings.pendingReminderEnabled,
                            hour = settings.pendingReminderHour,
                            minute = settings.pendingReminderMinute,
                            onToggle = { enabled ->
                                scope.launch {
                                    preferencesManager.setPendingReminder(
                                        enabled,
                                        settings.pendingReminderHour,
                                        settings.pendingReminderMinute
                                    )
                                    preferencesManager.applySettings(
                                        settings.copy(pendingReminderEnabled = enabled)
                                    )
                                }
                            },
                            onTimeChange = { hour, minute ->
                                scope.launch {
                                    preferencesManager.setPendingReminder(
                                        settings.pendingReminderEnabled,
                                        hour,
                                        minute
                                    )
                                    preferencesManager.applySettings(
                                        settings.copy(
                                            pendingReminderHour = hour,
                                            pendingReminderMinute = minute
                                        )
                                    )
                                }
                            }
                        )
                    }
                    
                    // Dica do Dia
                    item {
                        NotificationTypeCard(
                            title = "Dica da Noite 💡",
                            description = "Dica útil baseada na sua semana de gestação",
                            icon = Icons.Outlined.Lightbulb,
                            iconColor = Color(0xFF9C27B0),
                            enabled = settings.tipsEnabled,
                            hour = settings.tipsHour,
                            minute = settings.tipsMinute,
                            onToggle = { enabled ->
                                scope.launch {
                                    preferencesManager.setTips(
                                        enabled,
                                        settings.tipsHour,
                                        settings.tipsMinute
                                    )
                                    preferencesManager.applySettings(
                                        settings.copy(tipsEnabled = enabled)
                                    )
                                }
                            },
                            onTimeChange = { hour, minute ->
                                scope.launch {
                                    preferencesManager.setTips(
                                        settings.tipsEnabled,
                                        hour,
                                        minute
                                    )
                                    preferencesManager.applySettings(
                                        settings.copy(
                                            tipsHour = hour,
                                            tipsMinute = minute
                                        )
                                    )
                                }
                            }
                        )
                    }
                    
                    // Nova Semana
                    item {
                        SimpleNotificationCard(
                            title = "Nova Semana 🎉",
                            description = "Aviso quando você entra em uma nova semana de gestação",
                            icon = Icons.Outlined.Celebration,
                            iconColor = Color(0xFFE91E63),
                            enabled = settings.weekUpdateEnabled,
                            onToggle = { enabled ->
                                scope.launch {
                                    preferencesManager.setWeekUpdateEnabled(enabled)
                                }
                            }
                        )
                    }
                    
                    // Botão de teste
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        TestNotificationButton(
                            onClick = {
                                notificationHelper.showDailyReminder("Mamãe", null)
                            }
                        )
                    }
                    
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
private fun PermissionCard(
    onRequestPermission: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Filled.NotificationsOff,
                contentDescription = null,
                tint = Color(0xFFE65100),
                modifier = Modifier.size(48.dp)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Permissão Necessária",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE65100)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Para receber lembretes carinhosos, precisamos da sua permissão para enviar notificações.",
                fontSize = 14.sp,
                color = Color(0xFF5D4037),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onRequestPermission,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE91E63)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.Notifications, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Permitir Notificações")
            }
        }
    }
}

@Composable
private fun MainNotificationSwitch(
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) Color(0xFFE8F5E9) else Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        if (enabled) Color(0xFF4CAF50).copy(alpha = 0.2f)
                        else Color(0xFFE0E0E0)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (enabled) Icons.Filled.Notifications else Icons.Filled.NotificationsOff,
                    contentDescription = null,
                    tint = if (enabled) Color(0xFF4CAF50) else Color(0xFF9E9E9E),
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (enabled) "Notificações Ativas" else "Notificações Desativadas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (enabled) Color(0xFF2E7D32) else Color(0xFF666666)
                )
                Text(
                    text = if (enabled) 
                        "Você receberá lembretes carinhosos 💕" 
                    else 
                        "Ative para não perder nenhuma dica!",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
            
            Switch(
                checked = enabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF4CAF50)
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationTypeCard(
    title: String,
    description: String,
    icon: ImageVector,
    iconColor: Color,
    enabled: Boolean,
    hour: Int,
    minute: Int,
    onToggle: (Boolean) -> Unit,
    onTimeChange: (Int, Int) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = hour,
        initialMinute = minute
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF333333)
                    )
                    Text(
                        text = description,
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }
                
                Switch(
                    checked = enabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = iconColor
                    )
                )
            }
            
            // Seletor de horário (quando habilitado)
            AnimatedVisibility(
                visible = enabled,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Horário:",
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                    
                    Surface(
                        onClick = { showTimePicker = true },
                        color = iconColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.Schedule,
                                contentDescription = null,
                                tint = iconColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = String.format("%02d:%02d", hour, minute),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = iconColor
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Dialog do TimePicker
    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Escolha o horário") },
            text = {
                TimePicker(state = timePickerState)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onTimeChange(timePickerState.hour, timePickerState.minute)
                        showTimePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun SimpleNotificationCard(
    title: String,
    description: String,
    icon: ImageVector,
    iconColor: Color,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333)
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }
            
            Switch(
                checked = enabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = iconColor
                )
            )
        }
    }
}

@Composable
private fun TestNotificationButton(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🧪 Testar Notificação",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1565C0)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.Send, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Enviar notificação de teste")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Toque para ver como as notificações aparecem",
                fontSize = 12.sp,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center
            )
        }
    }
}
