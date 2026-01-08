@file:OptIn(ExperimentalMaterial3Api::class)

package com.gestantes.checklist.ui.contraction

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestantes.checklist.data.database.ChecklistDatabase
import com.gestantes.checklist.data.entity.Contraction
import com.gestantes.checklist.data.entity.ContractionIntensity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.layout.statusBarsPadding

@Composable
fun ContractionScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { ChecklistDatabase.getDatabase(context) }
    
    // Session ID - nova sessão a cada abertura
    val sessionId = remember { UUID.randomUUID().toString() }
    
    val contractions by database.contractionDao()
        .getContractionsBySession(sessionId)
        .collectAsState(initial = emptyList())
    
    var isContracting by remember { mutableStateOf(false) }
    var currentContractionStart by remember { mutableStateOf(0L) }
    var currentDuration by remember { mutableStateOf(0) }
    var selectedIntensity by remember { mutableStateOf(ContractionIntensity.MEDIUM) }
    var showIntensitySelector by remember { mutableStateOf(false) }
    
    // Timer para contração em andamento
    LaunchedEffect(isContracting) {
        if (isContracting) {
            currentContractionStart = System.currentTimeMillis()
            while (isContracting) {
                currentDuration = ((System.currentTimeMillis() - currentContractionStart) / 1000).toInt()
                delay(100)
            }
        }
    }
    
    // Stats
    val avgDuration = remember(contractions) {
        if (contractions.isEmpty()) 0
        else contractions.mapNotNull { it.durationSeconds }.average().toInt()
    }
    
    val avgInterval = remember(contractions) {
        if (contractions.size < 2) 0
        else {
            val intervals = contractions.zipWithNext { a, b ->
                (b.startTime - (a.endTime ?: a.startTime)) / 1000
            }
            intervals.average().toInt()
        }
    }
    
    // Alerta de ir ao hospital
    val shouldGoToHospital = contractions.size >= 6 && avgDuration >= 45 && avgInterval in 1..300
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = if (isContracting) {
                        listOf(
                            Color(0xFFFFCDD2),
                            Color(0xFFEF9A9A),
                            Color(0xFFE57373)
                        )
                    } else {
                        listOf(
                            Color(0xFFE3F2FD),
                            Color(0xFFBBDEFB),
                            Color(0xFF90CAF9)
                        )
                    }
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = if (isContracting) Color(0xFFE53935) else Color(0xFF2196F3),
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
                            Icon(Icons.Filled.ArrowBack, "Voltar", tint = Color.White)
                        }
                        
                        Text(
                            text = "⏱️ Contador de Contrações",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        
                        IconButton(onClick = {
                            scope.launch {
                                database.contractionDao().deleteSession(sessionId)
                            }
                        }) {
                            Icon(Icons.Filled.DeleteSweep, "Limpar", tint = Color.White)
                        }
                    }
                }
            }
            
            // Alert
            AnimatedVisibility(visible = shouldGoToHospital) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEB3B)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🏥", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Hora de ir ao hospital!",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF333333)
                            )
                            Text(
                                text = "Contrações regulares de ${avgDuration}s a cada ${avgInterval / 60} min",
                                fontSize = 14.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }
                }
            }
            
            // Main Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Timer
                    if (isContracting) {
                        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                        val scale by infiniteTransition.animateFloat(
                            initialValue = 1f,
                            targetValue = 1.1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(500),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "scale"
                        )
                        
                        Text(
                            text = formatDuration(currentDuration),
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE53935),
                            modifier = Modifier.scale(scale)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Contração em andamento...",
                            fontSize = 16.sp,
                            color = Color(0xFFE53935)
                        )
                    } else {
                        Text(
                            text = if (contractions.isEmpty()) "Pronta?" else "Próxima contração?",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF333333)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Big Button
                    Surface(
                        modifier = Modifier
                            .size(180.dp)
                            .clickable {
                                if (isContracting) {
                                    // Terminar contração
                                    showIntensitySelector = true
                                } else {
                                    // Iniciar contração
                                    isContracting = true
                                    currentDuration = 0
                                }
                            },
                        shape = CircleShape,
                        color = if (isContracting) Color(0xFFE53935) else Color(0xFF2196F3),
                        shadowElevation = 8.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    if (isContracting) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(64.dp)
                                )
                                Text(
                                    text = if (isContracting) "PARAR" else "INICIAR",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = if (isContracting) 
                            "Toque quando a contração terminar" 
                        else 
                            "Toque quando começar a contração",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Stats
            if (contractions.isNotEmpty()) {
                StatsSection(
                    contractionCount = contractions.size,
                    avgDuration = avgDuration,
                    avgInterval = avgInterval,
                    lastContraction = contractions.lastOrNull()
                )
            }
            
            // History
            if (contractions.isNotEmpty()) {
                Text(
                    text = "📋 Histórico da sessão",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(contractions.reversed()) { contraction ->
                        ContractionHistoryItem(
                            contraction = contraction,
                            index = contractions.indexOf(contraction) + 1
                        )
                    }
                }
            }
        }
        
        // Intensity Selector Dialog
        if (showIntensitySelector) {
            IntensitySelectorDialog(
                currentIntensity = selectedIntensity,
                onSelect = { intensity ->
                    selectedIntensity = intensity
                    scope.launch {
                        val endTime = System.currentTimeMillis()
                        database.contractionDao().insert(
                            Contraction(
                                startTime = currentContractionStart,
                                endTime = endTime,
                                durationSeconds = currentDuration,
                                intensity = intensity,
                                sessionId = sessionId
                            )
                        )
                        isContracting = false
                        currentDuration = 0
                        showIntensitySelector = false
                    }
                },
                onDismiss = {
                    scope.launch {
                        val endTime = System.currentTimeMillis()
                        database.contractionDao().insert(
                            Contraction(
                                startTime = currentContractionStart,
                                endTime = endTime,
                                durationSeconds = currentDuration,
                                intensity = selectedIntensity,
                                sessionId = sessionId
                            )
                        )
                        isContracting = false
                        currentDuration = 0
                        showIntensitySelector = false
                    }
                }
            )
        }
    }
}

/**
 * Seção de estatísticas MODERNA
 */
@Composable
private fun StatsSection(
    contractionCount: Int,
    avgDuration: Int,
    avgInterval: Int,
    lastContraction: Contraction?
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatItem(
                value = "$contractionCount",
                label = "Contrações",
                emoji = "🔢"
            )
            
            // Divisor
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(50.dp)
                    .background(Color(0xFFF3F4F6))
            )
            
            StatItem(
                value = "${avgDuration}s",
                label = "Duração",
                emoji = "⏱️"
            )
            
            // Divisor
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(50.dp)
                    .background(Color(0xFFF3F4F6))
            )
            
            StatItem(
                value = if (avgInterval > 60) "${avgInterval / 60}min" else "${avgInterval}s",
                label = "Intervalo",
                emoji = "📊"
            )
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    emoji: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = emoji, fontSize = 24.sp)
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF2196F3)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

/**
 * Card de histórico de contração MODERNO
 */
@Composable
private fun ContractionHistoryItem(
    contraction: Contraction,
    index: Int
) {
    val timeFormat = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }
    val intensityColor = Color(contraction.intensity.color)
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                intensityColor.copy(alpha = 0.08f),
                                Color.White
                            )
                        )
                    )
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Número com gradiente
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        intensityColor,
                                        intensityColor.copy(alpha = 0.7f)
                                    )
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$index",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(14.dp))
                    
                    Column {
                        Text(
                            text = timeFormat.format(Date(contraction.startTime)),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = Color(0xFF1A1A2E)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = contraction.intensity.emoji,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = contraction.intensity.displayName,
                                fontSize = 12.sp,
                                color = intensityColor,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                
                // Duração com destaque
                Box(
                    modifier = Modifier
                        .background(
                            color = intensityColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${contraction.durationSeconds}s",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = intensityColor
                    )
                }
            }
            
            // Linha decorativa
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(30.dp)
                    .align(Alignment.CenterStart)
                    .background(
                        color = intensityColor,
                        shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                    )
            )
        }
    }
}

@Composable
private fun IntensitySelectorDialog(
    currentIntensity: ContractionIntensity,
    onSelect: (ContractionIntensity) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Como foi a intensidade?",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ContractionIntensity.entries.forEach { intensity ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(intensity) },
                        color = Color(intensity.color).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = intensity.emoji, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = intensity.displayName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color(intensity.color)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Pular")
            }
        }
    )
}

private fun formatDuration(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return if (mins > 0) {
        String.format("%d:%02d", mins, secs)
    } else {
        "${secs}s"
    }
}
