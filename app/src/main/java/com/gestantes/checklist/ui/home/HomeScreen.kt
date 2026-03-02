package com.gestantes.checklist.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.BabyChangingStation
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Healing
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.Nightlight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestantes.checklist.data.entity.ChecklistCategory
import com.gestantes.checklist.data.preferences.UserData
import com.gestantes.checklist.data.preferences.UserPreferencesManager
import com.gestantes.checklist.ai.AICompanion
import com.gestantes.checklist.notification.ComfortMessages
import com.gestantes.checklist.ui.components.AIWelcomeBanner
import com.gestantes.checklist.ui.components.AITipCard
import com.gestantes.checklist.ui.theme.OnSurfaceVariant
import com.gestantes.checklist.ui.theme.getCardMaternidade
import com.gestantes.checklist.ui.theme.getCardPosparto
import com.gestantes.checklist.ui.theme.getCardPrenatal
import com.gestantes.checklist.ui.theme.getIconMaternidade
import com.gestantes.checklist.ui.theme.getIconPosparto
import com.gestantes.checklist.ui.theme.getIconPrenatal

// ==================== GRADIENTES CACHEADOS PARA PERFORMANCE ====================
// Evita recriação de objetos Brush a cada recomposição

private val PremiumBadgeGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFFFB800), Color(0xFFFFD54F))
)

private val GreenBadgeGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF10B981), Color(0xFF34D399))
)

private val AdoptionBackgroundGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7).copy(alpha = 0.3f))
)

private val AdoptionLineGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF9C27B0), Color(0xFFE91E63))
)

private val AdoptionIconGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF9C27B0).copy(alpha = 0.2f), Color(0xFF7B1FA2).copy(alpha = 0.3f))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    isPremium: Boolean = false,
    onCategoryClick: (ChecklistCategory) -> Unit,
    onSleepGuideClick: () -> Unit = {},
    onDevelopmentClick: () -> Unit = {},
    onCareGuideClick: () -> Unit = {},
    onSubscriptionClick: () -> Unit = {},
    // Novos callbacks para o ecossistema do bebê
    onDiaryClick: () -> Unit = {},
    onDocumentsClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onGrowthClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    // NOVOS CALLBACKS - Expansão da Gestação (ADITIVOS)
    onWeeklyChecklistClick: () -> Unit = {},
    onTimelineClick: () -> Unit = {},
    onPregnancyContentClick: () -> Unit = {},
    // NOVOS CALLBACKS - Expansão v2.0
    onBellyGalleryClick: () -> Unit = {},
    onBabyLetterClick: () -> Unit = {},
    onBabyShowerClick: () -> Unit = {},
    onContractionClick: () -> Unit = {},
    onReminderClick: () -> Unit = {},
    // Configurações de notificação
    onNotificationSettingsClick: () -> Unit = {},
    // Apoio à Adoção
    onAdoptionClick: () -> Unit = {},
    // Ferramentas Essenciais
    onDueDateCalculatorClick: () -> Unit = {},
    onKickCounterClick: () -> Unit = {},
    onBabySizeClick: () -> Unit = {},
    onBabyNamesClick: () -> Unit = {},
    onBirthPlanClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val preferencesManager = remember { UserPreferencesManager(context) }
    val userData by preferencesManager.userData.collectAsState(initial = UserData())
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Checklist para Gestantes",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    // Botão de notificações
                    IconButton(onClick = onNotificationSettingsClick) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Notificações",
                            tint = Color.White
                        )
                    }
                    
                    // Botão de configurações
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Configurações",
                            tint = Color.White
                        )
                    }
                    
                    // Botão de busca inteligente
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Busca Inteligente",
                            tint = Color.White
                        )
                    }
                    
                    // Botão Premium
                    if (!isPremium) {
                        IconButton(onClick = onSubscriptionClick) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Assinar Premium",
                                tint = Color(0xFFFFD700)
                            )
                        }
                    } else {
                        Surface(
                            color = Color(0xFFFFD700).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Premium",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        // Cache da semana atual para evitar recálculos
        val currentWeek = remember(userData.currentWeek) { 
            userData.currentWeek.takeIf { it > 0 } ?: 20 
        }
        
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // OTIMIZAÇÃO: Usando keys para melhorar recomposições
            item(key = "welcome") {
                WelcomeCard(userData = userData)
            }
            
            // ✨ Banner da IA Lumi - Companheira da Gestante
            item(key = "ai_banner") {
                AIWelcomeBanner(
                    momName = userData.momName,
                    currentWeek = currentWeek
                )
            }
            
            // Dica contextual da Lumi
            item(key = "ai_tip") {
                val tip = remember(currentWeek) { AICompanion.getTip(AICompanion.Context.HOME, currentWeek) }
                AITipCard(message = tip)
            }
            
            // ============ MINHA GESTAÇÃO (NOVA SEÇÃO - ADITIVA) ============
            item(key = "section_gestacao") {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🤰 Minha Gestação",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            // Acompanhamento semanal da gestação
            item {
                PregnancyFeatureCard(
                    emoji = "📅",
                    title = "Checklists Semanais",
                    description = "O que fazer em cada semana da gestação",
                    color = Color(0xFF9C27B0),
                    onClick = onWeeklyChecklistClick
                )
            }
            
            // Linha do tempo
            item {
                PregnancyFeatureCard(
                    emoji = "🌟",
                    title = "Linha do Tempo",
                    description = "Acompanhe os marcos da sua gestação",
                    color = Color(0xFFFF5722),
                    onClick = onTimelineClick
                )
            }
            
            // Conteúdos por fase
            item {
                PregnancyFeatureCard(
                    emoji = "📚",
                    title = "Conteúdos por Fase",
                    description = "Informações acolhedoras para cada trimestre",
                    color = Color(0xFF009688),
                    onClick = onPregnancyContentClick
                )
            }
            
            // Galeria de fotos da barriga
            item {
                PregnancyFeatureCard(
                    emoji = "📸",
                    title = "Galeria da Barriga",
                    description = "Registre e compare a evolução da sua barriga",
                    color = Color(0xFFE91E63),
                    onClick = onBellyGalleryClick
                )
            }
            
            // Cartas para o bebê
            item {
                PregnancyFeatureCard(
                    emoji = "💌",
                    title = "Cartas para o Bebê",
                    description = "Escreva memórias para seu bebê ler quando crescer",
                    color = Color(0xFFFF8A65),
                    onClick = onBabyLetterClick
                )
            }
            
            // ============ FERRAMENTAS ÚTEIS ============
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🛠️ Ferramentas Úteis",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            // Lista de chá de bebê
            item {
                PregnancyFeatureCard(
                    emoji = "🎁",
                    title = "Lista de Chá de Bebê",
                    description = "Organize e compartilhe sua lista de presentes",
                    color = Color(0xFF4CAF50),
                    onClick = onBabyShowerClick
                )
            }
            
            // Contador de contrações
            item {
                PregnancyFeatureCard(
                    emoji = "⏱️",
                    title = "Contador de Contrações",
                    description = "Monitore suas contrações para o trabalho de parto",
                    color = Color(0xFF2196F3),
                    onClick = onContractionClick
                )
            }
            
            // Lembretes
            item {
                PregnancyFeatureCard(
                    emoji = "🔔",
                    title = "Lembretes",
                    description = "Consultas, exames e vacinas",
                    color = Color(0xFF5C6BC0),
                    onClick = onReminderClick
                )
            }
            
            // ============ FERRAMENTAS ESSENCIAIS ============
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🛠️ Ferramentas Essenciais",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            // Calculadora DPP
            item {
                PregnancyFeatureCard(
                    emoji = "📅",
                    title = "Calculadora DPP",
                    description = "Calcule a data prevista do parto",
                    color = Color(0xFFE91E63),
                    onClick = onDueDateCalculatorClick
                )
            }
            
            // Contador de Chutes
            item {
                PregnancyFeatureCard(
                    emoji = "👣",
                    title = "Contador de Chutes",
                    description = "Monitore os movimentos do bebê",
                    color = Color(0xFF2196F3),
                    onClick = onKickCounterClick
                )
            }
            
            // Tamanho do Bebê
            item {
                PregnancyFeatureCard(
                    emoji = "🍎",
                    title = "Tamanho do Bebê",
                    description = "Compare o tamanho com frutas e objetos",
                    color = Color(0xFFFF9800),
                    onClick = onBabySizeClick
                )
            }
            
            // Lista de Nomes
            item {
                PregnancyFeatureCard(
                    emoji = "📝",
                    title = "Lista de Nomes",
                    description = "Organize e avalie nomes para o bebê",
                    color = Color(0xFF9C27B0),
                    onClick = onBabyNamesClick
                )
            }
            
            // Plano de Parto
            item {
                PregnancyFeatureCard(
                    emoji = "📋",
                    title = "Plano de Parto",
                    description = "Crie seu plano de parto personalizado",
                    color = Color(0xFF4CAF50),
                    onClick = onBirthPlanClick
                )
            }
            
            // ============ CAMINHOS ESPECIAIS ============
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "💜 Caminhos Especiais",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            // Apoio à Adoção
            item {
                AdoptionCard(onClick = onAdoptionClick)
            }
            
            // ============ ECOSSISTEMA DO BEBÊ ============
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "👶 Ecossistema do Bebê",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            // Quick Access Cards
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        listOf(
                            QuickAccessItem("📔", "Diário", Color(0xFFE91E63), onDiaryClick),
                            QuickAccessItem("📁", "Documentos", Color(0xFF4CAF50), onDocumentsClick),
                            QuickAccessItem("🏥", "Histórico", Color(0xFF2196F3), onHistoryClick),
                            QuickAccessItem("📏", "Crescimento", Color(0xFF00BCD4), onGrowthClick),
                            QuickAccessItem("🔮", "Busca IA", Color(0xFF9C27B0), onSearchClick)
                        )
                    ) { item ->
                        QuickAccessCard(
                            emoji = item.emoji,
                            title = item.title,
                            color = item.color,
                            isPremium = isPremium,
                            onClick = item.onClick
                        )
                    }
                }
            }
            
            // Diário da Mamãe
            item {
                BabyEcosystemCard(
                    emoji = "📔",
                    title = "Diário da Mamãe",
                    description = "Registre memórias, sentimentos e receba apoio da IA",
                    color = Color(0xFFE91E63),
                    isPremium = isPremium,
                    onClick = onDiaryClick
                )
            }
            
            // Documentos do Bebê
            item {
                BabyEcosystemCard(
                    emoji = "📁",
                    title = "Documentos do Bebê",
                    description = "Certidão, vacinas, exames e receitas organizados",
                    color = Color(0xFF4CAF50),
                    isPremium = isPremium,
                    onClick = onDocumentsClick
                )
            }
            
            // Histórico Clínico e Pedagógico
            item {
                BabyEcosystemCard(
                    emoji = "🏥",
                    title = "Histórico do Bebê",
                    description = "Consultas, vacinas, marcos e desenvolvimento",
                    color = Color(0xFF2196F3),
                    isPremium = isPremium,
                    onClick = onHistoryClick
                )
            }
            
            // Acompanhamento de Crescimento
            item {
                BabyEcosystemCard(
                    emoji = "📏",
                    title = "Crescimento",
                    description = "Peso, altura e gráficos de evolução",
                    color = Color(0xFF00BCD4),
                    isPremium = isPremium,
                    onClick = onGrowthClick
                )
            }
            
            // ============ CHECKLISTS ============
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "✅ Checklists da Gestação",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                CategoryCard(
                    title = "Mala da Maternidade",
                    description = "Itens essenciais para o grande dia",
                    icon = Icons.Outlined.LocalHospital,
                    backgroundColor = getCardMaternidade(),
                    iconColor = getIconMaternidade(),
                    onClick = { onCategoryClick(ChecklistCategory.MATERNIDADE) }
                )
            }
            
            item {
                CategoryCard(
                    title = "Pré-natal",
                    description = "Acompanhe seus cuidados",
                    icon = Icons.Outlined.Healing,
                    backgroundColor = getCardPrenatal(),
                    iconColor = getIconPrenatal(),
                    onClick = { onCategoryClick(ChecklistCategory.PRE_NATAL) }
                )
            }
            
            item {
                CategoryCard(
                    title = "Pós-parto",
                    description = "Prepare-se para a nova fase",
                    icon = Icons.Outlined.BabyChangingStation,
                    backgroundColor = getCardPosparto(),
                    iconColor = getIconPosparto(),
                    onClick = { onCategoryClick(ChecklistCategory.POS_PARTO) }
                )
            }
            
            // ============ GUIAS ============
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "📚 Guias para Mamães",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                val primary = MaterialTheme.colorScheme.primary
                PremiumCategoryCard(
                    title = "Guia de Sono",
                    description = "Dicas para uma rotina de sono saudável",
                    icon = Icons.Outlined.Nightlight,
                    backgroundColor = primary.copy(alpha = 0.15f),
                    iconColor = primary,
                    isPremium = isPremium,
                    onClick = onSleepGuideClick
                )
            }
            
            item {
                val secondary = MaterialTheme.colorScheme.secondary
                PremiumCategoryCard(
                    title = "Fases do Desenvolvimento",
                    description = "Do nascimento até 4 anos",
                    icon = Icons.Outlined.ChildCare,
                    backgroundColor = secondary.copy(alpha = 0.25f),
                    iconColor = secondary,
                    isPremium = isPremium,
                    onClick = onDevelopmentClick
                )
            }
            
            item {
                val tertiary = MaterialTheme.colorScheme.tertiary
                PremiumCategoryCard(
                    title = "Dúvidas e Cuidados",
                    description = "Febre, pele, alimentação e mais",
                    icon = Icons.Outlined.HelpOutline,
                    backgroundColor = tertiary.copy(alpha = 0.3f),
                    iconColor = tertiary,
                    isPremium = isPremium,
                    onClick = onCareGuideClick
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(8.dp))
                DisclaimerText()
            }
        }
    }
}

private data class QuickAccessItem(
    val emoji: String,
    val title: String,
    val color: Color,
    val onClick: () -> Unit
)

/**
 * Card de acesso rápido MODERNO
 */
@Composable
private fun QuickAccessCard(
    emoji: String,
    title: String,
    color: Color,
    isPremium: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(85.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 6.dp
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
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                color.copy(alpha = 0.1f),
                                color.copy(alpha = 0.2f)
                            )
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = 22.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF374151),
                textAlign = TextAlign.Center
            )
            if (!isPremium) {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(
                            color = Color(0xFFF3F4F6),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = Color(0xFFD1D5DB),
                        modifier = Modifier.size(8.dp)
                    )
                }
            }
        }
    }
}

/**
 * Card MODERNO do Ecossistema do Bebê (Premium)
 * OTIMIZADO: Gradientes cacheados para melhor performance
 */
@Composable
private fun BabyEcosystemCard(
    emoji: String,
    title: String,
    description: String,
    color: Color,
    isPremium: Boolean,
    onClick: () -> Unit
) {
    // Cache dos gradientes baseados na cor
    val backgroundGradient = remember(color) {
        Brush.horizontalGradient(listOf(Color.White, color.copy(alpha = 0.05f)))
    }
    val iconGradient = remember(color) {
        Brush.linearGradient(listOf(color.copy(alpha = 0.15f), color.copy(alpha = 0.25f)))
    }
    val lineGradient = remember(color) {
        Brush.verticalGradient(listOf(color, color.copy(alpha = 0.5f)))
    }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 8.dp,
        tonalElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundGradient)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(brush = iconGradient, shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji, fontSize = 28.sp)
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A2E)
                        )
                        if (!isPremium) {
                            Spacer(modifier = Modifier.width(8.dp))
                            // Badge Premium com gradiente cacheado
                            Box(
                                modifier = Modifier
                                    .background(brush = PremiumBadgeGradient, shape = RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "PRO",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280),
                        lineHeight = 18.sp
                    )
                }
                
                if (!isPremium) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(color = Color(0xFFF3F4F6), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Conteúdo Premium",
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            
            // Linha decorativa lateral
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .align(Alignment.CenterStart)
                    .background(brush = lineGradient, shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
            )
        }
    }
}

@Composable
private fun WelcomeCard(userData: UserData) {
    val momName = userData.momName.ifBlank { "mamãe" }
    val babies = userData.babies
    val companionName = userData.companionName
    
    // Gera a saudação personalizada
    val greeting = when {
        babies.isEmpty() -> "Olá, $momName! ✨"
        babies.size == 1 -> "Olá, $momName! ✨\nMamãe do(a) ${babies[0].name} 💕"
        else -> {
            val babyNames = babies.map { it.name }
            val lastBaby = babyNames.last()
            val otherBabies = babyNames.dropLast(1).joinToString(", ")
            "Olá, $momName! ✨\nMamãe de $otherBabies e $lastBaby 💕"
        }
    }
    
    // Mensagem inclusiva para acompanhante (NOVO - ADITIVO)
    val supportMessage = if (companionName.isNotBlank()) {
        "Você e $companionName juntos nessa jornada! 💕"
    } else {
        "Organize sua jornada com carinho."
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = greeting,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = supportMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Card de mensagem diária EXPANDIDO
 * ADITIVO - Agora inclui menção ao acompanhante quando existir
 */
@Composable
private fun DailyComfortCard(
    momName: String,
    companionName: String = "" // EXPANSÃO: nome do acompanhante
) {
    // Mensagem do dia - consistente durante todo o dia
    val dailyMessage = remember { ComfortMessages.getDailyMessage() }
    val displayName = momName.ifBlank { "mamãe" }
    val secondary = MaterialTheme.colorScheme.secondary
    val primary = MaterialTheme.colorScheme.primary
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = secondary.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            secondary.copy(alpha = 0.2f),
                            secondary.copy(alpha = 0.4f)
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(primary.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Favorite,
                            contentDescription = null,
                            tint = primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Mensagem do Dia",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = dailyMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // EXPANSÃO: Mensagem inclusiva com acompanhante
                val supportMessage = if (companionName.isNotBlank()) {
                    "Você e $companionName estão construindo algo lindo juntos! 💕"
                } else {
                    "$displayName, você e sua rede de apoio estão juntos nessa jornada! 💕"
                }
                
                Text(
                    text = supportMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceVariant.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * Card MODERNO para categorias de Checklist
 */
@Composable
private fun CategoryCard(
    title: String,
    description: String,
    icon: ImageVector,
    backgroundColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 8.dp,
        tonalElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.White,
                            iconColor.copy(alpha = 0.08f)
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    iconColor.copy(alpha = 0.15f),
                                    iconColor.copy(alpha = 0.25f)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A2E)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280),
                        lineHeight = 18.sp
                    )
                }
                
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFFD1D5DB),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            // Linha decorativa lateral
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .align(Alignment.CenterStart)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                iconColor,
                                iconColor.copy(alpha = 0.5f)
                            )
                        ),
                        shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                    )
            )
        }
    }
}

/**
 * Card MODERNO para Guias Premium
 */
@Composable
private fun PremiumCategoryCard(
    title: String,
    description: String,
    icon: ImageVector,
    backgroundColor: Color,
    iconColor: Color,
    isPremium: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 8.dp,
        tonalElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.White,
                            iconColor.copy(alpha = 0.08f)
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    iconColor.copy(alpha = 0.15f),
                                    iconColor.copy(alpha = 0.25f)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A2E)
                        )
                        if (!isPremium) {
                            Spacer(modifier = Modifier.width(8.dp))
                            // Badge Premium dourado moderno
                            Box(
                                modifier = Modifier
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                Color(0xFFFFB800),
                                                Color(0xFFFFD54F)
                                            )
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "PRO",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280),
                        lineHeight = 18.sp
                    )
                }
                
                if (!isPremium) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = Color(0xFFF3F4F6),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Conteúdo Premium",
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFFD1D5DB),
                        modifier = Modifier.size(24.dp)
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
                            colors = listOf(
                                iconColor,
                                iconColor.copy(alpha = 0.5f)
                            )
                        ),
                        shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                    )
            )
        }
    }
}

@Composable
private fun DisclaimerText() {
    Text(
        text = "Este aplicativo não substitui acompanhamento médico. Seu objetivo é apenas auxiliar na organização da rotina da gestante e no cuidado do bebê.",
        style = MaterialTheme.typography.bodySmall,
        color = OnSurfaceVariant.copy(alpha = 0.7f),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}

// ============ NOVOS COMPONENTES - Expansão da Gestação (ADITIVOS) ============

/**
 * Card MODERNO para as novas funcionalidades da gestação
 * OTIMIZADO: Gradientes cacheados para melhor performance
 */
@Composable
private fun PregnancyFeatureCard(
    emoji: String,
    title: String,
    description: String,
    color: Color,
    onClick: () -> Unit
) {
    // Cache dos gradientes baseados na cor
    val backgroundGradient = remember(color) {
        Brush.horizontalGradient(listOf(Color.White, color.copy(alpha = 0.05f)))
    }
    val iconGradient = remember(color) {
        Brush.linearGradient(listOf(color.copy(alpha = 0.15f), color.copy(alpha = 0.25f)))
    }
    val lineGradient = remember(color) {
        Brush.verticalGradient(listOf(color, color.copy(alpha = 0.5f)))
    }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 8.dp,
        tonalElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundGradient)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(brush = iconGradient, shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji, fontSize = 28.sp)
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A2E)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280),
                        lineHeight = 18.sp
                    )
                }
                
                // Badge com gradiente cacheado
                Box(
                    modifier = Modifier
                        .background(brush = GreenBadgeGradient, shape = RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "GRÁTIS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                }
            }
            
            // Linha decorativa lateral
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .align(Alignment.CenterStart)
                    .background(brush = lineGradient, shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
            )
        }
    }
}

/**
 * Card especial para Apoio à Adoção
 * "Gestação do Coração" - para famílias que escolhem adotar
 */
@Composable
private fun AdoptionCard(
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFF3E5F5),
                            Color(0xFFE1BEE7).copy(alpha = 0.3f)
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ícone especial com coração
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF9C27B0).copy(alpha = 0.2f),
                                    Color(0xFF7B1FA2).copy(alpha = 0.3f)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "💜",
                        fontSize = 32.sp
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Gestação do Coração",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7B1FA2)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Apoio completo para famílias em processo de adoção",
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280),
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✨ Checklists • Dicas • Recursos",
                            fontSize = 11.sp,
                            color = Color(0xFF9C27B0),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                Icon(
                    Icons.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF9C27B0),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            // Linha decorativa lateral roxa
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(50.dp)
                    .align(Alignment.CenterStart)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF9C27B0),
                                Color(0xFFE91E63)
                            )
                        ),
                        shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                    )
            )
        }
    }
}
