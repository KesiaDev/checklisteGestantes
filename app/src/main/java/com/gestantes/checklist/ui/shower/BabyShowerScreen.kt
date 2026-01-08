@file:OptIn(ExperimentalMaterial3Api::class)

package com.gestantes.checklist.ui.shower

import android.content.Intent
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestantes.checklist.data.database.ChecklistDatabase
import com.gestantes.checklist.data.entity.BabyShowerItem
import com.gestantes.checklist.data.entity.ItemPriority
import com.gestantes.checklist.data.entity.ShowerCategory
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.statusBarsPadding

@Composable
fun BabyShowerScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { ChecklistDatabase.getDatabase(context) }
    val allItems by database.babyShowerDao().getAllItems().collectAsState(initial = emptyList())
    
    var showAddDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<BabyShowerItem?>(null) }
    var selectedCategory by remember { mutableStateOf<ShowerCategory?>(null) }
    var showReceivedOnly by remember { mutableStateOf(false) }
    
    // Filtrar itens
    val filteredItems = remember(allItems, selectedCategory, showReceivedOnly) {
        allItems.filter { item ->
            (selectedCategory == null || item.category == selectedCategory) &&
            (!showReceivedOnly || item.isReceived)
        }
    }
    
    // Stats
    val totalItems = allItems.size
    val receivedItems = allItems.count { it.isReceived }
    val progress = if (totalItems > 0) receivedItems.toFloat() / totalItems else 0f
    
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
                            Icon(Icons.Filled.ArrowBack, "Voltar", tint = Color.White)
                        }
                        
                        Text(
                            text = "🎁 Lista de Chá de Bebê",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        
                        IconButton(onClick = {
                            // Compartilhar lista
                            val shareText = buildShareText(allItems)
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Lista de Chá de Bebê 🎁")
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(intent, "Compartilhar lista"))
                        }) {
                            Icon(Icons.Filled.Share, "Compartilhar", tint = Color.White)
                        }
                    }
                    
                    Text(
                        text = "Organize os presentes do seu chá de bebê",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Progress Card
            if (allItems.isNotEmpty()) {
                ProgressCard(
                    totalItems = totalItems,
                    receivedItems = receivedItems,
                    progress = progress
                )
            }
            
            // Category Filter
            CategoryFilter(
                selectedCategory = selectedCategory,
                showReceivedOnly = showReceivedOnly,
                onCategorySelect = { selectedCategory = it },
                onToggleReceived = { showReceivedOnly = it },
                itemCounts = allItems.groupingBy { it.category }.eachCount()
            )
            
            // Content
            if (allItems.isEmpty()) {
                EmptyShowerState(
                    onAddClick = { showAddDialog = true },
                    onAddSuggestions = {
                        scope.launch {
                            database.babyShowerDao().insertAll(getDefaultShowerItems())
                        }
                    }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Agrupar por categoria
                    val groupedItems = filteredItems.groupBy { it.category }
                    
                    groupedItems.forEach { (category, items) ->
                        item {
                            Text(
                                text = "${category.emoji} ${category.displayName}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        
                        items(items, key = { it.id }) { item ->
                            ShowerItemCard(
                                item = item,
                                onToggleReceived = {
                                    scope.launch {
                                        database.babyShowerDao().update(
                                            item.copy(isReceived = !item.isReceived)
                                        )
                                    }
                                },
                                onEdit = {
                                    editingItem = item
                                    showAddDialog = true
                                },
                                onDelete = {
                                    scope.launch {
                                        database.babyShowerDao().delete(item)
                                    }
                                },
                                onUpdateGiftedBy = { giftedBy ->
                                    scope.launch {
                                        database.babyShowerDao().update(
                                            item.copy(
                                                giftedBy = giftedBy,
                                                isReceived = true
                                            )
                                        )
                                    }
                                }
                            )
                        }
                    }
                    
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
        
        // FAB
        FloatingActionButton(
            onClick = { 
                editingItem = null
                showAddDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF4CAF50)
        ) {
            Icon(Icons.Filled.Add, "Adicionar item", tint = Color.White)
        }
        
        // Add/Edit Dialog
        if (showAddDialog) {
            AddItemDialog(
                existingItem = editingItem,
                onDismiss = { 
                    showAddDialog = false
                    editingItem = null
                },
                onSave = { item ->
                    scope.launch {
                        if (editingItem != null) {
                            database.babyShowerDao().update(item)
                        } else {
                            database.babyShowerDao().insert(item)
                        }
                        showAddDialog = false
                        editingItem = null
                    }
                }
            )
        }
    }
}

/**
 * Card de progresso MODERNO
 */
@Composable
private fun ProgressCard(
    totalItems: Int,
    receivedItems: Int,
    progress: Float
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "$receivedItems de $totalItems",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color(0xFF1A1A2E)
                    )
                    Text(
                        text = "itens recebidos",
                        fontSize = 14.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
                
                // Porcentagem em círculo
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF10B981),
                                    Color(0xFF34D399)
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Barra de progresso moderna
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0xFFF3F4F6))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(10.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF10B981),
                                    Color(0xFF34D399)
                                )
                            ),
                            shape = RoundedCornerShape(5.dp)
                        )
                )
            }
        }
    }
}

@Composable
private fun CategoryFilter(
    selectedCategory: ShowerCategory?,
    showReceivedOnly: Boolean,
    onCategorySelect: (ShowerCategory?) -> Unit,
    onToggleReceived: (Boolean) -> Unit,
    itemCounts: Map<ShowerCategory, Int>
) {
    Column {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { onCategorySelect(null) },
                    label = { Text("Todos") }
                )
            }
            
            items(ShowerCategory.entries) { category ->
                val count = itemCounts[category] ?: 0
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { onCategorySelect(category) },
                    label = { 
                        Text("${category.emoji} $count")
                    }
                )
            }
        }
        
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = showReceivedOnly,
                onCheckedChange = onToggleReceived,
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50))
            )
            Text(
                text = "Mostrar apenas recebidos",
                fontSize = 14.sp
            )
        }
    }
}

/**
 * Card de item do chá de bebê MODERNO
 */
@Composable
private fun ShowerItemCard(
    item: BabyShowerItem,
    onToggleReceived: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onUpdateGiftedBy: (String) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showGiftedByDialog by remember { mutableStateOf(false) }
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = if (item.isReceived) 2.dp else 6.dp
    ) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (item.isReceived) 
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF10B981).copy(alpha = 0.08f),
                                    Color.White
                                )
                            )
                        else Brush.horizontalGradient(
                            colors = listOf(Color.White, Color.White)
                        )
                    )
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Checkbox moderno
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (item.isReceived) Color(0xFF10B981) 
                            else Color(0xFFF3F4F6)
                        )
                        .clickable { 
                            if (!item.isReceived) showGiftedByDialog = true
                            else onToggleReceived()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (item.isReceived) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(14.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        textDecoration = if (item.isReceived) TextDecoration.LineThrough else null,
                        color = if (item.isReceived) Color(0xFF9CA3AF) else Color(0xFF1A1A2E)
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Quantidade
                        if (item.quantity > 1) {
                            Text(
                                text = "${item.quantityReceived}/${item.quantity}",
                                fontSize = 12.sp,
                                color = Color(0xFF9CA3AF),
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        // Prioridade com design moderno
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(item.priority.color).copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = item.priority.displayName,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(item.priority.color),
                                letterSpacing = 0.3.sp
                            )
                        }
                    }
                    
                    if (item.giftedBy.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🎁", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = item.giftedBy,
                                fontSize = 12.sp,
                                color = Color(0xFF10B981),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            Icons.Filled.MoreVert, 
                            "Menu",
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
            
            // Linha decorativa lateral
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(35.dp)
                    .align(Alignment.CenterStart)
                    .background(
                        color = if (item.isReceived) Color(0xFF10B981) else Color(item.priority.color),
                        shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                    )
            )
        }
    }
    
    if (showGiftedByDialog) {
        var giftedBy by remember { mutableStateOf("") }
        
        AlertDialog(
            onDismissRequest = { showGiftedByDialog = false },
            title = { Text("🎁 Quem presenteou?") },
            text = {
                OutlinedTextField(
                    value = giftedBy,
                    onValueChange = { giftedBy = it },
                    label = { Text("Nome (opcional)") },
                    placeholder = { Text("Ex: Tia Maria") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateGiftedBy(giftedBy)
                        showGiftedByDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showGiftedByDialog = false
                    onToggleReceived()
                }) {
                    Text("Pular")
                }
            }
        )
    }
}

@Composable
private fun EmptyShowerState(
    onAddClick: () -> Unit,
    onAddSuggestions: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🎁", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Lista vazia",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Crie sua lista de chá de bebê e compartilhe com amigos e familiares!",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onAddSuggestions,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Icon(Icons.Filled.AutoAwesome, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Adicionar sugestões")
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        OutlinedButton(onClick = onAddClick) {
            Icon(Icons.Filled.Add, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Adicionar item manualmente")
        }
    }
}

@Composable
private fun AddItemDialog(
    existingItem: BabyShowerItem?,
    onDismiss: () -> Unit,
    onSave: (BabyShowerItem) -> Unit
) {
    var name by remember { mutableStateOf(existingItem?.name ?: "") }
    var category by remember { mutableStateOf(existingItem?.category ?: ShowerCategory.CLOTHING) }
    var quantity by remember { mutableStateOf(existingItem?.quantity?.toString() ?: "1") }
    var priority by remember { mutableStateOf(existingItem?.priority ?: ItemPriority.MEDIUM) }
    var link by remember { mutableStateOf(existingItem?.link ?: "") }
    var notes by remember { mutableStateOf(existingItem?.notes ?: "") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (existingItem != null) "✏️ Editar Item" else "➕ Novo Item",
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
                        label = { Text("Nome do item") },
                        placeholder = { Text("Ex: Kit de fraldas") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                
                item {
                    Text("Categoria:", fontWeight = FontWeight.Medium)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(ShowerCategory.entries) { cat ->
                            FilterChip(
                                selected = category == cat,
                                onClick = { category = cat },
                                label = { Text(cat.emoji, fontSize = 16.sp) }
                            )
                        }
                    }
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = quantity,
                            onValueChange = { if (it.all { c -> c.isDigit() }) quantity = it },
                            label = { Text("Qtd") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                }
                
                item {
                    Text("Prioridade:", fontWeight = FontWeight.Medium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ItemPriority.entries.forEach { p ->
                            FilterChip(
                                selected = priority == p,
                                onClick = { priority = p },
                                label = { Text(p.displayName, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(p.color).copy(alpha = 0.2f)
                                )
                            )
                        }
                    }
                }
                
                item {
                    OutlinedTextField(
                        value = link,
                        onValueChange = { link = it },
                        label = { Text("Link (opcional)") },
                        placeholder = { Text("https://...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                
                item {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Observações (opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val item = BabyShowerItem(
                        id = existingItem?.id ?: 0,
                        name = name,
                        category = category,
                        quantity = quantity.toIntOrNull() ?: 1,
                        quantityReceived = existingItem?.quantityReceived ?: 0,
                        priority = priority,
                        link = link,
                        notes = notes,
                        giftedBy = existingItem?.giftedBy ?: "",
                        isReceived = existingItem?.isReceived ?: false,
                        createdAt = existingItem?.createdAt ?: System.currentTimeMillis()
                    )
                    onSave(item)
                },
                enabled = name.isNotBlank(),
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

private fun buildShareText(items: List<BabyShowerItem>): String {
    val sb = StringBuilder()
    sb.appendLine("🎁 Lista de Chá de Bebê")
    sb.appendLine("=" .repeat(30))
    sb.appendLine()
    
    val grouped = items.groupBy { it.category }
    grouped.forEach { (category, categoryItems) ->
        sb.appendLine("${category.emoji} ${category.displayName}")
        categoryItems.forEach { item ->
            val status = if (item.isReceived) "✅" else "⬜"
            val qty = if (item.quantity > 1) " (x${item.quantity})" else ""
            sb.appendLine("  $status ${item.name}$qty")
        }
        sb.appendLine()
    }
    
    val received = items.count { it.isReceived }
    sb.appendLine("📊 Progresso: $received/${items.size} itens recebidos")
    
    return sb.toString()
}

private fun getDefaultShowerItems(): List<BabyShowerItem> = listOf(
    // Roupinhas
    BabyShowerItem(name = "Bodies manga curta (RN)", category = ShowerCategory.CLOTHING, quantity = 6, priority = ItemPriority.HIGH),
    BabyShowerItem(name = "Bodies manga longa (RN)", category = ShowerCategory.CLOTHING, quantity = 6, priority = ItemPriority.HIGH),
    BabyShowerItem(name = "Macacões", category = ShowerCategory.CLOTHING, quantity = 4, priority = ItemPriority.HIGH),
    BabyShowerItem(name = "Meias", category = ShowerCategory.CLOTHING, quantity = 6, priority = ItemPriority.MEDIUM),
    BabyShowerItem(name = "Toucas", category = ShowerCategory.CLOTHING, quantity = 3, priority = ItemPriority.MEDIUM),
    BabyShowerItem(name = "Luvas", category = ShowerCategory.CLOTHING, quantity = 3, priority = ItemPriority.LOW),
    
    // Higiene
    BabyShowerItem(name = "Fraldas RN", category = ShowerCategory.HYGIENE, quantity = 2, priority = ItemPriority.HIGH),
    BabyShowerItem(name = "Fraldas P", category = ShowerCategory.HYGIENE, quantity = 4, priority = ItemPriority.HIGH),
    BabyShowerItem(name = "Lenços umedecidos", category = ShowerCategory.HYGIENE, quantity = 3, priority = ItemPriority.HIGH),
    BabyShowerItem(name = "Pomada para assaduras", category = ShowerCategory.HYGIENE, quantity = 2, priority = ItemPriority.HIGH),
    BabyShowerItem(name = "Álcool 70%", category = ShowerCategory.HYGIENE, quantity = 1, priority = ItemPriority.MEDIUM),
    
    // Alimentação
    BabyShowerItem(name = "Mamadeiras", category = ShowerCategory.FEEDING, quantity = 3, priority = ItemPriority.MEDIUM),
    BabyShowerItem(name = "Esterilizador", category = ShowerCategory.FEEDING, quantity = 1, priority = ItemPriority.MEDIUM),
    BabyShowerItem(name = "Babadores", category = ShowerCategory.FEEDING, quantity = 6, priority = ItemPriority.MEDIUM),
    
    // Quarto
    BabyShowerItem(name = "Berço", category = ShowerCategory.BEDROOM, quantity = 1, priority = ItemPriority.HIGH),
    BabyShowerItem(name = "Colchão de berço", category = ShowerCategory.BEDROOM, quantity = 1, priority = ItemPriority.HIGH),
    BabyShowerItem(name = "Lençóis de berço", category = ShowerCategory.BEDROOM, quantity = 3, priority = ItemPriority.MEDIUM),
    BabyShowerItem(name = "Móbile", category = ShowerCategory.BEDROOM, quantity = 1, priority = ItemPriority.LOW),
    BabyShowerItem(name = "Babá eletrônica", category = ShowerCategory.BEDROOM, quantity = 1, priority = ItemPriority.MEDIUM),
    
    // Passeio
    BabyShowerItem(name = "Carrinho de bebê", category = ShowerCategory.STROLLER, quantity = 1, priority = ItemPriority.HIGH),
    BabyShowerItem(name = "Bebê conforto", category = ShowerCategory.STROLLER, quantity = 1, priority = ItemPriority.HIGH),
    BabyShowerItem(name = "Bolsa maternidade", category = ShowerCategory.STROLLER, quantity = 1, priority = ItemPriority.HIGH),
    BabyShowerItem(name = "Canguru/Sling", category = ShowerCategory.STROLLER, quantity = 1, priority = ItemPriority.MEDIUM),
    
    // Banho
    BabyShowerItem(name = "Banheira", category = ShowerCategory.BATH, quantity = 1, priority = ItemPriority.HIGH),
    BabyShowerItem(name = "Toalhas com capuz", category = ShowerCategory.BATH, quantity = 3, priority = ItemPriority.HIGH),
    BabyShowerItem(name = "Sabonete líquido", category = ShowerCategory.BATH, quantity = 2, priority = ItemPriority.HIGH),
    BabyShowerItem(name = "Shampoo", category = ShowerCategory.BATH, quantity = 1, priority = ItemPriority.MEDIUM),
    BabyShowerItem(name = "Termômetro de banho", category = ShowerCategory.BATH, quantity = 1, priority = ItemPriority.MEDIUM),
    
    // Brinquedos
    BabyShowerItem(name = "Mordedores", category = ShowerCategory.TOYS, quantity = 2, priority = ItemPriority.MEDIUM),
    BabyShowerItem(name = "Chocalhos", category = ShowerCategory.TOYS, quantity = 2, priority = ItemPriority.LOW),
    
    // Saúde
    BabyShowerItem(name = "Termômetro digital", category = ShowerCategory.HEALTH, quantity = 1, priority = ItemPriority.HIGH),
    BabyShowerItem(name = "Aspirador nasal", category = ShowerCategory.HEALTH, quantity = 1, priority = ItemPriority.MEDIUM),
    BabyShowerItem(name = "Kit higiene (tesoura, cortador)", category = ShowerCategory.HEALTH, quantity = 1, priority = ItemPriority.MEDIUM)
)
