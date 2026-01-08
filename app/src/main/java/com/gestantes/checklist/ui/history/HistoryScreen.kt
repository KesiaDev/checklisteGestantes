@file:OptIn(ExperimentalMaterial3Api::class)

package com.gestantes.checklist.ui.history

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
import com.gestantes.checklist.data.entity.*
import com.gestantes.checklist.viewmodel.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    onBackClick: () -> Unit,
    viewModel: HistoryViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val medicalRecords by viewModel.filteredMedicalRecords.collectAsState()
    val developmentRecords by viewModel.filteredDevelopmentRecords.collectAsState()
    val selectedAgeGroup by viewModel.selectedAgeGroup.collectAsState()
    
    var showAddMedicalDialog by remember { mutableStateOf(false) }
    var showAddDevelopmentDialog by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF8FA),
                        Color(0xFFE3F2FD),
                        Color(0xFFE8F5E9)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            HistoryHeader(onBackClick = onBackClick)
            
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Color(0xFF2196F3)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("🏥 Clínico") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("⭐ Desenvolvimento") }
                )
            }
            
            // Filtro por idade
            AgeGroupFilter(
                selectedAgeGroup = selectedAgeGroup,
                onAgeGroupChange = { viewModel.setAgeGroupFilter(it) }
            )
            
            // Content
            when (selectedTab) {
                0 -> MedicalRecordsList(
                    records = medicalRecords,
                    onAddClick = { showAddMedicalDialog = true },
                    onDelete = { viewModel.deleteMedicalRecord(it) }
                )
                1 -> DevelopmentRecordsList(
                    records = developmentRecords,
                    onAddClick = { showAddDevelopmentDialog = true },
                    onDelete = { viewModel.deleteDevelopmentRecord(it) }
                )
            }
        }
        
        // FAB
        FloatingActionButton(
            onClick = {
                if (selectedTab == 0) showAddMedicalDialog = true
                else showAddDevelopmentDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = if (selectedTab == 0) Color(0xFF2196F3) else Color(0xFFFF9800),
            contentColor = Color.White
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Adicionar registro")
        }
    }
    
    // Dialogs
    if (showAddMedicalDialog) {
        AddMedicalRecordDialog(
            onDismiss = { showAddMedicalDialog = false },
            onSave = { type, title, description, date, doctor, location, notes, ageGroup ->
                viewModel.saveMedicalRecord(type, title, description, date, doctor, location, notes, ageGroup)
                showAddMedicalDialog = false
            }
        )
    }
    
    if (showAddDevelopmentDialog) {
        AddDevelopmentRecordDialog(
            onDismiss = { showAddDevelopmentDialog = false },
            onSave = { type, title, description, date, ageGroup, notes ->
                viewModel.saveDevelopmentRecord(type, title, description, date, ageGroup, notes)
                showAddDevelopmentDialog = false
            }
        )
    }
}

@Composable
private fun HistoryHeader(onBackClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color(0xFF2196F3)
                )
            }
            
            Text(
                text = "Histórico do Bebê 👶",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3)
            )
            
            Spacer(modifier = Modifier.width(48.dp))
        }
    }
}

@Composable
private fun AgeGroupFilter(
    selectedAgeGroup: AgeGroup?,
    onAgeGroupChange: (AgeGroup?) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedAgeGroup == null,
                onClick = { onAgeGroupChange(null) },
                label = { Text("Todas idades") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF2196F3),
                    selectedLabelColor = Color.White
                )
            )
        }
        
        items(AgeGroup.entries) { ageGroup ->
            FilterChip(
                selected = selectedAgeGroup == ageGroup,
                onClick = { onAgeGroupChange(ageGroup) },
                label = { Text(ageGroup.displayName) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF2196F3),
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

@Composable
private fun MedicalRecordsList(
    records: List<MedicalRecord>,
    onAddClick: () -> Unit,
    onDelete: (MedicalRecord) -> Unit
) {
    if (records.isEmpty()) {
        EmptyState(
            emoji = "🏥",
            title = "Nenhum registro clínico",
            subtitle = "Registre consultas, vacinas,\nmedicamentos e mais",
            buttonText = "Adicionar registro",
            buttonColor = Color(0xFF2196F3),
            onButtonClick = onAddClick
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(records, key = { it.id }) { record ->
                MedicalRecordCard(
                    record = record,
                    onDelete = { onDelete(record) }
                )
            }
            
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun DevelopmentRecordsList(
    records: List<DevelopmentRecord>,
    onAddClick: () -> Unit,
    onDelete: (DevelopmentRecord) -> Unit
) {
    if (records.isEmpty()) {
        EmptyState(
            emoji = "⭐",
            title = "Nenhum marco registrado",
            subtitle = "Registre primeiros passos, palavras,\ne outros marcos importantes",
            buttonText = "Adicionar marco",
            buttonColor = Color(0xFFFF9800),
            onButtonClick = onAddClick
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(records, key = { it.id }) { record ->
                DevelopmentRecordCard(
                    record = record,
                    onDelete = { onDelete(record) }
                )
            }
            
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun EmptyState(
    emoji: String,
    title: String,
    subtitle: String,
    buttonText: String,
    buttonColor: Color,
    onButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = emoji, fontSize = 64.sp)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = subtitle,
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onButtonClick,
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            shape = RoundedCornerShape(24.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(buttonText)
        }
    }
}

@Composable
private fun MedicalRecordCard(
    record: MedicalRecord,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = record.recordType.icon, fontSize = 22.sp)
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color(0xFF333333)
                )
                
                Text(
                    text = record.recordType.displayName,
                    fontSize = 12.sp,
                    color = Color(0xFF2196F3)
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = dateFormat.format(Date(record.date)),
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    
                    Text(
                        text = " • ${record.ageGroup.displayName}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
                
                if (record.doctorName.isNotBlank()) {
                    Text(
                        text = "Dr(a). ${record.doctorName}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
            
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Opções", tint = Color.Gray)
                }
                
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Excluir", color = Color.Red) },
                        onClick = {
                            showMenu = false
                            onDelete()
                        },
                        leadingIcon = {
                            Icon(Icons.Filled.Delete, contentDescription = null, tint = Color.Red)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DevelopmentRecordCard(
    record: DevelopmentRecord,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFF3E0)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = record.milestoneType.icon, fontSize = 22.sp)
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color(0xFF333333)
                )
                
                Text(
                    text = record.milestoneType.displayName,
                    fontSize = 12.sp,
                    color = Color(0xFFFF9800)
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = dateFormat.format(Date(record.date)),
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    
                    Text(
                        text = " • ${record.ageGroup.displayName}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
            
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Opções", tint = Color.Gray)
                }
                
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Excluir", color = Color.Red) },
                        onClick = {
                            showMenu = false
                            onDelete()
                        },
                        leadingIcon = {
                            Icon(Icons.Filled.Delete, contentDescription = null, tint = Color.Red)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddMedicalRecordDialog(
    onDismiss: () -> Unit,
    onSave: (MedicalRecordType, String, String, Long, String, String, String, AgeGroup) -> Unit
) {
    var selectedType by remember { mutableStateOf(MedicalRecordType.CONSULTATION) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var doctorName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedAgeGroup by remember { mutableStateOf(AgeGroup.NEWBORN) }
    val date = System.currentTimeMillis()
    
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
                    text = "Novo Registro Clínico 🏥",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2196F3)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Tipo
                Text("Tipo de registro", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(MedicalRecordType.entries) { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text("${type.icon} ${type.displayName}", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2196F3),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Faixa etária
                Text("Idade do bebê", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(AgeGroup.entries) { ageGroup ->
                        FilterChip(
                            selected = selectedAgeGroup == ageGroup,
                            onClick = { selectedAgeGroup = ageGroup },
                            label = { Text(ageGroup.displayName, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF2196F3),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Título") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2196F3),
                        cursorColor = Color(0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = doctorName,
                    onValueChange = { doctorName = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nome do médico (opcional)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2196F3),
                        cursorColor = Color(0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    label = { Text("Observações") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2196F3),
                        cursorColor = Color(0xFF2196F3)
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
                        onClick = {
                            onSave(selectedType, title, description, date, doctorName, location, notes, selectedAgeGroup)
                        },
                        enabled = title.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Salvar")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddDevelopmentRecordDialog(
    onDismiss: () -> Unit,
    onSave: (MilestoneType, String, String, Long, AgeGroup, String) -> Unit
) {
    var selectedType by remember { mutableStateOf(MilestoneType.MOTOR) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedAgeGroup by remember { mutableStateOf(AgeGroup.NEWBORN) }
    val date = System.currentTimeMillis()
    
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
                    text = "Novo Marco ⭐",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF9800)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Tipo
                Text("Tipo de marco", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(MilestoneType.entries) { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text("${type.icon} ${type.displayName}", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFFF9800),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Faixa etária
                Text("Idade do bebê", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(AgeGroup.entries) { ageGroup ->
                        FilterChip(
                            selected = selectedAgeGroup == ageGroup,
                            onClick = { selectedAgeGroup = ageGroup },
                            label = { Text(ageGroup.displayName, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFFF9800),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("O que aconteceu?") },
                    placeholder = { Text("Ex: Primeiro sorriso, disse mamãe...") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFF9800),
                        cursorColor = Color(0xFFFF9800)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    label = { Text("Detalhes e observações") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFF9800),
                        cursorColor = Color(0xFFFF9800)
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
                        onClick = {
                            onSave(selectedType, title, description, date, selectedAgeGroup, notes)
                        },
                        enabled = title.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Salvar")
                    }
                }
            }
        }
    }
}



