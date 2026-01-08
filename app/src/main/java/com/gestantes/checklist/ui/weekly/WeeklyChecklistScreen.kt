package com.gestantes.checklist.ui.weekly

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestantes.checklist.ai.AICompanion
import com.gestantes.checklist.data.database.ChecklistDatabase
import com.gestantes.checklist.data.entity.WeeklyCheckItem
import com.gestantes.checklist.data.guides.PregnancyWeeklyData
import com.gestantes.checklist.data.guides.WeeklyChecklist
import com.gestantes.checklist.ui.components.AIBubble
import com.gestantes.checklist.ui.components.AICelebrationCard
import com.gestantes.checklist.ui.components.LumiPrimary
import kotlinx.coroutines.launch

/**
 * Tela de Checklists por Semana da Gestação
 * 
 * NOTA: Esta é uma tela NOVA e ADITIVA
 * Não altera os checklists existentes (Maternidade, Pré-natal, Pós-parto)
 * 
 * ATUALIZADO: Agora salva o progresso no banco de dados!
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyChecklistScreen(
    onBackClick: () -> Unit,
    currentWeek: Int = 12 // Semana atual da gestação
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { ChecklistDatabase.getDatabase(context) }
    val weeklyCheckDao = remember { database.weeklyCheckDao() }
    
    var selectedWeek by remember { mutableStateOf<WeeklyChecklist?>(null) }
    val allChecklists = PregnancyWeeklyData.weeklyChecklists
    
    // Estado dos itens marcados - carregado do banco de dados
    var checkedItems by remember { mutableStateOf<Set<String>>(emptySet()) }
    
    // Estado para semanas com progresso (para mostrar indicador nos chips)
    var weeksWithProgress by remember { mutableStateOf<Set<Int>>(emptySet()) }
    
    // ✨ Estados da IA Lumi
    var showCelebration by remember { mutableStateOf(false) }
    var celebrationMessage by remember { mutableStateOf("") }
    var showAITip by remember { mutableStateOf(true) }
    val aiTip = remember { AICompanion.getTip(AICompanion.Context.WEEKLY_CHECKLIST, currentWeek) }
    
    // Carregar todas semanas que tem progresso
    LaunchedEffect(Unit) {
        weeklyCheckDao.getAllCheckedItems().collect { items ->
            weeksWithProgress = items.map { it.week }.toSet()
        }
    }
    
    // Carregar itens marcados do banco quando a semana muda
    LaunchedEffect(selectedWeek?.week) {
        selectedWeek?.let { week ->
            weeklyCheckDao.getItemsForWeek(week.week).collect { items ->
                checkedItems = items.filter { it.isChecked }.map { it.id }.toSet()
            }
        }
    }
    
    LaunchedEffect(Unit) {
        selectedWeek = PregnancyWeeklyData.getChecklistForWeek(currentWeek)
    }
    
    /**
     * Função para marcar/desmarcar item e salvar no banco
     */
    fun toggleItem(week: Int, itemText: String, isChecked: Boolean) {
        val id = WeeklyCheckItem.createId(week, itemText)
        
        scope.launch {
            val item = WeeklyCheckItem(
                id = id,
                week = week,
                itemText = itemText,
                isChecked = isChecked,
                checkedAt = if (isChecked) System.currentTimeMillis() else null
            )
            weeklyCheckDao.upsertItem(item)
            
            // Atualizar estado local
            val newCheckedItems = if (isChecked) {
                checkedItems + id
            } else {
                checkedItems - id
            }
            checkedItems = newCheckedItems
            
            // ✨ Celebração da IA quando marcar item
            if (isChecked) {
                selectedWeek?.let { weekData ->
                    val totalItems = weekData.items.size
                    val completedCount = weekData.items.count { itemText ->
                        newCheckedItems.contains(WeeklyCheckItem.createId(week, itemText))
                    }
                    
                    // Mostrar celebração em marcos importantes
                    when {
                        completedCount == totalItems -> {
                            celebrationMessage = AICompanion.getChecklistCelebration(completedCount, totalItems)
                            showCelebration = true
                        }
                        completedCount == 1 -> {
                            celebrationMessage = "✅ Primeiro item feito! É assim que se começa! Continue assim, mamãe! 💪"
                            showCelebration = true
                        }
                        completedCount == totalItems / 2 -> {
                            celebrationMessage = AICompanion.getChecklistCelebration(completedCount, totalItems)
                            showCelebration = true
                        }
                    }
                }
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
            
            // ✨ Dica da Lumi no topo
            if (showAITip) {
                AIBubble(
                    message = aiTip,
                    onDismiss = { showAITip = false }
                )
            }
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
                        hasProgress = weeksWithProgress.contains(checklist.week),
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
                    
                    // Barra de progresso da semana
                    item {
                        val totalItems = week.items.size
                        val checkedCount = week.items.count { item ->
                            checkedItems.contains(WeeklyCheckItem.createId(week.week, item))
                        }
                        val progress = if (totalItems > 0) checkedCount.toFloat() / totalItems else 0f
                        
                        ProgressCard(
                            checkedCount = checkedCount,
                            totalItems = totalItems,
                            progress = progress
                        )
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
                        val itemId = WeeklyCheckItem.createId(week.week, item)
                        WeeklyChecklistItem(
                            text = item,
                            isChecked = checkedItems.contains(itemId),
                            onCheckedChange = { checked ->
                                toggleItem(week.week, item, checked)
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
        
        // ✨ Card de celebração da Lumi (overlay)
        if (showCelebration) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { showCelebration = false },
                contentAlignment = Alignment.Center
            ) {
                AICelebrationCard(
                    message = celebrationMessage,
                    onDismiss = { showCelebration = false }
                )
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
    hasProgress: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> Color(0xFFE91E63)
        isCurrent -> Color(0xFFE91E63).copy(alpha = 0.3f)
        hasProgress -> Color(0xFF4CAF50).copy(alpha = 0.15f)
        isPast -> Color(0xFFE0E0E0)
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
        Box {
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
            
            // Indicador de progresso (bolinha verde)
            if (hasProgress && !isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-2).dp, y = 2.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
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
private fun ProgressCard(
    checkedCount: Int,
    totalItems: Int,
    progress: Float
) {
    val isComplete = checkedCount == totalItems && totalItems > 0
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isComplete) Color(0xFFE8F5E9) else Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isComplete) "🎉" else "📊",
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isComplete) "Semana Completa!" else "Seu progresso",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isComplete) Color(0xFF2E7D32) else Color(0xFF333333)
                    )
                }
                
                Text(
                    text = "$checkedCount/$totalItems",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isComplete) Color(0xFF2E7D32) else Color(0xFFE91E63)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Barra de progresso
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (isComplete) Color(0xFF4CAF50) else Color(0xFFE91E63),
                trackColor = Color(0xFFE0E0E0)
            )
            
            if (isComplete) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "✨ Parabéns! Você completou todos os itens desta semana!",
                    fontSize = 12.sp,
                    color = Color(0xFF2E7D32),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else if (checkedCount > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "💪 Continue assim! Faltam ${totalItems - checkedCount} itens.",
                    fontSize = 12.sp,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
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

