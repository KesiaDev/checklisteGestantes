package com.gestantes.checklist.ui.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import com.gestantes.checklist.data.guides.ContentSection
import com.gestantes.checklist.data.guides.PregnancyContent
import com.gestantes.checklist.data.guides.PregnancyContentData
import com.gestantes.checklist.data.guides.TrimesterInfo

/**
 * Tela de Conteúdos Informativos por Fase da Gestação
 * 
 * Conteúdo informativo e acolhedor, organizado por trimestre.
 * NÃO é conteúdo médico ou diagnóstico.
 * 
 * NOTA: Esta é uma tela NOVA e ADITIVA - não interfere em fluxos existentes
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PregnancyContentScreen(
    onBackClick: () -> Unit,
    currentWeek: Int = 20
) {
    val trimesters = PregnancyContentData.trimesters
    var selectedTrimester by remember { 
        mutableStateOf(PregnancyContentData.getTrimesterForWeek(currentWeek) ?: trimesters.first())
    }
    var expandedContentId by remember { mutableStateOf<String?>(null) }
    
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
            ContentHeader(onBackClick = onBackClick)
            
            // Seletor de trimestres
            TrimesterSelector(
                trimesters = trimesters,
                selectedTrimester = selectedTrimester,
                currentWeek = currentWeek,
                onTrimesterSelected = { selectedTrimester = it }
            )
            
            // Conteúdo do trimestre selecionado
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Card de informação do trimestre
                item {
                    TrimesterInfoCard(trimester = selectedTrimester)
                }
                
                // Lista de conteúdos
                items(selectedTrimester.contents) { content ->
                    ContentCard(
                        content = content,
                        isExpanded = expandedContentId == content.id,
                        onClick = {
                            expandedContentId = if (expandedContentId == content.id) null else content.id
                        }
                    )
                }
                
                // Aviso importante
                item {
                    DisclaimerCard()
                }
                
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun ContentHeader(onBackClick: () -> Unit) {
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
                    text = "Conteúdos da Gestação 📚",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.width(48.dp))
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Informações acolhedoras para cada fase",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TrimesterSelector(
    trimesters: List<TrimesterInfo>,
    selectedTrimester: TrimesterInfo,
    currentWeek: Int,
    onTrimesterSelected: (TrimesterInfo) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(trimesters) { trimester ->
            val isSelected = selectedTrimester.trimester == trimester.trimester
            val isCurrent = PregnancyContentData.getTrimesterForWeek(currentWeek)?.trimester == trimester.trimester
            
            Surface(
                onClick = { onTrimesterSelected(trimester) },
                shape = RoundedCornerShape(16.dp),
                color = when {
                    isSelected -> Color(0xFFE91E63)
                    isCurrent -> Color(0xFFE91E63).copy(alpha = 0.2f)
                    else -> Color.White
                },
                shadowElevation = if (isSelected) 4.dp else 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = trimester.emoji,
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = trimester.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSelected) Color.White else Color(0xFF333333)
                    )
                    Text(
                        text = trimester.weekRange,
                        fontSize = 10.sp,
                        color = if (isSelected) Color.White.copy(alpha = 0.8f) else Color.Gray
                    )
                    if (isCurrent && !isSelected) {
                        Text(
                            text = "você está aqui",
                            fontSize = 9.sp,
                            color = Color(0xFFE91E63),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TrimesterInfoCard(trimester: TrimesterInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
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
                    text = trimester.emoji,
                    fontSize = 28.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = trimester.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE91E63)
                )
                Text(
                    text = trimester.weekRange,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = trimester.description,
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

@Composable
private fun ContentCard(
    content: PregnancyContent,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isExpanded) Color(0xFFFFF0F5) else Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(if (isExpanded) 4.dp else 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = content.emoji,
                        fontSize = 28.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = content.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF333333)
                        )
                        Text(
                            text = content.description,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
                
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (isExpanded) "Recolher" else "Expandir",
                    tint = Color(0xFFE91E63)
                )
            }
            
            // Conteúdo expandido
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    content.sections.forEach { section ->
                        ContentSectionCard(section = section)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ContentSectionCard(section: ContentSection) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = section.emoji,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = section.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFE91E63)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = section.content,
                fontSize = 14.sp,
                color = Color(0xFF555555),
                lineHeight = 22.sp
            )
            
            if (section.tips.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                
                Surface(
                    color = Color(0xFFFFF3E0),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "💡 Dicas:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE65100)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        section.tips.forEach { tip ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "• ",
                                    fontSize = 12.sp,
                                    color = Color(0xFF795548)
                                )
                                Text(
                                    text = tip,
                                    fontSize = 12.sp,
                                    color = Color(0xFF5D4037)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DisclaimerCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                Icons.Filled.Info,
                contentDescription = null,
                tint = Color(0xFF1976D2),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Este conteúdo é apenas informativo e acolhedor. Não substitui o acompanhamento médico. Sempre consulte seu obstetra para orientações específicas sobre sua gestação.",
                fontSize = 12.sp,
                color = Color(0xFF1565C0),
                lineHeight = 18.sp
            )
        }
    }
}

