@file:OptIn(ExperimentalMaterial3Api::class)

package com.gestantes.checklist.ui.letter

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestantes.checklist.data.database.ChecklistDatabase
import com.gestantes.checklist.data.entity.BabyLetter
import com.gestantes.checklist.data.entity.LetterMood
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.layout.statusBarsPadding

@Composable
fun BabyLetterScreen(
    onBackClick: () -> Unit,
    currentWeek: Int = 20
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { ChecklistDatabase.getDatabase(context) }
    val letters by database.babyLetterDao().getAllLetters().collectAsState(initial = emptyList())
    
    var showWriteScreen by remember { mutableStateOf(false) }
    var editingLetter by remember { mutableStateOf<BabyLetter?>(null) }
    var showLetterDetail by remember { mutableStateOf<BabyLetter?>(null) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFDE7),
                        Color(0xFFFFF8E1),
                        Color(0xFFFFECB3)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFFF8A65),
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
                            text = "💌 Cartas para o Bebê",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                    
                    Text(
                        text = "Escreva memórias para seu bebê ler quando crescer",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Stats
            if (letters.isNotEmpty()) {
                LetterStatsCard(letterCount = letters.size)
            }
            
            // Content
            if (letters.isEmpty()) {
                EmptyLettersState(onWriteClick = { showWriteScreen = true })
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(letters, key = { it.id }) { letter ->
                        LetterCard(
                            letter = letter,
                            onClick = { showLetterDetail = letter },
                            onEdit = {
                                editingLetter = letter
                                showWriteScreen = true
                            },
                            onDelete = {
                                scope.launch {
                                    database.babyLetterDao().delete(letter)
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
            onClick = { 
                editingLetter = null
                showWriteScreen = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFFFF8A65)
        ) {
            Icon(
                Icons.Filled.Edit,
                contentDescription = "Escrever carta",
                tint = Color.White
            )
        }
        
        // Write Screen
        if (showWriteScreen) {
            WriteLetterScreen(
                existingLetter = editingLetter,
                currentWeek = currentWeek,
                onSave = { letter ->
                    scope.launch {
                        if (editingLetter != null) {
                            database.babyLetterDao().update(letter)
                        } else {
                            database.babyLetterDao().insert(letter)
                        }
                        showWriteScreen = false
                        editingLetter = null
                    }
                },
                onDismiss = {
                    showWriteScreen = false
                    editingLetter = null
                }
            )
        }
        
        // Letter Detail
        showLetterDetail?.let { letter ->
            LetterDetailDialog(
                letter = letter,
                onDismiss = { showLetterDetail = null }
            )
        }
    }
}

/**
 * Card de estatísticas de cartas MODERNO
 */
@Composable
private fun LetterStatsCard(letterCount: Int) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícone moderno
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFF8A65).copy(alpha = 0.15f),
                                Color(0xFFFFAB91).copy(alpha = 0.25f)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "📚", fontSize = 32.sp)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$letterCount carta${if (letterCount > 1) "s" else ""}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF1A1A2E)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Um tesouro de memórias 💝",
                    fontSize = 14.sp,
                    color = Color(0xFF9CA3AF)
                )
            }
            
            // Indicador visual
            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFF8A65),
                                Color(0xFFFFAB91)
                            )
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "✨",
                    fontSize = 16.sp
                )
            }
        }
    }
}

/**
 * Card de carta MODERNO
 */
@Composable
private fun LetterCard(
    letter: BabyLetter,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Box {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Emoji em caixa moderna
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFFFF8A65).copy(alpha = 0.15f),
                                            Color(0xFFFF8A65).copy(alpha = 0.25f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = letter.mood.emoji,
                                fontSize = 26.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = letter.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF1A1A2E)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = dateFormat.format(Date(letter.createdAt)),
                                fontSize = 12.sp,
                                color = Color(0xFF9CA3AF)
                            )
                        }
                    }
                    
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                Icons.Filled.MoreVert, 
                                contentDescription = "Menu",
                                tint = Color(0xFFD1D5DB)
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Editar") },
                                onClick = { showMenu = false; onEdit() },
                                leadingIcon = { Icon(Icons.Filled.Edit, null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Deletar", color = Color(0xFFEF4444)) },
                                onClick = { showMenu = false; onDelete() },
                                leadingIcon = { Icon(Icons.Filled.Delete, null, tint = Color(0xFFEF4444)) }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(14.dp))
                
                Text(
                    text = letter.content,
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 20.sp
                )
                
                if (letter.week != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFFF8A65).copy(alpha = 0.15f),
                                        Color(0xFFFFAB91).copy(alpha = 0.15f)
                                    )
                                ),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "📅 Semana ${letter.week}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFFF7043)
                        )
                    }
                }
            }
            
            // Linha decorativa lateral
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(50.dp)
                    .align(Alignment.CenterStart)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFF8A65),
                                Color(0xFFFFAB91)
                            )
                        ),
                        shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                    )
            )
        }
    }
}

@Composable
private fun EmptyLettersState(onWriteClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "💌", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Nenhuma carta ainda",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Escreva cartas para seu bebê ler quando crescer. Conte sobre seus sentimentos, sonhos e a espera por ele(a).",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onWriteClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A65))
        ) {
            Icon(Icons.Filled.Edit, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Escrever primeira carta")
        }
    }
}

@Composable
private fun WriteLetterScreen(
    existingLetter: BabyLetter?,
    currentWeek: Int,
    onSave: (BabyLetter) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(existingLetter?.title ?: "") }
    var content by remember { mutableStateOf(existingLetter?.content ?: "") }
    var selectedMood by remember { mutableStateOf(existingLetter?.mood ?: LetterMood.LOVE) }
    var includeWeek by remember { mutableStateOf(existingLetter?.week != null) }
    var selectedWeek by remember { mutableStateOf(existingLetter?.week ?: currentWeek) }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFFFDE7)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFFF8A65)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, "Fechar", tint = Color.White)
                    }
                    
                    Text(
                        text = if (existingLetter != null) "✏️ Editar Carta" else "✍️ Nova Carta",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    
                    TextButton(
                        onClick = {
                            val letter = BabyLetter(
                                id = existingLetter?.id ?: 0,
                                title = title.ifBlank { "Carta para você" },
                                content = content,
                                mood = selectedMood,
                                week = if (includeWeek) selectedWeek else null,
                                createdAt = existingLetter?.createdAt ?: System.currentTimeMillis(),
                                updatedAt = System.currentTimeMillis()
                            )
                            onSave(letter)
                        },
                        enabled = content.isNotBlank()
                    ) {
                        Text(
                            "Salvar",
                            color = if (content.isNotBlank()) Color.White else Color.White.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Mood selector
                item {
                    Text(
                        text = "Como você está se sentindo?",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(LetterMood.entries) { mood ->
                            FilterChip(
                                selected = selectedMood == mood,
                                onClick = { selectedMood = mood },
                                label = { 
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(mood.emoji)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(mood.displayName, fontSize = 12.sp)
                                    }
                                }
                            )
                        }
                    }
                }
                
                // Title
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Título da carta") },
                        placeholder = { Text("Ex: Para você, meu amor...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF8A65),
                            focusedLabelColor = Color(0xFFFF8A65)
                        )
                    )
                }
                
                // Content
                item {
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Sua carta") },
                        placeholder = { 
                            Text(
                                "Querido(a) bebê,\n\n" +
                                "Hoje estou escrevendo essa carta para você...\n\n" +
                                "Com amor,\nMamãe 💕"
                            ) 
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF8A65),
                            focusedLabelColor = Color(0xFFFF8A65)
                        )
                    )
                }
                
                // Week selector
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = includeWeek,
                            onCheckedChange = { includeWeek = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFFFF8A65)
                            )
                        )
                        Text(
                            text = "Incluir semana da gestação",
                            fontSize = 14.sp
                        )
                    }
                    
                    AnimatedVisibility(visible = includeWeek) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            items((4..40).toList()) { week ->
                                FilterChip(
                                    selected = selectedWeek == week,
                                    onClick = { selectedWeek = week },
                                    label = { Text("$week") }
                                )
                            }
                        }
                    }
                }
                
                // Tips
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFE0B2)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "💡 Dicas para sua carta",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFFE65100)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "• Conte como você está se sentindo\n" +
                                       "• Descreva seus sonhos para o bebê\n" +
                                       "• Fale sobre acontecimentos especiais\n" +
                                       "• Compartilhe conselhos de vida\n" +
                                       "• Expresse seu amor 💕",
                                fontSize = 12.sp,
                                color = Color(0xFF795548)
                            )
                        }
                    }
                }
                
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun LetterDetailDialog(
    letter: BabyLetter,
    onDismiss: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("pt", "BR")) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "${letter.mood.emoji} ${letter.title}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = dateFormat.format(Date(letter.createdAt)),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                if (letter.week != null) {
                    Text(
                        text = "Semana ${letter.week} da gestação",
                        fontSize = 12.sp,
                        color = Color(0xFFFF8A65)
                    )
                }
            }
        },
        text = {
            LazyColumn {
                item {
                    Text(
                        text = letter.content,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        color = Color(0xFF333333)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar", color = Color(0xFFFF8A65))
            }
        },
        containerColor = Color(0xFFFFFDE7)
    )
}
