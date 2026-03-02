package com.gestantes.checklist.ui.components

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Item do menu de navegação
 */
data class DrawerMenuItem(
    val id: String,
    val title: String,
    val emoji: String,
    val route: String,
    val isPremium: Boolean = false
)

/**
 * Seção do menu
 */
data class DrawerSection(
    val title: String,
    val items: List<DrawerMenuItem>
)

/**
 * Definição das seções do menu
 */
object DrawerSections {
    val sections = listOf(
        DrawerSection(
            title = "",
            items = listOf(
                DrawerMenuItem("home", "Início", "🏠", "home")
            )
        ),
        DrawerSection(
            title = "🤰 Minha Gestação",
            items = listOf(
                DrawerMenuItem("weekly", "Checklists Semanais", "📅", "section_pregnancy"),
                DrawerMenuItem("timeline", "Linha do Tempo", "🌟", "timeline"),
                DrawerMenuItem("content", "Conteúdos por Fase", "📚", "pregnancy_content"),
                DrawerMenuItem("belly", "Galeria da Barriga", "📸", "belly_gallery"),
                DrawerMenuItem("letters", "Cartas para o Bebê", "💌", "baby_letter")
            )
        ),
        DrawerSection(
            title = "🛠️ Ferramentas",
            items = listOf(
                DrawerMenuItem("tools", "Todas as Ferramentas", "🧰", "section_tools"),
                DrawerMenuItem("dpp", "Calculadora DPP", "📅", "due_date_calculator"),
                DrawerMenuItem("kicks", "Contador de Chutes", "👣", "kick_counter"),
                DrawerMenuItem("size", "Tamanho do Bebê", "🍎", "baby_size"),
                DrawerMenuItem("names", "Lista de Nomes", "📝", "baby_names"),
                DrawerMenuItem("birthplan", "Plano de Parto", "📋", "birth_plan"),
                DrawerMenuItem("contraction", "Contador de Contrações", "⏱️", "contraction"),
                DrawerMenuItem("reminder", "Lembretes", "🔔", "reminder")
            )
        ),
        DrawerSection(
            title = "👶 Meu Bebê",
            items = listOf(
                DrawerMenuItem("baby", "Painel do Bebê", "👶", "section_baby"),
                DrawerMenuItem("diary", "Diário da Mamãe", "📔", "diary", isPremium = true),
                DrawerMenuItem("docs", "Documentos", "📁", "documents", isPremium = true),
                DrawerMenuItem("history", "Histórico", "🏥", "history", isPremium = true),
                DrawerMenuItem("growth", "Crescimento", "📏", "growth", isPremium = true)
            )
        ),
        DrawerSection(
            title = "✅ Checklists",
            items = listOf(
                DrawerMenuItem("checklists", "Todos os Checklists", "✅", "section_checklists"),
                DrawerMenuItem("maternidade", "Mala da Maternidade", "👜", "checklist/MATERNIDADE"),
                DrawerMenuItem("prenatal", "Pré-natal", "💊", "checklist/PRE_NATAL"),
                DrawerMenuItem("posparto", "Pós-parto", "🍼", "checklist/POS_PARTO")
            )
        ),
        DrawerSection(
            title = "📚 Guias",
            items = listOf(
                DrawerMenuItem("guides", "Todos os Guias", "📚", "section_guides"),
                DrawerMenuItem("sleep", "Guia de Sono", "🌙", "sleep_guide", isPremium = true),
                DrawerMenuItem("dev", "Desenvolvimento", "👶", "development", isPremium = true),
                DrawerMenuItem("care", "Dúvidas e Cuidados", "❓", "care_guide", isPremium = true)
            )
        ),
        DrawerSection(
            title = "💜 Especial",
            items = listOf(
                DrawerMenuItem("adoption", "Gestação do Coração", "💜", "adoption"),
                DrawerMenuItem("shower", "Chá de Bebê", "🎁", "baby_shower")
            )
        ),
        DrawerSection(
            title = "",
            items = listOf(
                DrawerMenuItem("settings", "Configurações", "⚙️", "settings"),
                DrawerMenuItem("notifications", "Notificações", "🔔", "notification_settings"),
                DrawerMenuItem("premium", "Premium", "⭐", "subscription")
            )
        )
    )
}

/**
 * Drawer/Menu Lateral do App
 */
@Composable
fun AppDrawer(
    currentRoute: String,
    momName: String,
    currentWeek: Int,
    isPremium: Boolean,
    onNavigate: (String) -> Unit,
    onClose: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp),
        drawerContainerColor = Color.White
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header do Drawer
            item {
                DrawerHeader(
                    momName = momName,
                    currentWeek = currentWeek,
                    isPremium = isPremium
                )
            }
            
            // Seções do menu
            DrawerSections.sections.forEach { section ->
                if (section.title.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = section.title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9CA3AF),
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                        )
                    }
                }
                
                items(section.items) { item ->
                    DrawerItem(
                        item = item,
                        isSelected = currentRoute == item.route,
                        isPremium = isPremium,
                        onClick = {
                            onNavigate(item.route)
                            onClose()
                        }
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

/**
 * Header do Drawer com informações da usuária
 */
@Composable
private fun DrawerHeader(
    momName: String,
    currentWeek: Int,
    isPremium: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE91E63),
                        Color(0xFFAD1457)
                    )
                )
            )
            .padding(24.dp)
            .statusBarsPadding()
    ) {
        Column {
            // Avatar
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🤰", fontSize = 32.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Nome
            Text(
                text = if (momName.isNotBlank()) "Olá, $momName!" else "Olá, mamãe!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            // Semana da gestação
            if (currentWeek > 0) {
                Text(
                    text = "Semana $currentWeek da gestação",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
            
            // Badge Premium
            if (isPremium) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = Color(0xFFFFD700).copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "⭐", fontSize = 12.sp)
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
    }
}

/**
 * Item individual do menu
 */
@Composable
private fun DrawerItem(
    item: DrawerMenuItem,
    isSelected: Boolean,
    isPremium: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        Color(0xFFE91E63).copy(alpha = 0.1f)
    } else {
        Color.Transparent
    }
    
    val textColor = if (isSelected) {
        Color(0xFFE91E63)
    } else {
        Color(0xFF374151)
    }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.emoji,
                fontSize = 20.sp
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = item.title,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = textColor,
                modifier = Modifier.weight(1f)
            )
            
            // Badge Premium se necessário
            if (item.isPremium && !isPremium) {
                Surface(
                    color = Color(0xFFFFB800),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "PRO",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            
            // Indicador de seleção
            if (isSelected) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE91E63))
                )
            }
        }
    }
}
