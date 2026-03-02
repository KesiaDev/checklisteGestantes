package com.gestantes.checklist.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestantes.checklist.ai.AICompanion
import com.gestantes.checklist.ai.AIMessage
import kotlinx.coroutines.delay

/**
 * Componentes visuais da IA Companheira "Lumi"
 */

// Cores da IA Lumi
val LumiPrimary = Color(0xFF9C27B0)      // Roxo principal
val LumiSecondary = Color(0xFFE1BEE7)    // Roxo claro
val LumiAccent = Color(0xFFFF4081)       // Rosa accent
val LumiGradientStart = Color(0xFFCE93D8)
val LumiGradientEnd = Color(0xFF9C27B0)

/**
 * Balão de fala da IA Lumi
 */
@Composable
fun AIBubble(
    message: String,
    modifier: Modifier = Modifier,
    showAvatar: Boolean = true,
    onDismiss: (() -> Unit)? = null
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Top
        ) {
            if (showAvatar) {
                // Avatar da Lumi
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(LumiGradientStart, LumiGradientEnd)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✨",
                        fontSize = 20.sp
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
            }
            
            // Balão de mensagem
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(
                    topStart = 4.dp,
                    topEnd = 16.dp,
                    bottomEnd = 16.dp,
                    bottomStart = 16.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = LumiSecondary.copy(alpha = 0.3f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Lumi",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = LumiPrimary
                        )
                        
                        Spacer(modifier = Modifier.width(4.dp))
                        
                        Text(
                            text = "• sua companheira ✨",
                            fontSize = 10.sp,
                            color = LumiPrimary.copy(alpha = 0.7f)
                        )
                        
                        if (onDismiss != null) {
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(
                                onClick = { 
                                    visible = false
                                    onDismiss()
                                },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "Fechar",
                                    tint = LumiPrimary.copy(alpha = 0.5f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    Text(
                        text = message,
                        fontSize = 14.sp,
                        color = Color(0xFF333333),
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

/**
 * Card de dica da IA com ícone
 */
@Composable
fun AITipCard(
    message: String,
    icon: ImageVector = Icons.Outlined.Lightbulb,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            LumiSecondary.copy(alpha = 0.2f),
                            LumiSecondary.copy(alpha = 0.05f)
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ícone com gradiente
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(LumiGradientStart, LumiGradientEnd)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✨ Lumi diz:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = LumiPrimary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = message,
                        fontSize = 14.sp,
                        color = Color(0xFF333333),
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

// Gradiente cacheado para o card de celebração
private val CelebrationGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFFFF8E1), Color(0xFFFFF3E0))
)

/**
 * Card de celebração animado
 * OTIMIZADO: Animação de confete removida (consumia CPU desnecessariamente)
 */
@Composable
fun AICelebrationCard(
    message: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {
    var visible by remember { mutableStateOf(true) }
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )
    
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .scale(scale)
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CelebrationGradient)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Emojis de celebração (estáticos para performance)
                    Row {
                        Text(text = "🎉", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "✨", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "🎊", fontSize = 32.sp)
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Parabéns!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6F00)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = message,
                        fontSize = 16.sp,
                        color = Color(0xFF5D4037),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "— Lumi ✨",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        color = LumiPrimary
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = { 
                            visible = false
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LumiPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Obrigada, Lumi! 💕")
                    }
                }
            }
        }
    }
}

/**
 * Pergunta reflexiva da IA
 */
@Composable
fun AIQuestionCard(
    question: String,
    modifier: Modifier = Modifier,
    onAnswer: ((String) -> Unit)? = null
) {
    var answer by remember { mutableStateOf("") }
    var showTextField by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3E5F5)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar Lumi
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(LumiPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "✨", fontSize = 18.sp)
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = "Lumi quer saber...",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = LumiPrimary
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = question,
                fontSize = 16.sp,
                color = Color(0xFF333333),
                fontStyle = FontStyle.Italic,
                lineHeight = 22.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            if (showTextField) {
                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Sua resposta...") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LumiPrimary,
                        cursorColor = LumiPrimary
                    )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { showTextField = false }) {
                        Text("Cancelar")
                    }
                    
                    Button(
                        onClick = {
                            onAnswer?.invoke(answer)
                            showTextField = false
                            answer = ""
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LumiPrimary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Enviar 💕")
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { showTextField = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = LumiPrimary
                        )
                    ) {
                        Icon(Icons.Outlined.Edit, null, Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Responder")
                    }
                    
                    OutlinedButton(
                        onClick = { /* skip */ },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Pular")
                    }
                }
            }
        }
    }
}

// Gradientes cacheados para evitar recriação a cada recomposição
private val LumiBannerGradient = Brush.horizontalGradient(
    colors = listOf(
        LumiGradientStart.copy(alpha = 0.3f),
        LumiGradientEnd.copy(alpha = 0.1f)
    )
)

private val LumiAvatarGradient = Brush.radialGradient(
    colors = listOf(LumiGradientStart, LumiGradientEnd)
)

/**
 * Banner de boas-vindas da IA
 * OTIMIZADO: Removida animação infinita que consumia CPU
 */
@Composable
fun AIWelcomeBanner(
    momName: String,
    currentWeek: Int,
    modifier: Modifier = Modifier
) {
    // Cache das mensagens para evitar recálculo
    val greeting = remember(currentWeek) { AICompanion.getGreeting() }
    val funFact = remember(currentWeek) { AICompanion.getFunFact(currentWeek) }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(LumiBannerGradient)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar ESTÁTICO (removida animação infinita que consumia CPU)
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(LumiAvatarGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "✨", fontSize = 26.sp)
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = if (momName.isNotBlank()) "Olá, $momName!" else "Olá, mamãe!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                        Text(
                            text = "Sou a Lumi, sua companheira ✨",
                            fontSize = 13.sp,
                            color = LumiPrimary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = greeting,
                    fontSize = 15.sp,
                    color = Color(0xFF555555),
                    lineHeight = 22.sp
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Curiosidade da semana
                Surface(
                    color = Color(0xFFFFF8E1),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "💡", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = funFact,
                            fontSize = 13.sp,
                            color = Color(0xFF5D4037),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Mini dica flutuante da IA
 */
@Composable
fun AIMiniTip(
    message: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = LumiSecondary.copy(alpha = 0.3f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "✨", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = message,
                fontSize = 12.sp,
                color = Color(0xFF555555)
            )
        }
    }
}

/**
 * Botão de ajuda da IA
 */
@Composable
fun AIHelpButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(56.dp),
        containerColor = LumiPrimary,
        contentColor = Color.White,
        shape = CircleShape
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(text = "✨", fontSize = 24.sp)
        }
    }
}

/**
 * Dialog de ajuda da IA
 */
@Composable
fun AIHelpDialog(
    context: AICompanion.Context,
    currentWeek: Int,
    onDismiss: () -> Unit
) {
    val tip = remember { AICompanion.getTip(context, currentWeek) }
    val encouragement = remember { AICompanion.getEncouragement() }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        icon = {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(LumiGradientStart, LumiGradientEnd)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "✨", fontSize = 32.sp)
            }
        },
        title = {
            Text(
                text = "Lumi está aqui! 💕",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Dica
                Surface(
                    color = LumiSecondary.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "💡 Dica",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = LumiPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tip,
                            fontSize = 14.sp,
                            color = Color(0xFF333333)
                        )
                    }
                }
                
                // Encorajamento
                Text(
                    text = encouragement,
                    fontSize = 14.sp,
                    color = Color(0xFF555555),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LumiPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Obrigada! 💕")
            }
        }
    )
}

/**
 * Indicador de progresso com celebração da IA
 * OTIMIZADO: Animação de celebração removida (só mostrava quando 100%)
 */
@Composable
fun AIProgressCelebration(
    progress: Float,
    message: String,
    modifier: Modifier = Modifier
) {
    val showCelebration = progress >= 1f
    
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showCelebration) {
            // Emojis estáticos (removida animação infinita)
            Text(
                text = "🎉✨🎊",
                fontSize = 32.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        Text(
            text = message,
            fontSize = 14.sp,
            color = if (showCelebration) Color(0xFFFF6F00) else LumiPrimary,
            fontWeight = if (showCelebration) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center
        )
        
        if (!showCelebration) {
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = LumiPrimary,
                trackColor = LumiSecondary.copy(alpha = 0.3f)
            )
        }
    }
}
