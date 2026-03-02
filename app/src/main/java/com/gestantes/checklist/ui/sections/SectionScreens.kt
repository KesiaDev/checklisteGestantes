package com.gestantes.checklist.ui.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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

/**
 * Modelo de item para as seções
 */
data class SectionFeatureItem(
    val emoji: String,
    val title: String,
    val description: String,
    val route: String,
    val color: Color,
    val isPremium: Boolean = false,
    val badge: String? = null
)

/**
 * Template base para telas de seção
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionScreenTemplate(
    title: String,
    subtitle: String,
    emoji: String,
    headerColor: Color,
    items: List<SectionFeatureItem>,
    isPremium: Boolean,
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = emoji, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = title, style = MaterialTheme.typography.titleLarge)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = headerColor,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFAFAFA)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header da seção
            item {
                SectionHeader(
                    subtitle = subtitle,
                    color = headerColor
                )
            }
            
            // Lista de itens
            items(items) { item ->
                SectionFeatureCard(
                    item = item,
                    isPremiumUser = isPremium,
                    onClick = { onNavigate(item.route) }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SectionHeader(
    subtitle: String,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Text(
            text = subtitle,
            fontSize = 14.sp,
            color = color,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SectionFeatureCard(
    item: SectionFeatureItem,
    isPremiumUser: Boolean,
    onClick: () -> Unit
) {
    val isLocked = item.isPremium && !isPremiumUser
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.White,
                            item.color.copy(alpha = 0.08f)
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
                // Ícone
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(item.color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = item.emoji, fontSize = 28.sp)
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Textos
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A2E)
                        )
                        
                        // Badge se tiver
                        item.badge?.let { badge ->
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = Color(0xFF10B981),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = badge,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        
                        // Badge Premium
                        if (isLocked) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = Color(0xFFFFB800),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "PRO",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = item.description,
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280),
                        lineHeight = 18.sp
                    )
                }
                
                // Seta ou cadeado
                if (isLocked) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFF3F4F6), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Lock,
                            contentDescription = null,
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else {
                    Icon(
                        Icons.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFFD1D5DB)
                    )
                }
            }
            
            // Linha decorativa lateral
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .align(Alignment.CenterStart)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(item.color, item.color.copy(alpha = 0.5f))
                        ),
                        shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                    )
            )
        }
    }
}

// ==================== SEÇÃO: MINHA GESTAÇÃO ====================

@Composable
fun PregnancySectionScreen(
    currentWeek: Int,
    isPremium: Boolean,
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        SectionFeatureItem(
            emoji = "📅",
            title = "Checklists Semanais",
            description = "O que fazer em cada semana da gestação",
            route = "weekly_checklist",
            color = Color(0xFF9C27B0),
            badge = "Semana $currentWeek"
        ),
        SectionFeatureItem(
            emoji = "🌟",
            title = "Linha do Tempo",
            description = "Acompanhe os marcos da sua gestação",
            route = "timeline",
            color = Color(0xFFFF5722)
        ),
        SectionFeatureItem(
            emoji = "📚",
            title = "Conteúdos por Fase",
            description = "Informações acolhedoras para cada trimestre",
            route = "pregnancy_content",
            color = Color(0xFF009688)
        ),
        SectionFeatureItem(
            emoji = "📸",
            title = "Galeria da Barriga",
            description = "Registre e compare a evolução da sua barriga",
            route = "belly_gallery",
            color = Color(0xFFE91E63)
        ),
        SectionFeatureItem(
            emoji = "💌",
            title = "Cartas para o Bebê",
            description = "Escreva memórias para seu bebê ler quando crescer",
            route = "baby_letter",
            color = Color(0xFFFF8A65)
        )
    )
    
    SectionScreenTemplate(
        title = "Minha Gestação",
        subtitle = "Acompanhe cada momento especial da sua jornada 💕",
        emoji = "🤰",
        headerColor = Color(0xFF9C27B0),
        items = items,
        isPremium = isPremium,
        onBackClick = onBackClick,
        onNavigate = onNavigate
    )
}

// ==================== SEÇÃO: FERRAMENTAS ====================

@Composable
fun ToolsSectionScreen(
    isPremium: Boolean,
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        SectionFeatureItem(
            emoji = "📅",
            title = "Calculadora DPP",
            description = "Calcule a data prevista do parto",
            route = "due_date_calculator",
            color = Color(0xFFE91E63)
        ),
        SectionFeatureItem(
            emoji = "👣",
            title = "Contador de Chutes",
            description = "Monitore os movimentos do bebê",
            route = "kick_counter",
            color = Color(0xFF2196F3)
        ),
        SectionFeatureItem(
            emoji = "🍎",
            title = "Tamanho do Bebê",
            description = "Compare o tamanho com frutas e objetos",
            route = "baby_size",
            color = Color(0xFFFF9800)
        ),
        SectionFeatureItem(
            emoji = "📝",
            title = "Lista de Nomes",
            description = "Organize e avalie nomes para o bebê",
            route = "baby_names",
            color = Color(0xFF9C27B0)
        ),
        SectionFeatureItem(
            emoji = "📋",
            title = "Plano de Parto",
            description = "Crie seu plano de parto personalizado",
            route = "birth_plan",
            color = Color(0xFF4CAF50)
        ),
        SectionFeatureItem(
            emoji = "⏱️",
            title = "Contador de Contrações",
            description = "Monitore suas contrações para o trabalho de parto",
            route = "contraction",
            color = Color(0xFF00BCD4)
        ),
        SectionFeatureItem(
            emoji = "🔔",
            title = "Lembretes",
            description = "Consultas, exames e vacinas",
            route = "reminder",
            color = Color(0xFF5C6BC0)
        )
    )
    
    SectionScreenTemplate(
        title = "Ferramentas",
        subtitle = "Tudo que você precisa para acompanhar sua gestação 🛠️",
        emoji = "🛠️",
        headerColor = Color(0xFF2196F3),
        items = items,
        isPremium = isPremium,
        onBackClick = onBackClick,
        onNavigate = onNavigate
    )
}

// ==================== SEÇÃO: MEU BEBÊ ====================

@Composable
fun BabySectionScreen(
    isPremium: Boolean,
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        SectionFeatureItem(
            emoji = "📔",
            title = "Diário da Mamãe",
            description = "Registre memórias, sentimentos e receba apoio da IA",
            route = "diary",
            color = Color(0xFFE91E63),
            isPremium = true
        ),
        SectionFeatureItem(
            emoji = "📁",
            title = "Documentos do Bebê",
            description = "Certidão, vacinas, exames e receitas organizados",
            route = "documents",
            color = Color(0xFF4CAF50),
            isPremium = true
        ),
        SectionFeatureItem(
            emoji = "🏥",
            title = "Histórico do Bebê",
            description = "Consultas, vacinas, marcos e desenvolvimento",
            route = "history",
            color = Color(0xFF2196F3),
            isPremium = true
        ),
        SectionFeatureItem(
            emoji = "📏",
            title = "Crescimento",
            description = "Peso, altura e gráficos de evolução",
            route = "growth",
            color = Color(0xFF00BCD4),
            isPremium = true
        ),
        SectionFeatureItem(
            emoji = "🔮",
            title = "Busca Inteligente",
            description = "Encontre qualquer informação com a IA",
            route = "search",
            color = Color(0xFF9C27B0),
            isPremium = true
        )
    )
    
    SectionScreenTemplate(
        title = "Meu Bebê",
        subtitle = "Organize tudo sobre seu bebê em um só lugar 👶",
        emoji = "👶",
        headerColor = Color(0xFFE91E63),
        items = items,
        isPremium = isPremium,
        onBackClick = onBackClick,
        onNavigate = onNavigate
    )
}

// ==================== SEÇÃO: CHECKLISTS ====================

@Composable
fun ChecklistsSectionScreen(
    isPremium: Boolean,
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        SectionFeatureItem(
            emoji = "👜",
            title = "Mala da Maternidade",
            description = "Itens essenciais para o grande dia",
            route = "checklist/MATERNIDADE",
            color = Color(0xFFE91E63)
        ),
        SectionFeatureItem(
            emoji = "💊",
            title = "Pré-natal",
            description = "Acompanhe seus cuidados e consultas",
            route = "checklist/PRE_NATAL",
            color = Color(0xFF4CAF50)
        ),
        SectionFeatureItem(
            emoji = "🍼",
            title = "Pós-parto",
            description = "Prepare-se para a nova fase",
            route = "checklist/POS_PARTO",
            color = Color(0xFF2196F3)
        ),
        SectionFeatureItem(
            emoji = "🎁",
            title = "Lista de Chá de Bebê",
            description = "Organize e compartilhe sua lista de presentes",
            route = "baby_shower",
            color = Color(0xFFFF9800)
        )
    )
    
    SectionScreenTemplate(
        title = "Checklists",
        subtitle = "Organize todas as suas tarefas da gestação ✅",
        emoji = "✅",
        headerColor = Color(0xFF4CAF50),
        items = items,
        isPremium = isPremium,
        onBackClick = onBackClick,
        onNavigate = onNavigate
    )
}

// ==================== SEÇÃO: GUIAS ====================

@Composable
fun GuidesSectionScreen(
    isPremium: Boolean,
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        SectionFeatureItem(
            emoji = "🌙",
            title = "Guia de Sono",
            description = "Dicas para uma rotina de sono saudável",
            route = "sleep_guide",
            color = Color(0xFF5C6BC0),
            isPremium = true
        ),
        SectionFeatureItem(
            emoji = "👶",
            title = "Fases do Desenvolvimento",
            description = "Do nascimento até 4 anos",
            route = "development",
            color = Color(0xFFFF9800),
            isPremium = true
        ),
        SectionFeatureItem(
            emoji = "❓",
            title = "Dúvidas e Cuidados",
            description = "Febre, pele, alimentação e mais",
            route = "care_guide",
            color = Color(0xFF00BCD4),
            isPremium = true
        ),
        SectionFeatureItem(
            emoji = "📚",
            title = "Conteúdos por Fase",
            description = "Informações para cada trimestre",
            route = "pregnancy_content",
            color = Color(0xFF009688)
        )
    )
    
    SectionScreenTemplate(
        title = "Guias",
        subtitle = "Dicas e informações para cada fase 📚",
        emoji = "📚",
        headerColor = Color(0xFFFF9800),
        items = items,
        isPremium = isPremium,
        onBackClick = onBackClick,
        onNavigate = onNavigate
    )
}

// ==================== SEÇÃO: ESPECIAL ====================

@Composable
fun SpecialSectionScreen(
    isPremium: Boolean,
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        SectionFeatureItem(
            emoji = "💜",
            title = "Gestação do Coração",
            description = "Apoio completo para famílias em processo de adoção",
            route = "adoption",
            color = Color(0xFF9C27B0)
        ),
        SectionFeatureItem(
            emoji = "🎁",
            title = "Chá de Bebê",
            description = "Organize e compartilhe sua lista de presentes",
            route = "baby_shower",
            color = Color(0xFF4CAF50)
        )
    )
    
    SectionScreenTemplate(
        title = "Especial",
        subtitle = "Caminhos especiais para cada jornada 💜",
        emoji = "💜",
        headerColor = Color(0xFF9C27B0),
        items = items,
        isPremium = isPremium,
        onBackClick = onBackClick,
        onNavigate = onNavigate
    )
}
