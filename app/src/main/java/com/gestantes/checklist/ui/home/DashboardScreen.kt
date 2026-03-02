package com.gestantes.checklist.ui.home

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
import com.gestantes.checklist.ai.AICompanion
import com.gestantes.checklist.data.preferences.UserData
import kotlinx.coroutines.launch

/**
 * Dashboard principal - Tela inicial simplificada e organizada
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    userData: UserData,
    isPremium: Boolean,
    onOpenDrawer: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val currentWeek = remember(userData.currentWeek) { 
        userData.currentWeek.takeIf { it > 0 } ?: 20 
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Checklist para Gestantes",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE91E63),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { onNavigate("notification_settings") }) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Notificações",
                            tint = Color.White
                        )
                    }
                    if (!isPremium) {
                        IconButton(onClick = { onNavigate("subscription") }) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Premium",
                                tint = Color(0xFFFFD700)
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFAFAFA)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card de boas-vindas com progresso
            item(key = "welcome") {
                WelcomeProgressCard(
                    momName = userData.momName,
                    currentWeek = currentWeek
                )
            }
            
            // Dica da Lumi
            item(key = "lumi_tip") {
                LumiTipCard(currentWeek = currentWeek)
            }
            
            // Acesso rápido - Grid de atalhos principais
            item(key = "quick_access") {
                QuickAccessSection(
                    onNavigate = onNavigate,
                    isPremium = isPremium
                )
            }
            
            // Seções do app
            item(key = "sections") {
                AppSectionsGrid(
                    onNavigate = onNavigate,
                    isPremium = isPremium
                )
            }
            
            // Disclaimer
            item(key = "disclaimer") {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Este aplicativo não substitui acompanhamento médico.",
                    fontSize = 12.sp,
                    color = Color(0xFF9CA3AF),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * Card de boas-vindas com progresso da gestação
 */
@Composable
private fun WelcomeProgressCard(
    momName: String,
    currentWeek: Int
) {
    val progressPercent = ((currentWeek.toFloat() / 40f) * 100).toInt()
    val weeksRemaining = 40 - currentWeek
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFE91E63).copy(alpha = 0.1f),
                            Color(0xFF9C27B0).copy(alpha = 0.05f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (momName.isNotBlank()) "Olá, $momName! 👋" else "Olá, mamãe! 👋",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A2E)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Semana $currentWeek de 40",
                            fontSize = 14.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                    
                    // Círculo de progresso
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE91E63).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$progressPercent%",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE91E63)
                            )
                            Text(
                                text = "completo",
                                fontSize = 10.sp,
                                color = Color(0xFF9CA3AF)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Barra de progresso
                LinearProgressIndicator(
                    progress = { currentWeek / 40f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFFE91E63),
                    trackColor = Color(0xFFE91E63).copy(alpha = 0.2f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = if (weeksRemaining > 0) "Faltam $weeksRemaining semanas para conhecer seu bebê! 💕" 
                           else "Seu bebê pode chegar a qualquer momento! 🎉",
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}

/**
 * Card de dica da Lumi simplificado
 */
@Composable
private fun LumiTipCard(currentWeek: Int) {
    val tip = remember(currentWeek) { AICompanion.getTip(AICompanion.Context.HOME, currentWeek) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5))
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
                    .background(Color(0xFF9C27B0)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "✨", fontSize = 22.sp)
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Lumi diz:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF9C27B0)
                )
                Text(
                    text = tip,
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

/**
 * Seção de acesso rápido - atalhos horizontais
 */
@Composable
private fun QuickAccessSection(
    onNavigate: (String) -> Unit,
    isPremium: Boolean
) {
    Column {
        Text(
            text = "Acesso Rápido",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A2E),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                listOf(
                    QuickItem("📅", "Semana", "weekly_checklist", Color(0xFF9C27B0)),
                    QuickItem("⏱️", "Contrações", "contraction", Color(0xFF2196F3)),
                    QuickItem("👣", "Chutes", "kick_counter", Color(0xFFFF9800)),
                    QuickItem("📸", "Fotos", "belly_gallery", Color(0xFFE91E63)),
                    QuickItem("📔", "Diário", "diary", Color(0xFF4CAF50), isPremium = true),
                    QuickItem("🔔", "Lembretes", "reminder", Color(0xFF5C6BC0))
                )
            ) { item ->
                QuickAccessCard(
                    item = item,
                    isPremiumUser = isPremium,
                    onClick = { onNavigate(item.route) }
                )
            }
        }
    }
}

private data class QuickItem(
    val emoji: String,
    val title: String,
    val route: String,
    val color: Color,
    val isPremium: Boolean = false
)

@Composable
private fun QuickAccessCard(
    item: QuickItem,
    isPremiumUser: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(item.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item.emoji, fontSize = 22.sp)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = item.title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF374151),
                textAlign = TextAlign.Center
            )
            
            if (item.isPremium && !isPremiumUser) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "⭐",
                    fontSize = 10.sp
                )
            }
        }
    }
}

/**
 * Grid de seções principais do app
 */
@Composable
private fun AppSectionsGrid(
    onNavigate: (String) -> Unit,
    isPremium: Boolean
) {
    Column {
        Text(
            text = "Explorar",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A2E),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        val sections = listOf(
            SectionItem("🤰", "Minha Gestação", "Acompanhe sua jornada", "section_pregnancy", Color(0xFF9C27B0)),
            SectionItem("🛠️", "Ferramentas", "Calculadoras e contadores", "section_tools", Color(0xFF2196F3)),
            SectionItem("👶", "Meu Bebê", "Diário, docs e crescimento", "section_baby", Color(0xFFE91E63)),
            SectionItem("✅", "Checklists", "Organize suas tarefas", "section_checklists", Color(0xFF4CAF50)),
            SectionItem("📚", "Guias", "Dicas e informações", "section_guides", Color(0xFFFF9800)),
            SectionItem("💜", "Especial", "Adoção e chá de bebê", "section_special", Color(0xFF7B1FA2))
        )
        
        // Grid 2x3
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            sections.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { section ->
                        SectionCard(
                            section = section,
                            onClick = { onNavigate(section.route) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Preencher espaço vazio se número ímpar
                    if (row.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

private data class SectionItem(
    val emoji: String,
    val title: String,
    val subtitle: String,
    val route: String,
    val color: Color
)

@Composable
private fun SectionCard(
    section: SectionItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.White,
                            section.color.copy(alpha = 0.08f)
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(section.color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = section.emoji, fontSize = 24.sp)
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = section.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A2E)
                    )
                    Text(
                        text = section.subtitle,
                        fontSize = 11.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }
            
            // Linha decorativa
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .align(Alignment.CenterStart)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(section.color, section.color.copy(alpha = 0.5f))
                        ),
                        shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                    )
            )
        }
    }
}
