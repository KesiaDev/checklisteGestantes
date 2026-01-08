@file:OptIn(ExperimentalMaterial3Api::class)

package com.gestantes.checklist.ui.reminder

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestantes.checklist.data.database.ChecklistDatabase
import com.gestantes.checklist.data.entity.Reminder
import com.gestantes.checklist.data.entity.ReminderType
import com.gestantes.checklist.data.entity.RepeatType
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.layout.statusBarsPadding

@Composable
fun ReminderScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { ChecklistDatabase.getDatabase(context) }
    val reminders by database.reminderDao().getAllReminders().collectAsState(initial = emptyList())
    
    var showAddDialog by remember { mutableStateOf(false) }
    var editingReminder by remember { mutableStateOf<Reminder?>(null) }
    var selectedFilter by remember { mutableStateOf<ReminderType?>(null) }
    var showCompletedOnly by remember { mutableStateOf(false) }
    
    // Filtrar
    val filteredReminders = remember(reminders, selectedFilter, showCompletedOnly) {
        reminders.filter { reminder ->
            (selectedFilter == null || reminder.type == selectedFilter) &&
            (!showCompletedOnly || reminder.isCompleted)
        }.sortedBy { it.dateTime }
    }
    
    // Stats
    val upcomingCount = reminders.count { !it.isCompleted && it.dateTime > System.currentTimeMillis() }
    val overdueCount = reminders.count { !it.isCompleted && it.dateTime <= System.currentTimeMillis() }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE8EAF6),
                        Color(0xFFC5CAE9),
                        Color(0xFF9FA8DA)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF5C6BC0),
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
                            text = "🔔 Lembretes",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                    
                    Text(
                        text = "Consultas, exames e compromissos",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Stats
            if (reminders.isNotEmpty()) {
                StatsCard(
                    upcomingCount = upcomingCount,
                    overdueCount = overdueCount,
                    totalCount = reminders.size
                )
            }
            
            // Filters
            FilterSection(
                selectedFilter = selectedFilter,
                showCompletedOnly = showCompletedOnly,
                onFilterSelect = { selectedFilter = it },
                onToggleCompleted = { showCompletedOnly = it },
                typeCounts = reminders.groupingBy { it.type }.eachCount()
            )
            
            // Content
            if (reminders.isEmpty()) {
                EmptyRemindersState(onAddClick = { showAddDialog = true })
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Atrasados
                    val overdue = filteredReminders.filter { 
                        !it.isCompleted && it.dateTime <= System.currentTimeMillis() 
                    }
                    if (overdue.isNotEmpty()) {
                        item {
                            Text(
                                text = "⚠️ Atrasados",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFFE53935),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(overdue, key = { it.id }) { reminder ->
                            ReminderCard(
                                reminder = reminder,
                                isOverdue = true,
                                onToggleComplete = {
                                    scope.launch {
                                        database.reminderDao().update(
                                            reminder.copy(isCompleted = !reminder.isCompleted)
                                        )
                                    }
                                },
                                onEdit = {
                                    editingReminder = reminder
                                    showAddDialog = true
                                },
                                onDelete = {
                                    scope.launch {
                                        database.reminderDao().delete(reminder)
                                    }
                                }
                            )
                        }
                    }
                    
                    // Próximos
                    val upcoming = filteredReminders.filter { 
                        !it.isCompleted && it.dateTime > System.currentTimeMillis() 
                    }
                    if (upcoming.isNotEmpty()) {
                        item {
                            Text(
                                text = "📅 Próximos",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF5C6BC0),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(upcoming, key = { it.id }) { reminder ->
                            ReminderCard(
                                reminder = reminder,
                                isOverdue = false,
                                onToggleComplete = {
                                    scope.launch {
                                        database.reminderDao().update(
                                            reminder.copy(isCompleted = !reminder.isCompleted)
                                        )
                                    }
                                },
                                onEdit = {
                                    editingReminder = reminder
                                    showAddDialog = true
                                },
                                onDelete = {
                                    scope.launch {
                                        database.reminderDao().delete(reminder)
                                    }
                                }
                            )
                        }
                    }
                    
                    // Concluídos
                    val completed = filteredReminders.filter { it.isCompleted }
                    if (completed.isNotEmpty() && showCompletedOnly) {
                        item {
                            Text(
                                text = "✅ Concluídos",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF4CAF50),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(completed, key = { it.id }) { reminder ->
                            ReminderCard(
                                reminder = reminder,
                                isOverdue = false,
                                onToggleComplete = {
                                    scope.launch {
                                        database.reminderDao().update(
                                            reminder.copy(isCompleted = !reminder.isCompleted)
                                        )
                                    }
                                },
                                onEdit = {
                                    editingReminder = reminder
                                    showAddDialog = true
                                },
                                onDelete = {
                                    scope.launch {
                                        database.reminderDao().delete(reminder)
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
                editingReminder = null
                showAddDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF5C6BC0)
        ) {
            Icon(Icons.Filled.Add, "Adicionar", tint = Color.White)
        }
        
        // Add Dialog
        if (showAddDialog) {
            AddReminderDialog(
                existingReminder = editingReminder,
                onDismiss = { 
                    showAddDialog = false
                    editingReminder = null
                },
                onSave = { reminder ->
                    scope.launch {
                        if (editingReminder != null) {
                            database.reminderDao().update(reminder)
                        } else {
                            database.reminderDao().insert(reminder)
                        }
                        showAddDialog = false
                        editingReminder = null
                    }
                }
            )
        }
    }
}

/**
 * Card de estatísticas MODERNO
 */
@Composable
private fun StatsCard(
    upcomingCount: Int,
    overdueCount: Int,
    totalCount: Int
) {
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
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatItem(
                value = "$upcomingCount",
                label = "Próximos",
                emoji = "📅",
                color = Color(0xFF5C6BC0)
            )
            
            // Divisor
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(50.dp)
                    .background(Color(0xFFF3F4F6))
            )
            
            StatItem(
                value = "$overdueCount",
                label = "Atrasados",
                emoji = "⚠️",
                color = if (overdueCount > 0) Color(0xFFEF4444) else Color(0xFF9CA3AF)
            )
            
            // Divisor
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(50.dp)
                    .background(Color(0xFFF3F4F6))
            )
            
            StatItem(
                value = "$totalCount",
                label = "Total",
                emoji = "📋",
                color = Color(0xFF10B981)
            )
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    emoji: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = emoji, fontSize = 24.sp)
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = color
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun FilterSection(
    selectedFilter: ReminderType?,
    showCompletedOnly: Boolean,
    onFilterSelect: (ReminderType?) -> Unit,
    onToggleCompleted: (Boolean) -> Unit,
    typeCounts: Map<ReminderType, Int>
) {
    Column {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedFilter == null,
                    onClick = { onFilterSelect(null) },
                    label = { Text("Todos") }
                )
            }
            
            items(ReminderType.entries) { type ->
                val count = typeCounts[type] ?: 0
                FilterChip(
                    selected = selectedFilter == type,
                    onClick = { onFilterSelect(type) },
                    label = { Text("${type.emoji} $count") }
                )
            }
        }
        
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = showCompletedOnly,
                onCheckedChange = onToggleCompleted,
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF5C6BC0))
            )
            Text(text = "Mostrar concluídos", fontSize = 14.sp)
        }
    }
}

/**
 * Card de lembrete MODERNO
 */
@Composable
private fun ReminderCard(
    reminder: Reminder,
    isOverdue: Boolean,
    onToggleComplete: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val typeColor = Color(reminder.type.color)
    
    val accentColor = when {
        reminder.isCompleted -> Color(0xFF10B981)
        isOverdue -> Color(0xFFEF4444)
        else -> typeColor
    }
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = if (reminder.isCompleted) 2.dp else 6.dp
    ) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.06f),
                                Color.White
                            )
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
                            if (reminder.isCompleted) Color(0xFF10B981) 
                            else Color(0xFFF3F4F6)
                        )
                        .clickable { onToggleComplete() },
                    contentAlignment = Alignment.Center
                ) {
                    if (reminder.isCompleted) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Ícone do tipo
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    typeColor.copy(alpha = 0.15f),
                                    typeColor.copy(alpha = 0.25f)
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = reminder.type.emoji, fontSize = 22.sp)
                }
                
                Spacer(modifier = Modifier.width(14.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = reminder.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        textDecoration = if (reminder.isCompleted) TextDecoration.LineThrough else null,
                        color = if (reminder.isCompleted) Color(0xFF9CA3AF) else Color(0xFF1A1A2E)
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Data/hora com badge
                        Box(
                            modifier = Modifier
                                .background(
                                    color = accentColor.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "${dateFormat.format(Date(reminder.dateTime))} • ${timeFormat.format(Date(reminder.dateTime))}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = accentColor
                            )
                        }
                        
                        if (reminder.repeatType != RepeatType.NONE) {
                            Text(
                                text = "🔄",
                                fontSize = 12.sp
                            )
                        }
                    }
                    
                    if (reminder.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = reminder.description,
                            fontSize = 12.sp,
                            color = Color(0xFF9CA3AF),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
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
                    .height(40.dp)
                    .align(Alignment.CenterStart)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                accentColor,
                                accentColor.copy(alpha = 0.5f)
                            )
                        ),
                        shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                    )
            )
        }
    }
}

@Composable
private fun EmptyRemindersState(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🔔", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Nenhum lembrete",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Adicione lembretes de consultas, exames e vacinas para não esquecer!",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAddClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C6BC0))
        ) {
            Icon(Icons.Filled.Add, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Criar primeiro lembrete")
        }
    }
}

@Composable
private fun AddReminderDialog(
    existingReminder: Reminder?,
    onDismiss: () -> Unit,
    onSave: (Reminder) -> Unit
) {
    var title by remember { mutableStateOf(existingReminder?.title ?: "") }
    var description by remember { mutableStateOf(existingReminder?.description ?: "") }
    var type by remember { mutableStateOf(existingReminder?.type ?: ReminderType.CONSULTATION) }
    var repeatType by remember { mutableStateOf(existingReminder?.repeatType ?: RepeatType.NONE) }
    
    // Data e hora
    val calendar = remember { 
        Calendar.getInstance().apply {
            if (existingReminder != null) {
                timeInMillis = existingReminder.dateTime
            } else {
                add(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 9)
                set(Calendar.MINUTE, 0)
            }
        }
    }
    var selectedDate by remember { mutableStateOf(calendar.timeInMillis) }
    
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (existingReminder != null) "✏️ Editar Lembrete" else "➕ Novo Lembrete",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Título") },
                        placeholder = { Text("Ex: Consulta pré-natal") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
                
                item {
                    Text("Tipo:", fontWeight = FontWeight.Medium)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(ReminderType.entries) { t ->
                            FilterChip(
                                selected = type == t,
                                onClick = { type = t },
                                label = { 
                                    Text("${t.emoji} ${t.displayName}", fontSize = 11.sp)
                                }
                            )
                        }
                    }
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showDatePicker = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("📅 ${dateFormat.format(Date(selectedDate))}")
                        }
                        
                        OutlinedButton(
                            onClick = { showTimePicker = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("⏰ ${timeFormat.format(Date(selectedDate))}")
                        }
                    }
                }
                
                item {
                    Text("Repetir:", fontWeight = FontWeight.Medium)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(RepeatType.entries) { r ->
                            FilterChip(
                                selected = repeatType == r,
                                onClick = { repeatType = r },
                                label = { Text(r.displayName, fontSize = 12.sp) }
                            )
                        }
                    }
                }
                
                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descrição (opcional)") },
                        placeholder = { Text("Ex: Dr. Carlos - Maternidade X") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val reminder = Reminder(
                        id = existingReminder?.id ?: 0,
                        title = title,
                        description = description,
                        type = type,
                        dateTime = selectedDate,
                        repeatType = repeatType,
                        isCompleted = existingReminder?.isCompleted ?: false,
                        isNotified = existingReminder?.isNotified ?: false,
                        createdAt = existingReminder?.createdAt ?: System.currentTimeMillis()
                    )
                    onSave(reminder)
                },
                enabled = title.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C6BC0))
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
    
    // Date Picker
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val cal = Calendar.getInstance().apply {
                            timeInMillis = selectedDate
                        }
                        val newCal = Calendar.getInstance().apply {
                            timeInMillis = millis
                            set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY))
                            set(Calendar.MINUTE, cal.get(Calendar.MINUTE))
                        }
                        selectedDate = newCal.timeInMillis
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    
    // Time Picker
    if (showTimePicker) {
        val cal = Calendar.getInstance().apply { timeInMillis = selectedDate }
        val timePickerState = rememberTimePickerState(
            initialHour = cal.get(Calendar.HOUR_OF_DAY),
            initialMinute = cal.get(Calendar.MINUTE)
        )
        
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Selecionar horário") },
            text = {
                TimePicker(state = timePickerState)
            },
            confirmButton = {
                TextButton(onClick = {
                    val newCal = Calendar.getInstance().apply {
                        timeInMillis = selectedDate
                        set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        set(Calendar.MINUTE, timePickerState.minute)
                    }
                    selectedDate = newCal.timeInMillis
                    showTimePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
