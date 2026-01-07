package com.gestantes.checklist.ui.weekly

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestantes.checklist.data.guides.PregnancyWeeklyData
import com.gestantes.checklist.data.guides.WeeklyChecklist

/**
 * Tela de Checklists por Semana da Gestação
 * 
 * NOTA: Esta é uma tela NOVA e ADITIVA
 * Não altera os checklists existentes (Maternidade, Pré-natal, Pós-parto)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyChecklistScreen(
    onBackClick: () -> Unit,
    currentWeek: Int = 12 // Semana atual da gestação
) {
    var selectedWeek by remember { mutableStateOf<WeeklyChecklist?>(null) }
    val allChecklists = PregnancyWeeklyData.weeklyChecklists
    
    // Estado local dos itens marcados (não persiste - é apenas visual)
    var checkedItems by remember { mutableStateOf<Set<String>>(emptySet()) }
    
    LaunchedEffect(Unit) {
        selectedWeek = PregnancyWeeklyData.getChecklistForWeek(currentWeek)
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
                Column(modifier = Modifier.padding(16.dp)) {
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
                            text = "Checklists Semanais 📅",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Acompanhe sua gestação semana a semana",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Seletor de semanas
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(allChecklists) { checklist ->
                    WeekChip(
                        checklist = checklist,
                        isSelected = selectedWeek?.week == checklist.week,
                        isPast = checklist.week < currentWeek,
                        isCurrent = checklist.week == PregnancyWeeklyData.getChecklistForWeek(currentWeek)?.week,
                        onClick = { selectedWeek = checklist }
                    )
                }
            }
            
            // Conteúdo do checklist selecionado
            selectedWeek?.let { week ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Card de informação da semana
                    item {
                        WeekInfoCard(week)
                    }
                    
                    // Lista de itens do checklist
                    item {
                        Text(
                            text = "📋 O que fazer nessa semana",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    
                    items(week.items) { item ->
                        WeeklyChecklistItem(
                            text = item,
                            isChecked = checkedItems.contains("${week.week}-$item"),
                            onCheckedChange = { checked ->
                                checkedItems = if (checked) {
                                    checkedItems + "${week.week}-$item"
                                } else {
                                    checkedItems - "${week.week}-$item"
                                }
                            }
                        )
                    }
                    
                    // Dicas da semana
                    if (week.tips.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            TipsCard(tips = week.tips)
                        }
                    }
                    
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
private fun WeekChip(
    checklist: WeeklyChecklist,
    isSelected: Boolean,
    isPast: Boolean,
    isCurrent: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> Color(0xFFE91E63)
        isCurrent -> Color(0xFFE91E63).copy(alpha = 0.3f)
        isPast -> Color(0xFF4CAF50).copy(alpha = 0.2f)
        else -> Color.White
    }
    
    val textColor = when {
        isSelected -> Color.White
        else -> Color(0xFF333333)
    }
    
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        shadowElevation = if (isSelected) 4.dp else 1.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = checklist.emoji,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Sem ${checklist.week}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
            if (isCurrent && !isSelected) {
                Text(
                    text = "atual",
                    fontSize = 10.sp,
                    color = Color(0xFFE91E63)
                )
            }
        }
    }
}

@Composable
private fun WeekInfoCard(week: WeeklyChecklist) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE91E63).copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = week.emoji,
                    fontSize = 28.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = week.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE91E63)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = week.description,
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

@Composable
private fun WeeklyChecklistItem(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isChecked) {
            Color(0xFF4CAF50).copy(alpha = 0.15f)
        } else {
            Color.White
        },
        label = "backgroundColor"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF4CAF50),
                    uncheckedColor = Color(0xFFE91E63)
                )
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = text,
                fontSize = 14.sp,
                color = if (isChecked) Color(0xFF666666) else Color(0xFF333333),
                modifier = Modifier.weight(1f)
            )
            
            if (isChecked) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun TipsCard(tips: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "💡",
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Dicas para essa fase",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFE65100)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            tips.forEach { tip ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "•",
                        fontSize = 14.sp,
                        color = Color(0xFFE65100)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tip,
                        fontSize = 13.sp,
                        color = Color(0xFF5D4037)
                    )
                }
            }
        }
    }
}

