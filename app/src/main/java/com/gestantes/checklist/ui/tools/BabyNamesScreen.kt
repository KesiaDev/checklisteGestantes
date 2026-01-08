package com.gestantes.checklist.ui.tools

import androidx.compose.animation.*
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestantes.checklist.data.database.ChecklistDatabase
import com.gestantes.checklist.data.entity.BabyName
import kotlinx.coroutines.launch

/**
 * Tela de Lista de Nomes para o Bebê
 * 
 * Permite salvar, organizar e avaliar nomes para o bebê
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyNamesScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    
    val database = remember { ChecklistDatabase.getDatabase(context) }
    val babyNameDao = database.babyNameDao()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("all") } // all, male, female, favorites
    var searchQuery by remember { mutableStateOf("") }
    var editingName by remember { mutableStateOf<BabyName?>(null) }
    
    val allNames by babyNameDao.getAllNames().collectAsState(initial = emptyList())
    
    // Filtrar nomes
    val filteredNames = remember(allNames, selectedFilter, searchQuery) {
        allNames.filter { name ->
            val matchesFilter = when (selectedFilter) {
                "male" -> name.gender == "male"
                "female" -> name.gender == "female"
                "favorites" -> name.isFavorite
                else -> true
            }
            val matchesSearch = searchQuery.isEmpty() || 
                name.name.contains(searchQuery, ignoreCase = true) ||
                name.meaning.contains(searchQuery, ignoreCase = true)
            matchesFilter && matchesSearch
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF3E5F5),
                        Color(0xFFE1BEE7),
                        Color(0xFFCE93D8)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF9C27B0),
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
                        
                        Text(
                            text = "Lista de Nomes 👶",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        
                        IconButton(onClick = { showAddDialog = true }) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "Adicionar nome",
                                tint = Color.White
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Barra de pesquisa
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Buscar nomes...", color = Color.White.copy(alpha = 0.7f)) },
                        leadingIcon = {
                            Icon(Icons.Filled.Search, contentDescription = null, tint = Color.White)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Filled.Clear, contentDescription = "Limpar", tint = Color.White)
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
                    )
                }
            }
            
            // Filtros
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                item {
                    FilterChip(
                        label = "Todos",
                        emoji = "📋",
                        isSelected = selectedFilter == "all",
                        onClick = { selectedFilter = "all" }
                    )
                }
                item {
                    FilterChip(
                        label = "Meninos",
                        emoji = "👦",
                        isSelected = selectedFilter == "male",
                        onClick = { selectedFilter = "male" }
                    )
                }
                item {
                    FilterChip(
                        label = "Meninas",
                        emoji = "👧",
                        isSelected = selectedFilter == "female",
                        onClick = { selectedFilter = "female" }
                    )
                }
                item {
                    FilterChip(
                        label = "Favoritos",
                        emoji = "❤️",
                        isSelected = selectedFilter == "favorites",
                        onClick = { selectedFilter = "favorites" }
                    )
                }
            }
            
            // Lista de nomes ou estado vazio
            if (filteredNames.isEmpty()) {
                EmptyState(
                    hasAnyNames = allNames.isNotEmpty(),
                    onAddClick = { showAddDialog = true }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "${filteredNames.size} nome${if (filteredNames.size != 1) "s" else ""} encontrado${if (filteredNames.size != 1) "s" else ""}",
                            fontSize = 14.sp,
                            color = Color(0xFF666666)
                        )
                    }
                    
                    items(filteredNames, key = { it.id }) { name ->
                        NameCard(
                            babyName = name,
                            onFavoriteClick = {
                                scope.launch {
                                    babyNameDao.updateFavorite(name.id, !name.isFavorite)
                                }
                            },
                            onRatingChange = { rating ->
                                scope.launch {
                                    babyNameDao.updateRating(name.id, rating)
                                }
                            },
                            onEditClick = { editingName = name },
                            onDeleteClick = {
                                scope.launch {
                                    babyNameDao.delete(name)
                                }
                            }
                        )
                    }
                    
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
        
        // FAB
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF9C27B0)
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Adicionar nome",
                tint = Color.White
            )
        }
        
        // Dialog de adicionar/editar nome
        if (showAddDialog || editingName != null) {
            AddEditNameDialog(
                existingName = editingName,
                onDismiss = { 
                    showAddDialog = false
                    editingName = null
                },
                onSave = { babyName ->
                    scope.launch {
                        if (editingName != null) {
                            babyNameDao.update(babyName)
                        } else {
                            babyNameDao.insert(babyName)
                        }
                        showAddDialog = false
                        editingName = null
                    }
                }
            )
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color(0xFF9C27B0) else Color.White
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                color = if (isSelected) Color.White else Color(0xFF666666)
            )
        }
    }
}

@Composable
private fun EmptyState(
    hasAnyNames: Boolean,
    onAddClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = if (hasAnyNames) "🔍" else "👶", fontSize = 60.sp)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = if (hasAnyNames) "Nenhum nome encontrado" else "Sua lista está vazia",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )
        
        Text(
            text = if (hasAnyNames) 
                "Tente ajustar os filtros ou buscar por outro termo" 
            else 
                "Comece a adicionar nomes para o seu bebê!",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        if (!hasAnyNames) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9C27B0)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Adicionar nome")
            }
        }
    }
}

@Composable
private fun NameCard(
    babyName: BabyName,
    onFavoriteClick: () -> Unit,
    onRatingChange: (Int) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    
    val genderColor = when (babyName.gender) {
        "male" -> Color(0xFF2196F3)
        "female" -> Color(0xFFE91E63)
        else -> Color(0xFF9C27B0)
    }
    
    val genderEmoji = when (babyName.gender) {
        "male" -> "👦"
        "female" -> "👧"
        else -> "👶"
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Emoji de gênero
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(genderColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = genderEmoji, fontSize = 24.sp)
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Nome e significado
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = babyName.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    if (babyName.meaning.isNotEmpty()) {
                        Text(
                            text = babyName.meaning,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                    if (babyName.origin.isNotEmpty()) {
                        Text(
                            text = "Origem: ${babyName.origin}",
                            fontSize = 12.sp,
                            color = genderColor
                        )
                    }
                }
                
                // Favorito
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        if (babyName.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (babyName.isFavorite) Color(0xFFE91E63) else Color.Gray
                    )
                }
                
                // Menu
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Mais opções", tint = Color.Gray)
                    }
                    
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Editar") },
                            leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                            onClick = {
                                showMenu = false
                                onEditClick()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Excluir", color = Color.Red) },
                            leadingIcon = { Icon(Icons.Filled.Delete, contentDescription = null, tint = Color.Red) },
                            onClick = {
                                showMenu = false
                                onDeleteClick()
                            }
                        )
                    }
                }
            }
            
            // Rating
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                (1..5).forEach { star ->
                    IconButton(
                        onClick = { onRatingChange(if (babyName.rating == star) 0 else star) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            if (star <= babyName.rating) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Avaliação $star",
                            tint = if (star <= babyName.rating) Color(0xFFFFB300) else Color.LightGray,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
            
            // Notas
            if (babyName.notes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(text = "📝", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = babyName.notes,
                            fontSize = 13.sp,
                            color = Color(0xFF666666)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditNameDialog(
    existingName: BabyName?,
    onDismiss: () -> Unit,
    onSave: (BabyName) -> Unit
) {
    var name by remember { mutableStateOf(existingName?.name ?: "") }
    var meaning by remember { mutableStateOf(existingName?.meaning ?: "") }
    var origin by remember { mutableStateOf(existingName?.origin ?: "") }
    var gender by remember { mutableStateOf(existingName?.gender ?: "neutral") }
    var notes by remember { mutableStateOf(existingName?.notes ?: "") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (existingName != null) "Editar Nome" else "Adicionar Nome",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nome *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                
                item {
                    OutlinedTextField(
                        value = meaning,
                        onValueChange = { meaning = it },
                        label = { Text("Significado") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                
                item {
                    OutlinedTextField(
                        value = origin,
                        onValueChange = { origin = it },
                        label = { Text("Origem") },
                        placeholder = { Text("Ex: Hebraico, Grego, Latim...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                
                item {
                    Text("Gênero", fontWeight = FontWeight.Medium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        GenderOption(
                            emoji = "👦",
                            label = "Menino",
                            isSelected = gender == "male",
                            onClick = { gender = "male" }
                        )
                        GenderOption(
                            emoji = "👧",
                            label = "Menina",
                            isSelected = gender == "female",
                            onClick = { gender = "female" }
                        )
                        GenderOption(
                            emoji = "👶",
                            label = "Neutro",
                            isSelected = gender == "neutral",
                            onClick = { gender = "neutral" }
                        )
                    }
                }
                
                item {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notas") },
                        placeholder = { Text("Por que gosta desse nome?") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 3
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val babyName = BabyName(
                            id = existingName?.id ?: 0,
                            name = name.trim(),
                            meaning = meaning.trim(),
                            origin = origin.trim(),
                            gender = gender,
                            notes = notes.trim(),
                            isFavorite = existingName?.isFavorite ?: false,
                            rating = existingName?.rating ?: 0,
                            addedAt = existingName?.addedAt ?: System.currentTimeMillis()
                        )
                        onSave(babyName)
                    }
                },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun GenderOption(
    emoji: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color(0xFF9C27B0).copy(alpha = 0.15f) else Color.Transparent,
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF9C27B0))
        } else null
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 24.sp)
            Text(
                text = label,
                fontSize = 12.sp,
                color = if (isSelected) Color(0xFF9C27B0) else Color.Gray
            )
        }
    }
}
