package com.gestantes.checklist.ui.timeline

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestantes.checklist.data.preferences.UserPreferencesManager
import com.gestantes.checklist.data.preferences.UserData
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.layout.statusBarsPadding

/**
 * Tela de Linha do Tempo da Gestação
 * 
 * Exibe uma visualização da jornada da gestação com marcos importantes.
 * Esta é uma tela APENAS LEITURA e VISUAL.
 * 
 * NOTA: Esta é uma tela NOVA e ADITIVA - não interfere em fluxos existentes
 */

data class PregnancyMilestone(
    val week: Int,
    val title: String,
    val description: String,
    val emoji: String,
    val category: MilestoneCategory
)

enum class MilestoneCategory(val color: Long) {
    DEVELOPMENT(0xFFE91E63),  // Rosa - desenvolvimento do bebê
    MEDICAL(0xFF2196F3),       // Azul - consultas/exames
    PREPARATION(0xFF4CAF50),   // Verde - preparativos
    SPECIAL(0xFFFF9800)        // Laranja - momentos especiais
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(
    onBackClick: () -> Unit,
    currentWeek: Int = 20 // Semana atual da gestação
) {
    val milestones = remember { getPregnancyMilestones() }
    
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
            TimelineHeader(
                currentWeek = currentWeek,
                onBackClick = onBackClick
            )
            
            // Progress indicator
            CurrentWeekCard(currentWeek = currentWeek)
            
            // Timeline
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                itemsIndexed(milestones) { index, milestone ->
                    TimelineMilestoneItem(
                        milestone = milestone,
                        isPast = milestone.week < currentWeek,
                        isCurrent = milestone.week == currentWeek || 
                            (index < milestones.size - 1 && 
                             milestone.week < currentWeek && 
                             milestones[index + 1].week > currentWeek),
                        isLast = index == milestones.lastIndex
                    )
                }
                
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
private fun TimelineHeader(
    currentWeek: Int,
    onBackClick: () -> Unit
) {
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
                    text = "Linha do Tempo 📅",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.width(48.dp))
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Sua jornada até o grande dia",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CurrentWeekCard(currentWeek: Int) {
    val trimester = when {
        currentWeek <= 12 -> "1º Trimestre"
        currentWeek <= 27 -> "2º Trimestre"
        else -> "3º Trimestre"
    }
    
    val weeksRemaining = (40 - currentWeek).coerceAtLeast(0)
    val progress = (currentWeek.toFloat() / 40f).coerceIn(0f, 1f)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Semana atual
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🗓️",
                        fontSize = 24.sp
                    )
                    Text(
                        text = "Semana $currentWeek",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE91E63)
                    )
                    Text(
                        text = trimester,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                
                // Divisor
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(50.dp)
                        .background(Color.LightGray)
                )
                
                // Semanas restantes
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "⏳",
                        fontSize = 24.sp
                    )
                    Text(
                        text = "$weeksRemaining semanas",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                    Text(
                        text = "para o grande dia",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Barra de progresso
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Progresso da gestação",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE91E63)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = Color(0xFFE91E63),
                    trackColor = Color(0xFFE91E63).copy(alpha = 0.2f)
                )
            }
        }
    }
}

@Composable
private fun TimelineMilestoneItem(
    milestone: PregnancyMilestone,
    isPast: Boolean,
    isCurrent: Boolean,
    isLast: Boolean
) {
    val milestoneColor = Color(milestone.category.color)
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        // Coluna da linha do tempo
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(40.dp)
        ) {
            // Ícone do marco
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (isPast || isCurrent) milestoneColor else milestoneColor.copy(alpha = 0.3f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isPast) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else if (isCurrent) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.5f))
                    )
                }
            }
            
            // Linha conectora
            if (!isLast) {
                Canvas(
                    modifier = Modifier
                        .width(2.dp)
                        .height(60.dp)
                ) {
                    drawLine(
                        color = if (isPast) milestoneColor else Color.LightGray,
                        start = Offset(size.width / 2, 0f),
                        end = Offset(size.width / 2, size.height),
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = if (!isPast) PathEffect.dashPathEffect(floatArrayOf(10f, 10f)) else null
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Card do marco
        Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(
                containerColor = if (isCurrent) milestoneColor.copy(alpha = 0.1f) else Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(if (isCurrent) 4.dp else 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = milestone.emoji,
                    fontSize = 24.sp
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = milestone.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isPast) Color.Gray else Color(0xFF333333)
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Surface(
                            color = milestoneColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Sem ${milestone.week}",
                                fontSize = 10.sp,
                                color = milestoneColor,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = milestone.description,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    
                    if (isCurrent) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "📍 Você está aqui",
                            fontSize = 11.sp,
                            color = milestoneColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

/**
 * Lista de marcos importantes da gestação
 */
private fun getPregnancyMilestones(): List<PregnancyMilestone> = listOf(
    PregnancyMilestone(
        week = 4,
        title = "Teste positivo!",
        description = "A jornada começa 💕",
        emoji = "🎉",
        category = MilestoneCategory.SPECIAL
    ),
    PregnancyMilestone(
        week = 6,
        title = "Primeira consulta",
        description = "Primeiro encontro com o médico",
        emoji = "👩‍⚕️",
        category = MilestoneCategory.MEDICAL
    ),
    PregnancyMilestone(
        week = 8,
        title = "Batimentos cardíacos",
        description = "Primeiro ultrassom e batimentos do coração",
        emoji = "💓",
        category = MilestoneCategory.DEVELOPMENT
    ),
    PregnancyMilestone(
        week = 12,
        title = "Fim do 1º trimestre",
        description = "Ultrassom morfológico e translucência nucal",
        emoji = "🎊",
        category = MilestoneCategory.MEDICAL
    ),
    PregnancyMilestone(
        week = 16,
        title = "Primeiros movimentos",
        description = "Você pode começar a sentir o bebê",
        emoji = "👶",
        category = MilestoneCategory.DEVELOPMENT
    ),
    PregnancyMilestone(
        week = 20,
        title = "Metade da gestação!",
        description = "Ultrassom morfológico - descobrir o sexo",
        emoji = "🎀",
        category = MilestoneCategory.SPECIAL
    ),
    PregnancyMilestone(
        week = 24,
        title = "Viabilidade fetal",
        description = "Marco importante de desenvolvimento",
        emoji = "⭐",
        category = MilestoneCategory.DEVELOPMENT
    ),
    PregnancyMilestone(
        week = 28,
        title = "Início do 3º trimestre",
        description = "Vacinas e exames importantes",
        emoji = "💉",
        category = MilestoneCategory.MEDICAL
    ),
    PregnancyMilestone(
        week = 32,
        title = "Preparação final",
        description = "Ultrassom de crescimento, preparar enxoval",
        emoji = "🍼",
        category = MilestoneCategory.PREPARATION
    ),
    PregnancyMilestone(
        week = 36,
        title = "Bebê a termo precoce",
        description = "Consultas semanais, mala pronta",
        emoji = "🧳",
        category = MilestoneCategory.PREPARATION
    ),
    PregnancyMilestone(
        week = 37,
        title = "Bebê a termo",
        description = "Pode nascer a qualquer momento!",
        emoji = "🌟",
        category = MilestoneCategory.DEVELOPMENT
    ),
    PregnancyMilestone(
        week = 40,
        title = "Data prevista do parto",
        description = "O grande dia está chegando!",
        emoji = "👣",
        category = MilestoneCategory.SPECIAL
    )
)

