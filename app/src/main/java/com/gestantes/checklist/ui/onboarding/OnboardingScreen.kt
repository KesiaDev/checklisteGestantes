@file:OptIn(ExperimentalMaterial3Api::class)

package com.gestantes.checklist.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PregnantWoman
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.gestantes.checklist.data.preferences.AppTheme
import com.gestantes.checklist.data.preferences.Baby
import com.gestantes.checklist.data.preferences.BabyGender
import com.gestantes.checklist.data.preferences.UserPreferencesManager
import com.gestantes.checklist.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    val preferencesManager = remember { UserPreferencesManager(context) }
    val scope = rememberCoroutineScope()
    
    var momName by remember { mutableStateOf("") }
    var babies by remember { mutableStateOf(listOf<Baby>()) }
    var showAddBabyDialog by remember { mutableStateOf(false) }
    var selectedTheme by remember { mutableStateOf(AppTheme.GIRL) }
    var showThemeSelector by remember { mutableStateOf(false) }
    
    // NOTA: Gênero do bebê NÃO define mais a cor do app!
    // O usuário escolhe livremente a paleta de cores que quiser.
    
    // Cores baseadas no tema selecionado
    val primaryColor = getPrimary(selectedTheme)
    val secondaryColor = getSecondary(selectedTheme)
    val accentColor = getAccent(selectedTheme)
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = 0.3f),
                        getPrimaryContainer(selectedTheme),
                        Background
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Spacer(modifier = Modifier.height(16.dp))
                
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(primaryColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = null,
                        tint = primaryColor,
                        modifier = Modifier.size(50.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Bem-vinda ao\nChecklist Gestantes! 💕",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = OnPrimaryContainer
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Vamos personalizar o app para você",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = OnSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // Step 1: Mom's name
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(primaryColor.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = primaryColor
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Como você gostaria de ser chamada?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = momName,
                            onValueChange = { momName = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Seu nome ou apelido") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryColor,
                                unfocusedBorderColor = Outline
                            )
                        )
                    }
                }
            }
            
            // Step 2: Babies
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(secondaryColor.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ChildCare,
                                    contentDescription = null,
                                    tint = secondaryColor
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Seu(s) bebê(s)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Pode adicionar mais de um! 👶",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OnSurfaceVariant
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Lista de bebês adicionados
                        if (babies.isNotEmpty()) {
                            babies.forEachIndexed { index, baby ->
                                BabyChip(
                                    baby = baby,
                                    theme = selectedTheme,
                                    onRemove = {
                                        babies = babies.filterIndexed { i, _ -> i != index }
                                    }
                                )
                                if (index < babies.size - 1) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        
                        // Botão para adicionar bebê
                        OutlinedButton(
                            onClick = { showAddBabyDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = primaryColor
                            ),
                            border = BorderStroke(1.dp, primaryColor)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (babies.isEmpty()) "Adicionar bebê" else "Adicionar outro bebê"
                            )
                        }
                    }
                }
            }
            
            // Step 3: Seletor de cores - SEMPRE disponível!
            // Escolha livre de cores, sem associação a gênero
            if (true) { // Sempre mostra o seletor de cores
                item {
                    // ====== SELETOR DE CORES MODERNO - SEM ASSOCIAÇÃO A GÊNERO ======
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(accentColor.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Palette,
                                        contentDescription = null,
                                        tint = accentColor
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Escolha suas cores favoritas 🎨",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Personalize do seu jeito!",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = OnSurfaceVariant
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Primeira linha de opções
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ThemeOption(
                                    name = AppTheme.GIRL.displayName,
                                    emoji = AppTheme.GIRL.emoji,
                                    colors = listOf(GirlThemeColors.Primary, GirlThemeColors.Secondary),
                                    isSelected = selectedTheme == AppTheme.GIRL,
                                    onClick = { selectedTheme = AppTheme.GIRL },
                                    modifier = Modifier.weight(1f)
                                )
                                
                                ThemeOption(
                                    name = AppTheme.BOY.displayName,
                                    emoji = AppTheme.BOY.emoji,
                                    colors = listOf(BoyThemeColors.Primary, BoyThemeColors.Secondary),
                                    isSelected = selectedTheme == AppTheme.BOY,
                                    onClick = { selectedTheme = AppTheme.BOY },
                                    modifier = Modifier.weight(1f)
                                )
                                
                                ThemeOption(
                                    name = AppTheme.NEUTRAL.displayName,
                                    emoji = AppTheme.NEUTRAL.emoji,
                                    colors = listOf(NeutralThemeColors.Primary, NeutralThemeColors.Secondary),
                                    isSelected = selectedTheme == AppTheme.NEUTRAL,
                                    onClick = { selectedTheme = AppTheme.NEUTRAL },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Segunda linha de opções
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ThemeOption(
                                    name = AppTheme.LAVENDER.displayName,
                                    emoji = AppTheme.LAVENDER.emoji,
                                    colors = listOf(LavenderThemeColors.Primary, LavenderThemeColors.Secondary),
                                    isSelected = selectedTheme == AppTheme.LAVENDER,
                                    onClick = { selectedTheme = AppTheme.LAVENDER },
                                    modifier = Modifier.weight(1f)
                                )
                                
                                ThemeOption(
                                    name = AppTheme.CORAL.displayName,
                                    emoji = AppTheme.CORAL.emoji,
                                    colors = listOf(CoralThemeColors.Primary, CoralThemeColors.Secondary),
                                    isSelected = selectedTheme == AppTheme.CORAL,
                                    onClick = { selectedTheme = AppTheme.CORAL },
                                    modifier = Modifier.weight(1f)
                                )
                                
                                ThemeOption(
                                    name = AppTheme.MINT.displayName,
                                    emoji = AppTheme.MINT.emoji,
                                    colors = listOf(MintThemeColors.Primary, MintThemeColors.Secondary),
                                    isSelected = selectedTheme == AppTheme.MINT,
                                    onClick = { selectedTheme = AppTheme.MINT },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Terceira linha de opções
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ThemeOption(
                                    name = AppTheme.PEACH.displayName,
                                    emoji = AppTheme.PEACH.emoji,
                                    colors = listOf(PeachThemeColors.Primary, PeachThemeColors.Secondary),
                                    isSelected = selectedTheme == AppTheme.PEACH,
                                    onClick = { selectedTheme = AppTheme.PEACH },
                                    modifier = Modifier.weight(1f)
                                )
                                
                                ThemeOption(
                                    name = AppTheme.OCEAN.displayName,
                                    emoji = AppTheme.OCEAN.emoji,
                                    colors = listOf(OceanThemeColors.Primary, OceanThemeColors.Secondary),
                                    isSelected = selectedTheme == AppTheme.OCEAN,
                                    onClick = { selectedTheme = AppTheme.OCEAN },
                                    modifier = Modifier.weight(1f)
                                )
                                
                                ThemeOption(
                                    name = AppTheme.SUNSET.displayName,
                                    emoji = AppTheme.SUNSET.emoji,
                                    colors = listOf(SunsetThemeColors.Primary, SunsetThemeColors.Secondary),
                                    isSelected = selectedTheme == AppTheme.SUNSET,
                                    onClick = { selectedTheme = AppTheme.SUNSET },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Quarta linha - última opção centralizada
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                ThemeOption(
                                    name = AppTheme.FOREST.displayName,
                                    emoji = AppTheme.FOREST.emoji,
                                    colors = listOf(ForestThemeColors.Primary, ForestThemeColors.Secondary),
                                    isSelected = selectedTheme == AppTheme.FOREST,
                                    onClick = { selectedTheme = AppTheme.FOREST },
                                    modifier = Modifier.width(120.dp)
                                )
                            }
                        }
                    }
                }
            }
            
            // Complete button
            item {
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = {
                        scope.launch {
                            preferencesManager.saveUserData(
                                momName = momName.ifBlank { "Mamãe" },
                                babies = babies,
                                theme = selectedTheme
                            )
                            onComplete()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor
                    ),
                    enabled = momName.isNotBlank() || babies.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Começar! 🎉",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                TextButton(
                    onClick = {
                        scope.launch {
                            preferencesManager.saveUserData(
                                momName = "Mamãe",
                                babies = emptyList(),
                                theme = AppTheme.GIRL
                            )
                            onComplete()
                        }
                    }
                ) {
                    Text(
                        text = "Pular por enquanto",
                        color = OnSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
    
    // Dialog para adicionar bebê
    if (showAddBabyDialog) {
        AddBabyDialog(
            onDismiss = { showAddBabyDialog = false },
            onConfirm = { baby ->
                babies = babies + baby
                showAddBabyDialog = false
            },
            primaryColor = primaryColor
        )
    }
}

@Composable
private fun ThemeOption(
    name: String,
    emoji: String,
    colors: List<Color>,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) colors[0] else Outline,
        label = "borderColor"
    )
    
    Card(
        modifier = modifier
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Color circles
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "$emoji $name",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Center
            )
            
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = colors[0],
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun BabyChip(
    baby: Baby,
    theme: AppTheme,
    onRemove: () -> Unit
) {
    val chipColor = when (baby.gender) {
        BabyGender.GIRL -> GirlThemeColors.Primary
        BabyGender.BOY -> BoyThemeColors.Primary
        BabyGender.UNKNOWN -> if (baby.isExpecting) Tertiary else getSecondary(theme)
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = chipColor.copy(alpha = 0.15f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (baby.isExpecting) 
                        Icons.Outlined.PregnantWoman 
                    else 
                        Icons.Outlined.ChildCare,
                    contentDescription = null,
                    tint = chipColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = baby.name,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = baby.gender.emoji,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    if (baby.isExpecting) {
                        Text(
                            text = "Ainda na barriga 🤰",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceVariant
                        )
                    } else if (baby.birthDate != null) {
                        Text(
                            text = "Nascido em ${baby.birthDate}",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceVariant
                        )
                    }
                }
            }
            
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remover",
                    tint = OnSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun AddBabyDialog(
    onDismiss: () -> Unit,
    onConfirm: (Baby) -> Unit,
    primaryColor: Color
) {
    var babyName by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf(BabyGender.UNKNOWN) }
    var isExpecting by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Adicionar bebê 👶",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = babyName,
                    onValueChange = { babyName = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Nome do bebê") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "É menino ou menina?",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GenderOption(
                        gender = BabyGender.GIRL,
                        isSelected = selectedGender == BabyGender.GIRL,
                        onClick = { selectedGender = BabyGender.GIRL },
                        modifier = Modifier.weight(1f)
                    )
                    GenderOption(
                        gender = BabyGender.BOY,
                        isSelected = selectedGender == BabyGender.BOY,
                        onClick = { selectedGender = BabyGender.BOY },
                        modifier = Modifier.weight(1f)
                    )
                    GenderOption(
                        gender = BabyGender.UNKNOWN,
                        isSelected = selectedGender == BabyGender.UNKNOWN,
                        onClick = { selectedGender = BabyGender.UNKNOWN },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isExpecting,
                        onCheckedChange = { isExpecting = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = primaryColor
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Ainda estou grávida 🤰",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (babyName.isNotBlank()) {
                        onConfirm(
                            Baby(
                                name = babyName,
                                gender = selectedGender,
                                isExpecting = isExpecting
                            )
                        )
                    }
                },
                enabled = babyName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Text("Adicionar")
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

@Composable
private fun GenderOption(
    gender: BabyGender,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val genderColor = when(gender) {
        BabyGender.GIRL -> GirlThemeColors.Primary
        BabyGender.BOY -> BoyThemeColors.Primary
        BabyGender.UNKNOWN -> NeutralThemeColors.Primary
    }
    
    Card(
        modifier = modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) genderColor.copy(alpha = 0.2f) else Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) genderColor else Outline
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = gender.emoji,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = when(gender) {
                    BabyGender.GIRL -> "Menina"
                    BabyGender.BOY -> "Menino"
                    BabyGender.UNKNOWN -> "Não sei"
                },
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
