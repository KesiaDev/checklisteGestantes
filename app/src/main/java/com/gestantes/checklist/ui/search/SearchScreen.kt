@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.gestantes.checklist.ui.search

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gestantes.checklist.ai.SearchResult
import com.gestantes.checklist.viewmodel.SearchViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    viewModel: SearchViewModel = viewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val searchResponse by viewModel.searchResponse.collectAsState()
    val recentQueries by viewModel.recentQueries.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF8FA),
                        Color(0xFFF3E5F5),
                        Color(0xFFE8EAF6)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header com busca
            SearchHeader(
                searchQuery = searchQuery,
                onSearchChange = { viewModel.setSearchQuery(it) },
                onSearch = {
                    keyboardController?.hide()
                    viewModel.search(searchQuery)
                },
                onBackClick = onBackClick,
                onClear = { viewModel.clearSearch() }
            )
            
            // Content
            if (isSearching) {
                SearchingState()
            } else if (searchResponse != null) {
                SearchResults(
                    response = searchResponse!!,
                    onClear = { viewModel.clearSearch() }
                )
            } else {
                SearchSuggestions(
                    recentQueries = recentQueries,
                    suggestedQueries = viewModel.suggestedQueries,
                    onQueryClick = { query ->
                        viewModel.setSearchQuery(query)
                        viewModel.search(query)
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchHeader(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onSearch: () -> Unit,
    onBackClick: () -> Unit,
    onClear: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color(0xFF9C27B0)
                    )
                }
                
                Text(
                    text = "Busca Inteligente 🔮",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF9C27B0),
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Pergunte qualquer coisa...") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFF9C27B0)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = onClear) {
                            Icon(Icons.Filled.Close, contentDescription = "Limpar")
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF9C27B0),
                    cursorColor = Color(0xFF9C27B0)
                ),
                shape = RoundedCornerShape(16.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = onSearch,
                modifier = Modifier.fillMaxWidth(),
                enabled = searchQuery.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9C27B0)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Buscar")
            }
        }
    }
}

@Composable
private fun SearchingState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = Color(0xFF9C27B0),
            modifier = Modifier.size(48.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Buscando em todos os seus dados...",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun SearchResults(
    response: com.gestantes.checklist.ai.AISearchResponse,
    onClear: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // AI Response message
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFF9C27B0),
                        modifier = Modifier.size(24.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Text(
                        text = response.message,
                        fontSize = 15.sp,
                        color = Color(0xFF333333),
                        lineHeight = 22.sp
                    )
                }
            }
        }
        
        // Results
        if (response.results.isNotEmpty()) {
            item {
                Text(
                    text = "Resultados encontrados:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            items(response.results) { result ->
                SearchResultCard(result = result)
            }
        }
        
        item {
            TextButton(
                onClick = onClear,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Nova busca", color = Color(0xFF9C27B0))
            }
        }
    }
}

@Composable
private fun SearchResultCard(result: SearchResult) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")) }
    
    val (icon, color) = when (result.type) {
        "documento" -> "📄" to Color(0xFF4CAF50)
        "diário" -> "📔" to Color(0xFFE91E63)
        "médico" -> "🏥" to Color(0xFF2196F3)
        "desenvolvimento" -> "⭐" to Color(0xFFFF9800)
        "crescimento" -> "📏" to Color(0xFF00BCD4)
        else -> "📋" to Color.Gray
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
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
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 20.sp)
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = result.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color(0xFF333333)
                )
                
                Text(
                    text = result.description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 2
                )
                
                Text(
                    text = dateFormat.format(Date(result.date)),
                    fontSize = 11.sp,
                    color = color
                )
            }
            
            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
private fun SearchSuggestions(
    recentQueries: List<String>,
    suggestedQueries: List<String>,
    onQueryClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Recent queries
        if (recentQueries.isNotEmpty()) {
            item {
                Text(
                    text = "Buscas recentes",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            items(recentQueries) { query ->
                SuggestionItem(
                    icon = Icons.Filled.History,
                    text = query,
                    onClick = { onQueryClick(query) }
                )
            }
            
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
        
        // Suggestions
        item {
            Text(
                text = "💡 Experimente perguntar:",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF666666),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        items(suggestedQueries) { query ->
            SuggestionItem(
                icon = Icons.Filled.Lightbulb,
                text = query,
                onClick = { onQueryClick(query) },
                iconColor = Color(0xFFFFB300)
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Info,
                        contentDescription = null,
                        tint = Color(0xFFFF9800)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Text(
                        text = "A busca inteligente procura no diário, documentos, histórico médico e de desenvolvimento do bebê.",
                        fontSize = 13.sp,
                        color = Color(0xFF666666)
                    )
                }
            }
        }
    }
}

@Composable
private fun SuggestionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit,
    iconColor: Color = Color.Gray
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = text,
                fontSize = 14.sp,
                color = Color(0xFF333333),
                modifier = Modifier.weight(1f)
            )
            
            Icon(
                Icons.Filled.NorthEast,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

