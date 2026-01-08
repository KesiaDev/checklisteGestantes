package com.gestantes.checklist.ui.tools

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.gestantes.checklist.data.preferences.UserPreferencesManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Calculadora de Data Prevista do Parto (DPP)
 * 
 * Calcula a DPP baseado na:
 * - Última menstruação (Regra de Naegele: +7 dias, -3 meses, +1 ano)
 * - Data da concepção (+266 dias)
 * - Ultrassom (data informada pelo médico)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DueDateCalculatorScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val preferencesManager = remember { UserPreferencesManager(context) }
    
    var calculationMethod by remember { mutableStateOf(0) } // 0: DUM, 1: Concepção, 2: Ultrassom
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var calculatedDueDate by remember { mutableStateOf<Date?>(null) }
    var currentWeeks by remember { mutableStateOf(0) }
    var currentDays by remember { mutableStateOf(0) }
    var daysUntilBirth by remember { mutableStateOf(0) }
    
    val datePickerState = rememberDatePickerState()
    
    // Calcular DPP quando a data muda
    LaunchedEffect(selectedDate, calculationMethod) {
        selectedDate?.let { date ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date
            
            when (calculationMethod) {
                0 -> { // DUM - Regra de Naegele
                    calendar.add(Calendar.DAY_OF_MONTH, 7)
                    calendar.add(Calendar.MONTH, -3)
                    calendar.add(Calendar.YEAR, 1)
                }
                1 -> { // Data da Concepção
                    calendar.add(Calendar.DAY_OF_MONTH, 266)
                }
                2 -> { // Ultrassom (data já é a DPP)
                    // Não faz nada, usa a data diretamente
                }
            }
            
            calculatedDueDate = calendar.time
            
            // Calcular semanas atuais
            val today = Calendar.getInstance()
            val startDate = Calendar.getInstance()
            
            if (calculationMethod == 2) {
                // Se é ultrassom, calcular data de concepção retroativamente
                startDate.time = calculatedDueDate
                startDate.add(Calendar.DAY_OF_MONTH, -280)
            } else if (calculationMethod == 1) {
                startDate.timeInMillis = date
            } else {
                startDate.timeInMillis = date
            }
            
            val diffInMillis = today.timeInMillis - startDate.timeInMillis
            val totalDays = (diffInMillis / (1000 * 60 * 60 * 24)).toInt()
            currentWeeks = totalDays / 7
            currentDays = totalDays % 7
            
            // Dias até o nascimento
            val diffUntilBirth = calendar.timeInMillis - today.timeInMillis
            daysUntilBirth = maxOf(0, (diffUntilBirth / (1000 * 60 * 60 * 24)).toInt())
            
            // Salvar nas preferências
            scope.launch {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                preferencesManager.savePregnancyData(
                    expectedDueDate = dateFormat.format(calculatedDueDate!!),
                    currentWeek = currentWeeks
                )
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF8FA),
                        Color(0xFFFCE4EC),
                        Color(0xFFF8BBD9)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFE91E63),
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
                            text = "Calculadora DPP 📅",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                }
            }
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Explicação
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "👶", fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Quando o bebê vai nascer?",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE91E63)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Calcule a data prevista do parto (DPP) e descubra em qual semana você está!",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
                
                // Método de cálculo
                item {
                    Text(
                        text = "📊 Método de Cálculo",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        MethodOption(
                            title = "Última Menstruação (DUM)",
                            description = "Primeiro dia da última menstruação",
                            emoji = "🩸",
                            isSelected = calculationMethod == 0,
                            onClick = { calculationMethod = 0; selectedDate = null; calculatedDueDate = null }
                        )
                        
                        MethodOption(
                            title = "Data da Concepção",
                            description = "Se você sabe quando concebeu",
                            emoji = "💕",
                            isSelected = calculationMethod == 1,
                            onClick = { calculationMethod = 1; selectedDate = null; calculatedDueDate = null }
                        )
                        
                        MethodOption(
                            title = "Ultrassom",
                            description = "DPP informada pelo médico",
                            emoji = "🏥",
                            isSelected = calculationMethod == 2,
                            onClick = { calculationMethod = 2; selectedDate = null; calculatedDueDate = null }
                        )
                    }
                }
                
                // Seletor de data
                item {
                    val label = when (calculationMethod) {
                        0 -> "Primeiro dia da última menstruação"
                        1 -> "Data da concepção"
                        else -> "Data prevista informada pelo médico"
                    }
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker = true },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                                    .background(Color(0xFFE91E63).copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Outlined.CalendarMonth,
                                    contentDescription = null,
                                    tint = Color(0xFFE91E63)
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = label,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = if (selectedDate != null) {
                                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                            .format(Date(selectedDate!!))
                                    } else {
                                        "Toque para selecionar"
                                    },
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedDate != null) Color(0xFF333333) else Color(0xFFE91E63)
                                )
                            }
                            
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = null,
                                tint = Color(0xFFE91E63)
                            )
                        }
                    }
                }
                
                // Resultado
                if (calculatedDueDate != null) {
                    item {
                        ResultCard(
                            dueDate = calculatedDueDate!!,
                            currentWeeks = currentWeeks,
                            currentDays = currentDays,
                            daysUntilBirth = daysUntilBirth
                        )
                    }
                    
                    // Trimestre
                    item {
                        TrimesterCard(currentWeeks = currentWeeks)
                    }
                    
                    // Datas importantes
                    item {
                        ImportantDatesCard(dueDate = calculatedDueDate!!)
                    }
                }
                
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
        
        // Date Picker Dialog
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let {
                                selectedDate = it
                            }
                            showDatePicker = false
                        }
                    ) {
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
    }
}

@Composable
private fun MethodOption(
    title: String,
    description: String,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFE91E63).copy(alpha = 0.1f) else Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFE91E63))
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333)
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            if (isSelected) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFFE91E63)
                )
            }
        }
    }
}

@Composable
private fun ResultCard(
    dueDate: Date,
    currentWeeks: Int,
    currentDays: Int,
    daysUntilBirth: Int
) {
    val dateFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("pt", "BR"))
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE91E63).copy(alpha = 0.1f),
                            Color.White
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "🎉", fontSize = 40.sp)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Data Prevista do Parto",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Text(
                    text = dateFormat.format(dueDate),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE91E63),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Semanas atuais
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$currentWeeks+$currentDays",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                        Text(
                            text = "semanas",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(50.dp)
                            .background(Color.LightGray)
                    )
                    
                    // Dias restantes
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$daysUntilBirth",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2196F3)
                        )
                        Text(
                            text = "dias restantes",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
                
                if (daysUntilBirth <= 30) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = Color(0xFFFFF3E0),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "🎊 Reta final! O bebê está quase chegando!",
                            fontSize = 13.sp,
                            color = Color(0xFFE65100),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

private data class TrimesterInfo(
    val name: String,
    val progress: Float,
    val color: Color,
    val emoji: String
)

@Composable
private fun TrimesterCard(currentWeeks: Int) {
    val trimesterInfo = when {
        currentWeeks < 14 -> TrimesterInfo("1º Trimestre", currentWeeks / 13f, Color(0xFF9C27B0), "🌱")
        currentWeeks < 28 -> TrimesterInfo("2º Trimestre", (currentWeeks - 13) / 14f, Color(0xFF4CAF50), "🌿")
        else -> TrimesterInfo("3º Trimestre", (currentWeeks - 27) / 13f, Color(0xFFFF9800), "🌻")
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = trimesterInfo.emoji, fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = trimesterInfo.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = trimesterInfo.color
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            LinearProgressIndicator(
                progress = minOf(1f, trimesterInfo.progress),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = trimesterInfo.color,
                trackColor = trimesterInfo.color.copy(alpha = 0.2f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = when {
                    currentWeeks < 14 -> "Fase de formação dos órgãos do bebê"
                    currentWeeks < 28 -> "Fase de crescimento e desenvolvimento"
                    else -> "Fase de preparação para o nascimento"
                },
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun ImportantDatesCard(dueDate: Date) {
    val calendar = Calendar.getInstance()
    calendar.time = dueDate
    
    val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
    
    // Calcular datas importantes
    val week12 = Calendar.getInstance().apply {
        time = dueDate
        add(Calendar.DAY_OF_MONTH, -196) // 40-12 = 28 semanas antes
    }
    val week20 = Calendar.getInstance().apply {
        time = dueDate
        add(Calendar.DAY_OF_MONTH, -140) // 40-20 = 20 semanas antes
    }
    val week28 = Calendar.getInstance().apply {
        time = dueDate
        add(Calendar.DAY_OF_MONTH, -84) // 40-28 = 12 semanas antes
    }
    val week37 = Calendar.getInstance().apply {
        time = dueDate
        add(Calendar.DAY_OF_MONTH, -21) // 40-37 = 3 semanas antes
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "📌 Datas Importantes",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            DateMilestone("Semana 12", dateFormat.format(week12.time), "Fim do 1º trimestre", "🎯")
            DateMilestone("Semana 20", dateFormat.format(week20.time), "Metade da gestação!", "🌟")
            DateMilestone("Semana 28", dateFormat.format(week28.time), "Início do 3º trimestre", "🚀")
            DateMilestone("Semana 37", dateFormat.format(week37.time), "Bebê a termo", "👶")
            DateMilestone("Semana 40", dateFormat.format(dueDate), "Data prevista!", "🎉")
        }
    }
}

@Composable
private fun DateMilestone(week: String, date: String, description: String, emoji: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = emoji, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = week,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Text(
            text = date,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE91E63)
        )
    }
}
