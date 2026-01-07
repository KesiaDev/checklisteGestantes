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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gestantes.checklist.data.guides.BabyCareGuide
import com.gestantes.checklist.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareGuideScreen(
    onBackClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Guia de Cuidados") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Tertiary,
                    titleContentColor = OnTertiary,
                    navigationIconContentColor = OnTertiary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { 
                    searchQuery = it
                    selectedCategoryId = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Pesquisar: febre, assadura, cólica...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Pesquisar"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { 
                            searchQuery = ""
                            selectedCategoryId = null
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpar"
                            )
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Tertiary,
                    unfocusedBorderColor = Outline
                )
            )

            // Search suggestions
            if (searchQuery.isEmpty() && selectedCategoryId == null) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val suggestions = listOf("febre", "assadura", "cólica", "sono", "amamentação", "birra")
                    items(suggestions) { suggestion ->
                        SuggestionChip(
                            onClick = { 
                                searchQuery = suggestion
                                focusManager.clearFocus()
                            },
                            label = { Text(suggestion) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = TertiaryContainer
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            when {
                searchQuery.isNotEmpty() -> {
                    // Show search results
                    SearchResultsContent(searchQuery = searchQuery)
                }
                selectedCategoryId != null -> {
                    // Show category detail
                    CategoryDetailContent(
                        categoryId = selectedCategoryId!!,
                        onBackToCategories = { selectedCategoryId = null }
                    )
                }
                else -> {
                    // Show categories
                    CategoriesContent(
                        onCategoryClick = { selectedCategoryId = it }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoriesContent(
    onCategoryClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TertiaryContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "🔍 Pesquise sua dúvida ou escolha uma categoria abaixo",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }

        items(BabyCareGuide.categories) { category ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCategoryClick(category.id) },
                colors = CardDefaults.cardColors(containerColor = SurfaceVariant),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Tertiary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category.icon,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = category.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceVariant
                        )
                        Text(
                            text = "${category.topics.size} tópicos",
                            style = MaterialTheme.typography.labelSmall,
                            color = Tertiary
                        )
                    }
                }
            }
        }

        item {
            DisclaimerCard()
        }
    }
}

@Composable
private fun CategoryDetailContent(
    categoryId: String,
    onBackToCategories: () -> Unit
) {
    val category = BabyCareGuide.categories.find { it.id == categoryId } ?: return
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            TextButton(onClick = onBackToCategories) {
                Text("← Voltar às categorias")
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TertiaryContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category.icon,
                        style = MaterialTheme.typography.displaySmall
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = OnTertiaryContainer
                        )
                        Text(
                            text = category.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnTertiaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        items(category.topics) { topic ->
            TopicCard(topic = topic)
        }

        item {
            DisclaimerCard()
        }
    }
}

@Composable
private fun SearchResultsContent(searchQuery: String) {
    val results = remember(searchQuery) {
        BabyCareGuide.searchTopics(searchQuery)
    }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = if (results.isEmpty()) 
                    "Nenhum resultado para \"$searchQuery\"" 
                else 
                    "${results.size} resultado(s) encontrado(s)",
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (results.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceVariant),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🔍", style = MaterialTheme.typography.displayMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tente outras palavras-chave como:\nfebre, assadura, cólica, sono, birra...",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = OnSurfaceVariant
                        )
                    }
                }
            }
        }

        items(results) { (category, topic) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(4.dp)) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(category.icon)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.labelMedium,
                            color = Tertiary
                        )
                    }
                    TopicCard(topic = topic, showFullContent = true)
                }
            }
        }

        item {
            DisclaimerCard()
        }
    }
}

@Composable
private fun TopicCard(
    topic: BabyCareGuide.CareTopic,
    showFullContent: Boolean = false
) {
    var expanded by remember { mutableStateOf(showFullContent) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = if (showFullContent) Color.Transparent else SurfaceVariant
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = if (showFullContent) CardDefaults.cardElevation(0.dp) else CardDefaults.cardElevation(1.dp)
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
                    text = topic.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                if (!showFullContent) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Recolher" else "Expandir"
                    )
                }
            }
            
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    // Content
                    Text(
                        text = topic.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurface.copy(alpha = 0.9f)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Tips
                    Text(
                        text = "💡 Dicas:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Tertiary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    topic.tips.forEach { tip ->
                        Row(modifier = Modifier.padding(vertical = 2.dp)) {
                            Text("✓", color = Secondary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = tip,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // When to see doctor
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Error.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.Warning,
                                    contentDescription = null,
                                    tint = Error,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Quando procurar o médico:",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Error
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            topic.whenToSeeDoctor.forEach { sign ->
                                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                    Text("•", color = Error)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = sign,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
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
        colors = CardDefaults.cardColors(containerColor = Error.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Outlined.Warning,
                contentDescription = null,
                tint = Error,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "⚠️ IMPORTANTE: Este guia oferece apenas orientações gerais e NÃO substitui " +
                        "avaliação e acompanhamento médico. Em caso de dúvida ou emergência, " +
                        "procure atendimento médico imediatamente.",
                style = MaterialTheme.typography.bodySmall,
                color = OnSurface.copy(alpha = 0.8f)
            )
        }
    }
}

