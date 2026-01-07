@file:OptIn(ExperimentalMaterial3Api::class)

package com.gestantes.checklist.ui.guides

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gestantes.checklist.data.guides.DevelopmentData
import com.gestantes.checklist.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevelopmentScreen(
    onBackClick: () -> Unit
) {
    var selectedPhaseIndex by remember { mutableIntStateOf(0) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fases do Desenvolvimento") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Secondary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Age selector
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SecondaryContainer)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(DevelopmentData.phases.size) { index ->
                    val phase = DevelopmentData.phases[index]
                    FilterChip(
                        selected = selectedPhaseIndex == index,
                        onClick = { selectedPhaseIndex = index },
                        label = { 
                            Text(
                                "${phase.icon} ${phase.ageRange.split(" ")[0]}",
                                style = MaterialTheme.typography.labelMedium
                            ) 
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Secondary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // Selected phase content
            val selectedPhase = DevelopmentData.phases[selectedPhaseIndex]
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SecondaryContainer),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = selectedPhase.icon,
                                style = MaterialTheme.typography.displayMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = selectedPhase.ageRange,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = OnSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = selectedPhase.summary,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = OnSecondaryContainer.copy(alpha = 0.9f)
                            )
                        }
                    }
                }

                // Development areas
                item {
                    DevelopmentCategory(
                        title = "🏃 Desenvolvimento Motor",
                        items = selectedPhase.physical,
                        backgroundColor = CardMaternidade.copy(alpha = 0.5f)
                    )
                }

                item {
                    DevelopmentCategory(
                        title = "🧠 Desenvolvimento Cognitivo",
                        items = selectedPhase.cognitive,
                        backgroundColor = CardPrenatal.copy(alpha = 0.5f)
                    )
                }

                item {
                    DevelopmentCategory(
                        title = "👥 Desenvolvimento Social",
                        items = selectedPhase.social,
                        backgroundColor = CardPosparto.copy(alpha = 0.5f)
                    )
                }

                item {
                    DevelopmentCategory(
                        title = "🗣️ Linguagem",
                        items = selectedPhase.language,
                        backgroundColor = PrimaryContainer.copy(alpha = 0.7f)
                    )
                }

                // Tips
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Secondary.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "💡 Dicas para essa fase",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            selectedPhase.tips.forEach { tip ->
                                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                    Text("✓", color = Secondary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = tip,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }

                // Alert signs
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Error.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.Warning,
                                    contentDescription = null,
                                    tint = Error
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Sinais para conversar com o pediatra",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Error
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Text(
                                text = "Cada criança tem seu ritmo! Estes são apenas sinais que merecem atenção:",
                                style = MaterialTheme.typography.bodySmall,
                                color = OnSurface.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            selectedPhase.alertSigns.forEach { sign ->
                                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                    Text("⚠️", style = MaterialTheme.typography.bodySmall)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = sign,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }

                // Disclaimer
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SurfaceVariant),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "⚠️ Cada criança é única e se desenvolve no seu próprio ritmo. " +
                                    "Este guia é apenas uma referência geral baseada em marcos típicos. " +
                                    "Sempre consulte o pediatra para avaliação individualizada.",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun DevelopmentCategory(
    title: String,
    items: List<String>,
    backgroundColor: Color
) {
    var expanded by remember { mutableStateOf(true) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Recolher" else "Expandir"
                )
            }
            
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    items.forEach { item ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 6.dp)
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(OnSurface.copy(alpha = 0.5f))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

