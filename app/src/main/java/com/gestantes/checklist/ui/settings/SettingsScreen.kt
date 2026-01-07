@file:OptIn(ExperimentalMaterial3Api::class)

package com.gestantes.checklist.ui.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gestantes.checklist.data.preferences.AppTheme
import com.gestantes.checklist.data.preferences.UserData
import com.gestantes.checklist.data.preferences.UserPreferencesManager
import com.gestantes.checklist.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val preferencesManager = remember { UserPreferencesManager(context) }
    val userData by preferencesManager.userData.collectAsState(initial = UserData())
    val scope = rememberCoroutineScope()
    
    var showThemeDialog by remember { mutableStateOf(false) }
    var showCompanionDialog by remember { mutableStateOf(false) }
    var showPregnancyDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurações") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ============ SEÇÃO MINHA GESTAÇÃO (NOVA - ADITIVA) ============
            item {
                Text(
                    text = "🤰 Minha Gestação",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            // Semana da gestação
            item {
                SettingsCard(
                    icon = Icons.Outlined.CalendarMonth,
                    title = "Semana da Gestação",
                    subtitle = if (userData.currentWeek > 0) 
                        "Semana ${userData.currentWeek}" 
                    else 
                        "Não informada",
                    onClick = { showPregnancyDialog = true }
                )
            }
            
            // ============ SEÇÃO INCLUSÃO FAMILIAR (NOVA - ADITIVA) ============
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "👨‍👩‍👧 Você e quem te acompanha",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            // Pessoa acompanhante
            item {
                SettingsCard(
                    icon = Icons.Outlined.Favorite,
                    title = "Pessoa Acompanhante",
                    subtitle = userData.companionName.ifBlank { "Parceiro(a), familiar ou amigo(a) - opcional" },
                    onClick = { showCompanionDialog = true }
                )
            }
            
            // Mensagem de apoio
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "💕",
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "A gestação é uma jornada mais leve quando compartilhada. Conte com sua rede de apoio!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            
            // Seção de Personalização
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🎨 Personalização",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            // Tema de Cores
            item {
                SettingsCard(
                    icon = Icons.Outlined.Palette,
                    title = "Tema de Cores",
                    subtitle = getThemeDisplayName(userData.appTheme),
                    onClick = { showThemeDialog = true },
                    trailing = {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            ThemeColorPreview(userData.appTheme)
                        }
                    }
                )
            }
            
            // Seção Sobre
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "ℹ️ Sobre",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                SettingsCard(
                    icon = Icons.Outlined.Info,
                    title = "Sobre o App",
                    subtitle = "Checklist Gestantes v2.0.1",
                    onClick = { }
                )
            }
            
            // Usuário
            item {
                SettingsCard(
                    icon = Icons.Outlined.Person,
                    title = "Perfil",
                    subtitle = userData.momName.ifBlank { "Mamãe" },
                    onClick = { }
                )
            }
        }
    }
    
    // Dialog para escolher tema
    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = userData.appTheme,
            onThemeSelected = { theme ->
                scope.launch {
                    preferencesManager.saveAppTheme(theme)
                }
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }
    
    // Dialog para pessoa acompanhante (NOVO - ADITIVO)
    if (showCompanionDialog) {
        CompanionDialog(
            currentName = userData.companionName,
            onSave = { name ->
                scope.launch {
                    preferencesManager.saveCompanionName(name)
                }
                showCompanionDialog = false
            },
            onDismiss = { showCompanionDialog = false }
        )
    }
    
    // Dialog para semana da gestação (NOVO - ADITIVO)
    if (showPregnancyDialog) {
        PregnancyWeekDialog(
            currentWeek = userData.currentWeek,
            onSave = { week ->
                scope.launch {
                    preferencesManager.saveCurrentWeek(week)
                }
                showPregnancyDialog = false
            },
            onDismiss = { showPregnancyDialog = false }
        )
    }
}

@Composable
private fun SettingsCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    trailing: (@Composable () -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            trailing?.invoke()
        }
    }
}

@Composable
private fun ThemeColorPreview(theme: AppTheme) {
    val colors = when(theme) {
        AppTheme.GIRL -> listOf(GirlThemeColors.Primary, GirlThemeColors.Secondary)
        AppTheme.BOY -> listOf(BoyThemeColors.Primary, BoyThemeColors.Secondary)
        AppTheme.NEUTRAL, AppTheme.CUSTOM -> listOf(NeutralThemeColors.Primary, NeutralThemeColors.Secondary)
    }
    
    colors.forEach { color ->
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(color)
        )
    }
}

@Composable
private fun ThemeSelectionDialog(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Escolha o Tema 🎨",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ThemeOptionItem(
                    theme = AppTheme.GIRL,
                    name = "Menina",
                    emoji = "👧",
                    description = "Rosa e verde claro",
                    colors = listOf(GirlThemeColors.Primary, GirlThemeColors.Secondary),
                    isSelected = currentTheme == AppTheme.GIRL,
                    onClick = { onThemeSelected(AppTheme.GIRL) }
                )
                
                ThemeOptionItem(
                    theme = AppTheme.BOY,
                    name = "Menino",
                    emoji = "👦",
                    description = "Azul e verde",
                    colors = listOf(BoyThemeColors.Primary, BoyThemeColors.Secondary),
                    isSelected = currentTheme == AppTheme.BOY,
                    onClick = { onThemeSelected(AppTheme.BOY) }
                )
                
                ThemeOptionItem(
                    theme = AppTheme.NEUTRAL,
                    name = "Neutro",
                    emoji = "🌈",
                    description = "Verde e amarelo",
                    colors = listOf(NeutralThemeColors.Primary, NeutralThemeColors.Secondary),
                    isSelected = currentTheme == AppTheme.NEUTRAL,
                    onClick = { onThemeSelected(AppTheme.NEUTRAL) }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun ThemeOptionItem(
    theme: AppTheme,
    name: String,
    emoji: String,
    description: String,
    colors: List<Color>,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) colors[0] else Color.LightGray,
        label = "borderColor"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) colors[0].copy(alpha = 0.1f) else Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = borderColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            
            // Color preview
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }
            
            if (isSelected) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = colors[0]
                )
            }
        }
    }
}

private fun getThemeDisplayName(theme: AppTheme): String = when(theme) {
    AppTheme.GIRL -> "Menina (Rosa)"
    AppTheme.BOY -> "Menino (Azul)"
    AppTheme.NEUTRAL -> "Neutro (Verde)"
    AppTheme.CUSTOM -> "Personalizado"
}

// ============ NOVOS DIALOGS - Inclusão Familiar (ADITIVOS) ============

/**
 * Dialog para informar pessoa acompanhante
 * Campo opcional - pode ser parceiro(a), familiar ou amigo(a)
 */
@Composable
private fun CompanionDialog(
    currentName: String,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Pessoa Acompanhante 💕",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Quem está te acompanhando nessa jornada? Pode ser seu parceiro(a), familiar, amigo(a) ou qualquer pessoa especial.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nome (opcional)") },
                    placeholder = { Text("Ex: João, Maria, Vovó Ana...") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                
                Text(
                    text = "💡 Este campo é opcional e privado. Serve para personalizar sua experiência.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(name) },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

/**
 * Dialog para informar semana da gestação
 */
@Composable
private fun PregnancyWeekDialog(
    currentWeek: Int,
    onSave: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var weekText by remember { mutableStateOf(if (currentWeek > 0) currentWeek.toString() else "") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Semana da Gestação 📅",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Em qual semana de gestação você está? Essa informação ajuda a personalizar os checklists e conteúdos para você.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                
                OutlinedTextField(
                    value = weekText,
                    onValueChange = { 
                        weekText = it.filter { char -> char.isDigit() }
                        errorMessage = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Semana (1-42)") },
                    placeholder = { Text("Ex: 20") },
                    singleLine = true,
                    isError = errorMessage != null,
                    supportingText = errorMessage?.let { { Text(it, color = Color.Red) } },
                    shape = RoundedCornerShape(12.dp)
                )
                
                Text(
                    text = "💡 Você pode perguntar ao seu médico ou calcular a partir da data da última menstruação.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    val week = weekText.toIntOrNull()
                    when {
                        week == null && weekText.isNotBlank() -> {
                            errorMessage = "Digite um número válido"
                        }
                        week != null && (week < 1 || week > 42) -> {
                            errorMessage = "A semana deve estar entre 1 e 42"
                        }
                        else -> {
                            onSave(week ?: 0)
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

