@file:OptIn(ExperimentalMaterial3Api::class)

package com.gestantes.checklist.ui.growth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gestantes.checklist.data.entity.GrowthRecord
import com.gestantes.checklist.viewmodel.GrowthViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GrowthScreen(
    onBackClick: () -> Unit,
    viewModel: GrowthViewModel = viewModel()
) {
    val records by viewModel.allRecords.collectAsState()
    val latestRecord by viewModel.latestRecord.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showAnalysis by remember { mutableStateOf(false) }
    var analysisText by remember { mutableStateOf("") }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF8FA),
                        Color(0xFFE1F5FE),
                        Color(0xFFE8F5E9)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            GrowthHeader(onBackClick = onBackClick)
            
            // Latest measurement summary
            latestRecord?.let { record ->
                LatestMeasurementCard(
                    record = record,
                    onAnalyze = {
                        analysisText = viewModel.analyzeGrowth(record)
                        showAnalysis = true
                    }
                )
            }
            
            // Records list
            if (records.isEmpty()) {
                EmptyGrowthState(onAddClick = { showAddDialog = true })
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Histórico de medições",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF333333),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    
                    items(records.sortedByDescending { it.date }, key = { it.id }) { record ->
                        GrowthRecordCard(
                            record = record,
                            onDelete = { viewModel.deleteRecord(record) }
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
                .padding(24.dp),
            containerColor = Color(0xFF00BCD4),
            contentColor = Color.White
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Adicionar medição")
        }
    }
    
    // Add dialog
    if (showAddDialog) {
        AddGrowthRecordDialog(
            onDismiss = { showAddDialog = false },
            onSave = { age, weight, height, head, notes, measuredBy ->
                viewModel.saveRecord(age, weight, height, head, notes, measuredBy)
                showAddDialog = false
            }
        )
    }
    
    // Analysis dialog
    if (showAnalysis) {
        AlertDialog(
            onDismissRequest = { showAnalysis = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFF00BCD4)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Análise do Crescimento", color = Color(0xFF00BCD4))
                }
            },
            text = {
                Text(
                    text = analysisText,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { showAnalysis = false }) {
                    Text("Entendi", color = Color(0xFF00BCD4))
                }
            },
            containerColor = Color.White
        )
    }
}

@Composable
private fun GrowthHeader(onBackClick: () -> Unit) {
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
                    tint = Color(0xFF00BCD4)
                )
            }
            
            Text(
                text = "Crescimento 📏",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00BCD4)
            )
            
            Spacer(modifier = Modifier.width(48.dp))
        }
    }
}

@Composable
private fun LatestMeasurementCard(
    record: GrowthRecord,
    onAnalyze: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(20.dp)
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
                Text(
                    text = "Última medição",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333)
                )
                
                Text(
                    text = "${record.ageInMonths} meses",
                    fontSize = 14.sp,
                    color = Color(0xFF00BCD4),
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                record.weightKg?.let {
                    MeasurementItem(
                        icon = "⚖️",
                        value = "${it}kg",
                        label = "Peso"
                    )
                }
                
                record.heightCm?.let {
                    MeasurementItem(
                        icon = "📏",
                        value = "${it}cm",
                        label = "Altura"
                    )
                }
                
                record.headCircumferenceCm?.let {
                    MeasurementItem(
                        icon = "🧠",
                        value = "${it}cm",
                        label = "Cabeça"
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onAnalyze,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE0F7FA)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFF00BCD4)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Ver análise",
                    color = Color(0xFF00BCD4)
                )
            }
        }
    }
}

@Composable
private fun MeasurementItem(
    icon: String,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = icon, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun EmptyGrowthState(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "📏", fontSize = 64.sp)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Nenhuma medição registrada",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Acompanhe o peso, altura e\ncrescimento do seu bebê",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onAddClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Adicionar medição")
        }
    }
}

@Composable
private fun GrowthRecordCard(
    record: GrowthRecord,
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
                    .background(Color(0xFFE0F7FA)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${record.ageInMonths}m",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00BCD4)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row {
                    record.weightKg?.let {
                        Text(
                            text = "⚖️ ${it}kg",
                            fontSize = 14.sp,
                            color = Color(0xFF333333)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    
                    record.heightCm?.let {
                        Text(
                            text = "📏 ${it}cm",
                            fontSize = 14.sp,
                            color = Color(0xFF333333)
                        )
                    }
                }
                
                Text(
                    text = dateFormat.format(Date(record.date)),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                
                if (record.measuredBy.isNotBlank()) {
                    Text(
                        text = "Medido: ${record.measuredBy}",
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
private fun AddGrowthRecordDialog(
    onDismiss: () -> Unit,
    onSave: (Int, Float?, Float?, Float?, String, String) -> Unit
) {
    var ageInMonths by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var headCircumference by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var measuredBy by remember { mutableStateOf("") }
    
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
                    text = "Nova Medição 📏",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00BCD4)
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                OutlinedTextField(
                    value = ageInMonths,
                    onValueChange = { ageInMonths = it.filter { c -> c.isDigit() } },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Idade em meses") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00BCD4),
                        cursorColor = Color(0xFF00BCD4)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = weight,
                        onValueChange = { weight = it },
                        modifier = Modifier.weight(1f),
                        label = { Text("Peso (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00BCD4),
                            cursorColor = Color(0xFF00BCD4)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    OutlinedTextField(
                        value = height,
                        onValueChange = { height = it },
                        modifier = Modifier.weight(1f),
                        label = { Text("Altura (cm)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00BCD4),
                            cursorColor = Color(0xFF00BCD4)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = headCircumference,
                    onValueChange = { headCircumference = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Perímetro cefálico em cm (opcional)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00BCD4),
                        cursorColor = Color(0xFF00BCD4)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = measuredBy,
                    onValueChange = { measuredBy = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Quem mediu? (ex: pediatra, em casa)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00BCD4),
                        cursorColor = Color(0xFF00BCD4)
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
                    label = { Text("Observações (opcional)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00BCD4),
                        cursorColor = Color(0xFF00BCD4)
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
                            val age = ageInMonths.toIntOrNull() ?: 0
                            val weightKg = weight.replace(",", ".").toFloatOrNull()
                            val heightCm = height.replace(",", ".").toFloatOrNull()
                            val headCm = headCircumference.replace(",", ".").toFloatOrNull()
                            onSave(age, weightKg, heightCm, headCm, notes, measuredBy)
                        },
                        enabled = ageInMonths.isNotBlank() && (weight.isNotBlank() || height.isNotBlank()),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Salvar")
                    }
                }
            }
        }
    }
}



