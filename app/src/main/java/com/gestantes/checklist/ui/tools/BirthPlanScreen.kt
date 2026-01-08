package com.gestantes.checklist.ui.tools

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestantes.checklist.data.database.ChecklistDatabase
import com.gestantes.checklist.data.entity.BirthPlanCategory
import com.gestantes.checklist.data.entity.BirthPlanItem
import com.gestantes.checklist.data.guides.BirthPlanData
import kotlinx.coroutines.launch

/**
 * Tela do Plano de Parto
 * 
 * Permite criar e personalizar o plano de parto
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthPlanScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    val database = remember { ChecklistDatabase.getDatabase(context) }
    val birthPlanDao = database.birthPlanDao()
    
    var selectedCategory by remember { mutableStateOf(BirthPlanCategory.AMBIENTE) }
    var showPreviewDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<BirthPlanItem?>(null) }
    
    val allItems by birthPlanDao.getAllItems().collectAsState(initial = emptyList())
    val selectedItems by birthPlanDao.getSelectedItems().collectAsState(initial = emptyList())
    
    // Inicializar dados se estiver vazio
    LaunchedEffect(Unit) {
        if (birthPlanDao.getCount() == 0) {
            birthPlanDao.insertAll(BirthPlanData.getDefaultItems())
        }
    }
    
    // Filtrar por categoria
    val categoryItems = remember(allItems, selectedCategory) {
        allItems.filter { it.category == selectedCategory.name }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE8F5E9),
                        Color(0xFFC8E6C9),
                        Color(0xFFA5D6A7)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF4CAF50),
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
                            text = "Plano de Parto 📋",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        
                        IconButton(onClick = { showPreviewDialog = true }) {
                            Icon(
                                Icons.Filled.Visibility,
                                contentDescription = "Visualizar",
                                tint = Color.White
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Progresso
                    val progress = if (allItems.isNotEmpty()) {
                        selectedItems.size.toFloat() / allItems.size
                    } else 0f
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Color.White,
                            trackColor = Color.White.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${selectedItems.size}/${allItems.size}",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
            
            // Tabs de categorias
            ScrollableTabRow(
                selectedTabIndex = BirthPlanCategory.values().indexOf(selectedCategory),
                containerColor = Color.White,
                contentColor = Color(0xFF4CAF50),
                edgePadding = 0.dp
            ) {
                BirthPlanCategory.values().forEach { category ->
                    Tab(
                        selected = category == selectedCategory,
                        onClick = { selectedCategory = category },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = category.emoji, fontSize = 16.sp)
                                Text(
                                    text = category.displayName,
                                    fontSize = 13.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    )
                }
            }
            
            // Lista de itens
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    InfoCard(category = selectedCategory)
                }
                
                items(categoryItems, key = { it.id }) { item ->
                    BirthPlanItemCard(
                        item = item,
                        onPreferenceChange = { preference, note, isSelected ->
                            scope.launch {
                                birthPlanDao.updatePreference(item.id, preference, note, isSelected)
                            }
                        },
                        onEditClick = { editingItem = item }
                    )
                }
                
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
        
        // FAB para compartilhar
        FloatingActionButton(
            onClick = { 
                val text = generateBirthPlanText(selectedItems)
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "Meu Plano de Parto")
                    putExtra(Intent.EXTRA_TEXT, text)
                }
                context.startActivity(Intent.createChooser(intent, "Compartilhar Plano de Parto"))
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF4CAF50)
        ) {
            Icon(
                Icons.Filled.Share,
                contentDescription = "Compartilhar",
                tint = Color.White
            )
        }
        
        // Dialog de preview
        if (showPreviewDialog) {
            PreviewDialog(
                items = selectedItems,
                onDismiss = { showPreviewDialog = false },
                onShare = {
                    val text = generateBirthPlanText(selectedItems)
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "Meu Plano de Parto")
                        putExtra(Intent.EXTRA_TEXT, text)
                    }
                    context.startActivity(Intent.createChooser(intent, "Compartilhar Plano de Parto"))
                }
            )
        }
        
        // Dialog de edição
        if (editingItem != null) {
            EditItemDialog(
                item = editingItem!!,
                onDismiss = { editingItem = null },
                onSave = { preference, note ->
                    scope.launch {
                        birthPlanDao.updatePreference(
                            editingItem!!.id, 
                            preference, 
                            note, 
                            preference.isNotEmpty()
                        )
                        editingItem = null
                    }
                }
            )
        }
    }
}

@Composable
private fun InfoCard(category: BirthPlanCategory) {
    val description = when (category) {
        BirthPlanCategory.AMBIENTE -> "Configure como você gostaria que fosse o ambiente do parto."
        BirthPlanCategory.TRABALHO_PARTO -> "Suas preferências durante o trabalho de parto."
        BirthPlanCategory.PARTO -> "Escolhas para o momento do nascimento."
        BirthPlanCategory.BEBE -> "Cuidados com o bebê logo após o nascimento."
        BirthPlanCategory.POS_PARTO -> "Preferências para o período pós-parto."
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(text = category.emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = category.displayName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun BirthPlanItemCard(
    item: BirthPlanItem,
    onPreferenceChange: (String, String, Boolean) -> Unit,
    onEditClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    val preferenceColor = when (item.preference) {
        "sim" -> Color(0xFF4CAF50)
        "nao" -> Color(0xFFE91E63)
        "talvez" -> Color(0xFFFF9800)
        else -> Color.Gray
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isSelected) Color.White else Color(0xFFFAFAFA)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Indicador de preferência
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(preferenceColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    when (item.preference) {
                        "sim" -> Icon(Icons.Filled.Check, contentDescription = null, tint = preferenceColor)
                        "nao" -> Icon(Icons.Filled.Close, contentDescription = null, tint = preferenceColor)
                        "talvez" -> Icon(Icons.Filled.HelpOutline, contentDescription = null, tint = preferenceColor)
                        else -> Icon(Icons.Outlined.Circle, contentDescription = null, tint = Color.LightGray)
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF333333)
                    )
                    if (item.description.isNotEmpty()) {
                        Text(
                            text = item.description,
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
                
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = "Expandir",
                        tint = Color.Gray
                    )
                }
            }
            
            // Opções expandidas
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    // Botões de preferência
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        PreferenceButton(
                            text = "Sim",
                            emoji = "✅",
                            isSelected = item.preference == "sim",
                            color = Color(0xFF4CAF50),
                            onClick = { onPreferenceChange("sim", item.customNote, true) }
                        )
                        PreferenceButton(
                            text = "Talvez",
                            emoji = "🤔",
                            isSelected = item.preference == "talvez",
                            color = Color(0xFFFF9800),
                            onClick = { onPreferenceChange("talvez", item.customNote, true) }
                        )
                        PreferenceButton(
                            text = "Não",
                            emoji = "❌",
                            isSelected = item.preference == "nao",
                            color = Color(0xFFE91E63),
                            onClick = { onPreferenceChange("nao", item.customNote, true) }
                        )
                    }
                    
                    // Nota personalizada
                    if (item.isSelected && item.customNote.isNotEmpty()) {
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
                                    text = item.customNote,
                                    fontSize = 13.sp,
                                    color = Color(0xFF666666)
                                )
                            }
                        }
                    }
                    
                    // Botão de editar nota
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        onClick = onEditClick,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Adicionar nota", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun PreferenceButton(
    text: String,
    emoji: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) color.copy(alpha = 0.15f) else Color.Transparent,
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, color)
        } else {
            androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                fontSize = 13.sp,
                color = if (isSelected) color else Color.Gray,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditItemDialog(
    item: BirthPlanItem,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var preference by remember { mutableStateOf(item.preference) }
    var note by remember { mutableStateOf(item.customNote) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = item.title,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = item.description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Text(
                    text = "Sua preferência:",
                    fontWeight = FontWeight.Medium
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PreferenceButton(
                        text = "Sim",
                        emoji = "✅",
                        isSelected = preference == "sim",
                        color = Color(0xFF4CAF50),
                        onClick = { preference = "sim" }
                    )
                    PreferenceButton(
                        text = "Talvez",
                        emoji = "🤔",
                        isSelected = preference == "talvez",
                        color = Color(0xFFFF9800),
                        onClick = { preference = "talvez" }
                    )
                    PreferenceButton(
                        text = "Não",
                        emoji = "❌",
                        isSelected = preference == "nao",
                        color = Color(0xFFE91E63),
                        onClick = { preference = "nao" }
                    )
                }
                
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Observações") },
                    placeholder = { Text("Adicione detalhes específicos...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(preference, note) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
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
private fun PreviewDialog(
    items: List<BirthPlanItem>,
    onDismiss: () -> Unit,
    onShare: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "📋", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Meu Plano de Parto",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            if (items.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "📝", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Nenhum item selecionado ainda",
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                    Text(
                        text = "Marque suas preferências para criar seu plano",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BirthPlanCategory.values().forEach { category ->
                        val categoryItems = items.filter { it.category == category.name }
                        if (categoryItems.isNotEmpty()) {
                            item {
                                Text(
                                    text = "${category.emoji} ${category.displayName}",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4CAF50),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                            items(categoryItems) { planItem ->
                                PreviewItem(item = planItem)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (items.isNotEmpty()) {
                Button(
                    onClick = onShare,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Icon(Icons.Filled.Share, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Compartilhar")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar")
            }
        }
    )
}

@Composable
private fun PreviewItem(item: BirthPlanItem) {
    val preferenceEmoji = when (item.preference) {
        "sim" -> "✅"
        "nao" -> "❌"
        "talvez" -> "🤔"
        else -> "⚪"
    }
    
    Row(
        modifier = Modifier.padding(start = 8.dp, bottom = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = preferenceEmoji, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = item.title,
                fontSize = 14.sp,
                color = Color(0xFF333333)
            )
            if (item.customNote.isNotEmpty()) {
                Text(
                    text = "\"${item.customNote}\"",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}

private fun generateBirthPlanText(items: List<BirthPlanItem>): String {
    val sb = StringBuilder()
    sb.appendLine("📋 MEU PLANO DE PARTO")
    sb.appendLine("=" .repeat(30))
    sb.appendLine()
    
    BirthPlanCategory.values().forEach { category ->
        val categoryItems = items.filter { it.category == category.name }
        if (categoryItems.isNotEmpty()) {
            sb.appendLine("${category.emoji} ${category.displayName.uppercase()}")
            sb.appendLine("-".repeat(20))
            
            categoryItems.forEach { item ->
                val preferenceSymbol = when (item.preference) {
                    "sim" -> "✅"
                    "nao" -> "❌"
                    "talvez" -> "🤔"
                    else -> "⚪"
                }
                sb.appendLine("$preferenceSymbol ${item.title}")
                if (item.customNote.isNotEmpty()) {
                    sb.appendLine("   → ${item.customNote}")
                }
            }
            sb.appendLine()
        }
    }
    
    sb.appendLine("=" .repeat(30))
    sb.appendLine("Criado com ❤️ pelo app Checklist Gestantes")
    
    return sb.toString()
}
