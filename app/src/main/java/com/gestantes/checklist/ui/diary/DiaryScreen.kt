@file:OptIn(ExperimentalMaterial3Api::class)

package com.gestantes.checklist.ui.diary

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gestantes.checklist.data.entity.DiaryEntry
import com.gestantes.checklist.data.entity.Emotion
import com.gestantes.checklist.viewmodel.DiaryViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DiaryScreen(
    onBackClick: () -> Unit,
    viewModel: DiaryViewModel = viewModel()
) {
    val entries by viewModel.filteredEntries.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedEntryForEdit by remember { mutableStateOf<DiaryEntry?>(null) }
    var showDetailSheet by remember { mutableStateOf(false) }
    var detailEntry by remember { mutableStateOf<DiaryEntry?>(null) }
    
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
            DiaryHeader(
                onBackClick = onBackClick,
                searchQuery = searchQuery,
                onSearchChange = { viewModel.setSearchQuery(it) }
            )
            
            // Content
            if (entries.isEmpty() && searchQuery.isBlank()) {
                EmptyDiaryState(onAddClick = { showAddDialog = true })
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(entries, key = { it.id }) { entry ->
                        DiaryEntryCard(
                            entry = entry,
                            onClick = {
                                detailEntry = entry
                                showDetailSheet = true
                            },
                            onEdit = {
                                selectedEntryForEdit = entry
                                showAddDialog = true
                            },
                            onDelete = { viewModel.deleteEntry(entry) }
                        )
                    }
                    
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
        
        // FAB
        FloatingActionButton(
            onClick = { 
                selectedEntryForEdit = null
                showAddDialog = true 
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = Color(0xFFE91E63),
            contentColor = Color.White
        ) {
            Icon(Icons.Filled.Edit, contentDescription = "Novo registro")
        }
    }
    
    // Dialog para adicionar/editar
    if (showAddDialog) {
        AddDiaryEntryDialog(
            existingEntry = selectedEntryForEdit,
            onDismiss = { 
                showAddDialog = false
                selectedEntryForEdit = null
            },
            onSave = { title, content, emotion ->
                viewModel.saveEntry(title, content, emotion, selectedEntryForEdit)
                showAddDialog = false
                selectedEntryForEdit = null
            }
        )
    }
    
    // Bottom Sheet com detalhes e resposta da IA
    if (showDetailSheet && detailEntry != null) {
        DiaryDetailSheet(
            entry = detailEntry!!,
            viewModel = viewModel,
            onDismiss = { 
                showDetailSheet = false
                detailEntry = null
            }
        )
    }
}

@Composable
private fun DiaryHeader(
    onBackClick: () -> Unit,
    searchQuery: String,
    onSearchChange: (String) -> Unit
) {
    var showSearch by remember { mutableStateOf(false) }
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
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
                        tint = Color(0xFFE91E63)
                    )
                }
                
                Text(
                    text = "Diário da Mamãe 📔",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE91E63)
                )
                
                IconButton(onClick = { showSearch = !showSearch }) {
                    Icon(
                        if (showSearch) Icons.Filled.Close else Icons.Filled.Search,
                        contentDescription = "Buscar",
                        tint = Color(0xFFE91E63)
                    )
                }
            }
            
            AnimatedVisibility(visible = showSearch) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    placeholder = { Text("Buscar no diário...") },
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = null)
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE91E63),
                        cursorColor = Color(0xFFE91E63)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyDiaryState(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📔",
            fontSize = 64.sp
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Seu diário está vazio",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Comece a registrar suas memórias,\nsentimentos e reflexões da maternidade",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onAddClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE91E63)
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Icon(Icons.Filled.Edit, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Criar primeira entrada")
        }
    }
}

@Composable
private fun DiaryEntryCard(
    entry: DiaryEntry,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy • HH:mm", Locale("pt", "BR")) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Emoji da emoção
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(entry.emotion.color).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = entry.emotion.emoji,
                            fontSize = 22.sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = entry.title.ifEmpty { entry.emotion.displayName },
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = Color(0xFF333333)
                        )
                        Text(
                            text = dateFormat.format(Date(entry.createdAt)),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
                
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = "Opções",
                            tint = Color.Gray
                        )
                    }
                    
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Editar") },
                            onClick = {
                                showMenu = false
                                onEdit()
                            },
                            leadingIcon = {
                                Icon(Icons.Filled.Edit, contentDescription = null)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Excluir", color = Color.Red) },
                            onClick = {
                                showMenu = false
                                onDelete()
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = null,
                                    tint = Color.Red
                                )
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = entry.content,
                fontSize = 14.sp,
                color = Color(0xFF666666),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            
            // Indicador de resposta da IA
            if (entry.aiResponse != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            Color(0xFFE91E63).copy(alpha = 0.1f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFFE91E63),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Mensagem de apoio disponível",
                        fontSize = 11.sp,
                        color = Color(0xFFE91E63)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddDiaryEntryDialog(
    existingEntry: DiaryEntry?,
    onDismiss: () -> Unit,
    onSave: (String, String, Emotion) -> Unit
) {
    var title by remember { mutableStateOf(existingEntry?.title ?: "") }
    var content by remember { mutableStateOf(existingEntry?.content ?: "") }
    var selectedEmotion by remember { mutableStateOf(existingEntry?.emotion ?: Emotion.CALM) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = if (existingEntry != null) "Editar entrada" else "Nova entrada ✨",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE91E63)
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Seletor de emoção
                Text(
                    text = "Como você está se sentindo?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF666666)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(Emotion.entries) { emotion ->
                        EmotionChip(
                            emotion = emotion,
                            isSelected = selectedEmotion == emotion,
                            onClick = { selectedEmotion = emotion }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Título (opcional)
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Título (opcional)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE91E63),
                        cursorColor = Color(0xFFE91E63)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Conteúdo
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    label = { Text("O que você quer compartilhar hoje?") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE91E63),
                        cursorColor = Color(0xFFE91E63)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = Color.Gray)
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = { onSave(title, content, selectedEmotion) },
                        enabled = content.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE91E63)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Salvar")
                    }
                }
            }
        }
    }
}

@Composable
private fun EmotionChip(
    emotion: Emotion,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color(emotion.color) else Color(emotion.color).copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = emotion.emoji,
                fontSize = 16.sp
            )
            if (isSelected) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = emotion.displayName,
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiaryDetailSheet(
    entry: DiaryEntry,
    viewModel: DiaryViewModel,
    onDismiss: () -> Unit
) {
    val aiResponse by viewModel.aiResponse.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val dateFormat = remember { SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy 'às' HH:mm", Locale("pt", "BR")) }
    
    LaunchedEffect(entry) {
        viewModel.selectEntry(entry)
        if (entry.aiResponse == null) {
            viewModel.analyzeEntry(entry)
        }
    }
    
    ModalBottomSheet(
        onDismissRequest = {
            viewModel.clearAIResponse()
            onDismiss()
        },
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Header com emoção
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(entry.emotion.color).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = entry.emotion.emoji,
                        fontSize = 28.sp
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        text = entry.title.ifEmpty { "Meu diário" },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    Text(
                        text = dateFormat.format(Date(entry.createdAt)),
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Conteúdo do diário
            Text(
                text = entry.content,
                fontSize = 16.sp,
                color = Color(0xFF444444),
                lineHeight = 24.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Resposta da IA
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF0F5)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFFE91E63),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Mensagem de apoio",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE91E63)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    if (isAnalyzing) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color(0xFFE91E63),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Preparando uma mensagem para você...",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    } else {
                        Text(
                            text = aiResponse?.message ?: entry.aiResponse ?: "Clique para receber uma mensagem de apoio",
                            fontSize = 14.sp,
                            color = Color(0xFF555555),
                            lineHeight = 22.sp
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}



