package com.gestantes.checklist.ui.adoption

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestantes.checklist.data.guides.AdoptionData
import com.gestantes.checklist.data.guides.AdoptionPhase
import com.gestantes.checklist.data.guides.AdoptionResource

// Cores do tema de adoção (tons de roxo/lilás - representando amor universal)
val AdoptionPrimary = Color(0xFF7B1FA2)
val AdoptionSecondary = Color(0xFFE1BEE7)
val AdoptionAccent = Color(0xFFFF4081)
val AdoptionBackground = Color(0xFFFCE4EC)

/**
 * Tela de Apoio à Adoção
 * 
 * ADITIVO - Nova tela para apoiar famílias em processo de adoção
 * A "gestação do coração" merece tanto cuidado quanto a gestação física!
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdoptionScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var selectedPhase by remember { mutableStateOf<AdoptionPhase?>(null) }
    var showAITip by remember { mutableStateOf(true) }
    var checkedItems by remember { mutableStateOf<Set<String>>(emptySet()) }
    
    val aiMessage = remember { AdoptionData.getRandomSupportMessage() }
    
    LaunchedEffect(Unit) {
        selectedPhase = AdoptionData.adoptionPhases.firstOrNull()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFCE4EC),
                        Color(0xFFF3E5F5),
                        Color(0xFFE1BEE7).copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AdoptionPrimary,
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
                        
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Gestação do Coração 💜",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Apoio à Adoção",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                }
            }
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Mensagem de boas-vindas da Lumi
                if (showAITip) {
                    item {
                        AdoptionWelcomeCard(
                            message = aiMessage,
                            onDismiss = { showAITip = false }
                        )
                    }
                }
                
                // Seletor de fases
                item {
                    Text(
                        text = "📋 Fases da Adoção",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AdoptionPrimary
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(AdoptionData.adoptionPhases) { phase ->
                            PhaseChip(
                                phase = phase,
                                isSelected = selectedPhase?.id == phase.id,
                                onClick = { selectedPhase = phase }
                            )
                        }
                    }
                }
                
                // Conteúdo da fase selecionada
                selectedPhase?.let { phase ->
                    item {
                        PhaseInfoCard(phase = phase)
                    }
                    
                    // Dicas da fase
                    item {
                        TipsCard(tips = phase.tips)
                    }
                    
                    // Checklist da fase
                    item {
                        Text(
                            text = "✅ O que fazer nesta fase",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AdoptionPrimary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    
                    items(phase.checklist) { item ->
                        val itemId = "adoption-${phase.id}-$item"
                        val isChecked = checkedItems.contains(itemId)
                        
                        AdoptionChecklistItem(
                            text = item,
                            isChecked = isChecked,
                            onCheckedChange = { checked ->
                                checkedItems = if (checked) {
                                    checkedItems + itemId
                                } else {
                                    checkedItems - itemId
                                }
                            }
                        )
                    }
                }
                
                // Curiosidade
                item {
                    FunFactCard()
                }
                
                // Recursos úteis
                item {
                    ResourcesSection()
                }
                
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun AdoptionWelcomeCard(
    message: String,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
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
                            AdoptionSecondary.copy(alpha = 0.3f),
                            AdoptionSecondary.copy(alpha = 0.1f)
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
                    verticalAlignment = Alignment.Top
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(AdoptionPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "💜", fontSize = 26.sp)
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column {
                            Text(
                                text = "Lumi está com você!",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AdoptionPrimary
                            )
                            Text(
                                text = "Sua companheira na jornada ✨",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                    
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Fechar",
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = message,
                    fontSize = 15.sp,
                    color = Color(0xFF333333),
                    lineHeight = 22.sp,
                    fontStyle = FontStyle.Italic
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
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
                        Text(text = "💡", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AdoptionData.getRandomFunFact(),
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

@Composable
private fun PhaseChip(
    phase: AdoptionPhase,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) AdoptionPrimary else Color.White,
        shadowElevation = if (isSelected) 4.dp else 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = phase.emoji,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = phase.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFF333333),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun PhaseInfoCard(phase: AdoptionPhase) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
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
                    .background(AdoptionPrimary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = phase.emoji,
                    fontSize = 28.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Fase ${phase.id}: ${phase.title}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AdoptionPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = phase.description,
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    lineHeight = 20.sp
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
                Text(text = "💡", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Dicas da Lumi para esta fase",
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
                        color = AdoptionPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tip,
                        fontSize = 13.sp,
                        color = Color(0xFF5D4037),
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun AdoptionChecklistItem(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) },
        colors = CardDefaults.cardColors(
            containerColor = if (isChecked) {
                Color(0xFF4CAF50).copy(alpha = 0.15f)
            } else {
                Color.White
            }
        ),
        shape = RoundedCornerShape(12.dp)
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
                    uncheckedColor = AdoptionPrimary
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
private fun FunFactCard() {
    val funFact = remember { AdoptionData.getRandomFunFact() }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8EAF6)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "🌟", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Você sabia?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3F51B5)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = funFact,
                    fontSize = 13.sp,
                    color = Color(0xFF333333),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun ResourcesSection() {
    val context = LocalContext.current
    
    Column {
        Text(
            text = "📚 Recursos Úteis",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = AdoptionPrimary
        )
        
        Text(
            text = "Toque para abrir no navegador",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        AdoptionData.resources.forEach { resource ->
            ResourceCard(
                resource = resource,
                onClick = {
                    if (resource.url.isNotBlank()) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resource.url))
                        context.startActivity(intent)
                    }
                }
            )
        }
    }
}

@Composable
private fun ResourceCard(
    resource: AdoptionResource,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
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
                        when (resource.type) {
                            com.gestantes.checklist.data.guides.ResourceType.WEBSITE -> Color(0xFF2196F3).copy(alpha = 0.15f)
                            com.gestantes.checklist.data.guides.ResourceType.GOVERNMENT -> Color(0xFF4CAF50).copy(alpha = 0.15f)
                            com.gestantes.checklist.data.guides.ResourceType.SUPPORT_GROUP -> Color(0xFFFF9800).copy(alpha = 0.15f)
                            com.gestantes.checklist.data.guides.ResourceType.NGO -> Color(0xFF9C27B0).copy(alpha = 0.15f)
                            else -> AdoptionSecondary.copy(alpha = 0.5f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    when (resource.type) {
                        com.gestantes.checklist.data.guides.ResourceType.WEBSITE -> Icons.Outlined.Language
                        com.gestantes.checklist.data.guides.ResourceType.GOVERNMENT -> Icons.Outlined.AccountBalance
                        com.gestantes.checklist.data.guides.ResourceType.SUPPORT_GROUP -> Icons.Outlined.Groups
                        com.gestantes.checklist.data.guides.ResourceType.NGO -> Icons.Outlined.VolunteerActivism
                        else -> Icons.Outlined.Info
                    },
                    contentDescription = null,
                    tint = when (resource.type) {
                        com.gestantes.checklist.data.guides.ResourceType.WEBSITE -> Color(0xFF2196F3)
                        com.gestantes.checklist.data.guides.ResourceType.GOVERNMENT -> Color(0xFF4CAF50)
                        com.gestantes.checklist.data.guides.ResourceType.SUPPORT_GROUP -> Color(0xFFFF9800)
                        com.gestantes.checklist.data.guides.ResourceType.NGO -> Color(0xFF9C27B0)
                        else -> AdoptionPrimary
                    },
                    modifier = Modifier.size(22.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = resource.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333)
                )
                Text(
                    text = resource.description,
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
                if (resource.url.isNotBlank()) {
                    Text(
                        text = "🔗 Toque para acessar",
                        fontSize = 11.sp,
                        color = Color(0xFF2196F3),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            Icon(
                Icons.Outlined.OpenInNew,
                contentDescription = "Abrir link",
                tint = Color(0xFF2196F3),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
