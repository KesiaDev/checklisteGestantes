package com.gestantes.checklist.ui.tools

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
import androidx.compose.material.icons.outlined.*
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
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

/**
 * Contador de Chutes/Movimentos do Bebê
 * 
 * Importante para monitorar a saúde do bebê no 3º trimestre.
 * Médicos recomendam contar 10 movimentos em até 2 horas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KickCounterScreen(
    onBackClick: () -> Unit
) {
    var kickCount by remember { mutableStateOf(0) }
    var isCountingSession by remember { mutableStateOf(false) }
    var sessionStartTime by remember { mutableStateOf<Long?>(null) }
    var elapsedTime by remember { mutableStateOf(0L) }
    var kickHistory by remember { mutableStateOf<List<KickRecord>>(emptyList()) }
    var showCelebration by remember { mutableStateOf(false) }
    
    // Timer
    LaunchedEffect(isCountingSession, sessionStartTime) {
        if (isCountingSession && sessionStartTime != null) {
            while (isCountingSession) {
                elapsedTime = System.currentTimeMillis() - sessionStartTime!!
                delay(1000)
            }
        }
    }
    
    // Celebração quando chega a 10 chutes
    LaunchedEffect(kickCount) {
        if (kickCount == 10 && isCountingSession) {
            showCelebration = true
            delay(3000)
            showCelebration = false
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF8FA),
                        Color(0xFFE3F2FD),
                        Color(0xFFBBDEFB)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF2196F3),
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
                            text = "Contador de Chutes 👣",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                }
            }
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Card de informação
                item {
                    InfoCard()
                }
                
                // Contador principal
                item {
                    KickCounterCard(
                        kickCount = kickCount,
                        isCountingSession = isCountingSession,
                        elapsedTime = elapsedTime,
                        onKick = { 
                            if (isCountingSession) {
                                kickCount++
                            }
                        },
                        onStartSession = {
                            kickCount = 0
                            isCountingSession = true
                            sessionStartTime = System.currentTimeMillis()
                            elapsedTime = 0
                        },
                        onEndSession = {
                            if (kickCount > 0) {
                                kickHistory = kickHistory + KickRecord(
                                    date = Date(),
                                    kicks = kickCount,
                                    duration = elapsedTime
                                )
                            }
                            isCountingSession = false
                            sessionStartTime = null
                        }
                    )
                }
                
                // Dica
                item {
                    TipCard()
                }
                
                // Histórico
                if (kickHistory.isNotEmpty()) {
                    item {
                        Text(
                            text = "📊 Histórico",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                    }
                    
                    items(kickHistory.reversed().take(7)) { record ->
                        HistoryItem(record = record)
                    }
                }
                
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
        
        // Celebração overlay
        if (showCelebration) {
            CelebrationOverlay()
        }
    }
}

@Composable
private fun InfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "👶", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Por que contar os movimentos?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2196F3)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Contar os movimentos do bebê ajuda a monitorar o bem-estar dele. " +
                       "A partir da semana 28, é recomendado fazer esse acompanhamento diariamente.",
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun KickCounterCard(
    kickCount: Int,
    isCountingSession: Boolean,
    elapsedTime: Long,
    onKick: () -> Unit,
    onStartSession: () -> Unit,
    onEndSession: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (kickCount > 0) 1f else 0.95f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isCountingSession) {
                // Timer
                Text(
                    text = formatTime(elapsedTime),
                    fontSize = 18.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Contador grande
                Text(
                    text = "$kickCount",
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (kickCount >= 10) Color(0xFF4CAF50) else Color(0xFF2196F3)
                )
                
                Text(
                    text = if (kickCount == 1) "movimento" else "movimentos",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Progresso para 10
                LinearProgressIndicator(
                    progress = minOf(1f, kickCount / 10f),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (kickCount >= 10) Color(0xFF4CAF50) else Color(0xFF2196F3),
                    trackColor = Color(0xFFE0E0E0)
                )
                
                Text(
                    text = if (kickCount >= 10) "✨ Meta atingida!" else "$kickCount de 10",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Botão de registrar chute
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF42A5F5),
                                    Color(0xFF1976D2)
                                )
                            )
                        )
                        .clickable { onKick() },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "👣", fontSize = 36.sp)
                        Text(
                            text = "TOQUE!",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Botão de finalizar
                OutlinedButton(
                    onClick = onEndSession,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFE91E63)
                    )
                ) {
                    Icon(Icons.Filled.Stop, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Finalizar Sessão")
                }
            } else {
                // Tela inicial
                Text(text = "👶", fontSize = 60.sp)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Pronta para contar?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                
                Text(
                    text = "Deite de lado, relaxe e comece a contar\nos movimentos do seu bebê",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onStartSession,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(56.dp)
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Iniciar Contagem", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun TipCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(text = "💡", fontSize = 20.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Dica importante",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE65100)
                )
                Text(
                    text = "Conte 10 movimentos em até 2 horas. Se demorar mais, entre em contato com seu médico. " +
                           "O ideal é fazer a contagem sempre no mesmo horário.",
                    fontSize = 13.sp,
                    color = Color(0xFF5D4037),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun HistoryItem(record: KickRecord) {
    val dateFormat = SimpleDateFormat("dd/MM - HH:mm", Locale.getDefault())
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (record.kicks >= 10) Color(0xFF4CAF50).copy(alpha = 0.15f)
                        else Color(0xFFFF9800).copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${record.kicks}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (record.kicks >= 10) Color(0xFF4CAF50) else Color(0xFFFF9800)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dateFormat.format(record.date),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333)
                )
                Text(
                    text = "Duração: ${formatTime(record.duration)}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            
            if (record.kicks >= 10) {
                Text(text = "✅", fontSize = 20.sp)
            }
        }
    }
}

@Composable
private fun CelebrationOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "🎉", fontSize = 60.sp)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Parabéns!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
                
                Text(
                    text = "10 movimentos registrados!\nSeu bebê está ativo e saudável! 💚",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun formatTime(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    val hours = millis / (1000 * 60 * 60)
    
    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}

data class KickRecord(
    val date: Date,
    val kicks: Int,
    val duration: Long
)
