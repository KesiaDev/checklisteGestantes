@file:OptIn(ExperimentalMaterial3Api::class)

package com.gestantes.checklist.ui.belly

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gestantes.checklist.data.database.ChecklistDatabase
import com.gestantes.checklist.data.entity.BellyPhoto
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.layout.statusBarsPadding

@Composable
fun BellyGalleryScreen(
    onBackClick: () -> Unit,
    currentWeek: Int = 20
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { ChecklistDatabase.getDatabase(context) }
    val photos by database.bellyPhotoDao().getAllPhotos().collectAsState(initial = emptyList())
    
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedWeek by remember { mutableStateOf(currentWeek) }
    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var photoNote by remember { mutableStateOf("") }
    
    var showCompareMode by remember { mutableStateOf(false) }
    var comparePhoto1 by remember { mutableStateOf<BellyPhoto?>(null) }
    var comparePhoto2 by remember { mutableStateOf<BellyPhoto?>(null) }
    
    var showPhotoDetail by remember { mutableStateOf<BellyPhoto?>(null) }
    
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedPhotoUri = it
            showAddDialog = true
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF8FA),
                        Color(0xFFFFF0F5),
                        Color(0xFFFFE4EC)
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
                            text = "📸 Galeria da Barriga",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        
                        IconButton(
                            onClick = { showCompareMode = !showCompareMode }
                        ) {
                            Icon(
                                if (showCompareMode) Icons.Filled.Close else Icons.Filled.Compare,
                                contentDescription = "Comparar",
                                tint = Color.White
                            )
                        }
                    }
                    
                    Text(
                        text = "Registre a evolução da sua barriga semana a semana",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Compare Mode
            AnimatedVisibility(visible = showCompareMode && photos.size >= 2) {
                CompareSection(
                    photos = photos,
                    photo1 = comparePhoto1,
                    photo2 = comparePhoto2,
                    onSelectPhoto1 = { comparePhoto1 = it },
                    onSelectPhoto2 = { comparePhoto2 = it }
                )
            }
            
            // Stats Card
            if (photos.isNotEmpty()) {
                StatsCard(
                    totalPhotos = photos.size,
                    firstWeek = photos.minOfOrNull { it.week } ?: 0,
                    lastWeek = photos.maxOfOrNull { it.week } ?: 0
                )
            }
            
            // Content
            if (photos.isEmpty()) {
                EmptyGalleryState(
                    onAddClick = { imagePicker.launch("image/*") }
                )
            } else {
                // Week selector
                WeekSelector(
                    photos = photos,
                    currentWeek = currentWeek,
                    onWeekClick = { week ->
                        photos.find { it.week == week }?.let { photo ->
                            showPhotoDetail = photo
                        }
                    }
                )
                
                // Photo Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(photos) { photo ->
                        PhotoGridItem(
                            photo = photo,
                            isCompareMode = showCompareMode,
                            isSelected = comparePhoto1 == photo || comparePhoto2 == photo,
                            onClick = {
                                if (showCompareMode) {
                                    when {
                                        comparePhoto1 == null -> comparePhoto1 = photo
                                        comparePhoto2 == null && photo != comparePhoto1 -> comparePhoto2 = photo
                                        comparePhoto1 == photo -> comparePhoto1 = null
                                        comparePhoto2 == photo -> comparePhoto2 = null
                                    }
                                } else {
                                    showPhotoDetail = photo
                                }
                            }
                        )
                    }
                }
            }
        }
        
        // FAB
        FloatingActionButton(
            onClick = { 
                selectedWeek = currentWeek
                imagePicker.launch("image/*")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFFE91E63)
        ) {
            Icon(
                Icons.Filled.AddAPhoto,
                contentDescription = "Adicionar foto",
                tint = Color.White
            )
        }
        
        // Add Dialog
        if (showAddDialog && selectedPhotoUri != null) {
            AddPhotoDialog(
                photoUri = selectedPhotoUri!!,
                initialWeek = selectedWeek,
                currentWeek = currentWeek,
                existingWeeks = photos.map { it.week }.toSet(),
                onDismiss = { 
                    showAddDialog = false
                    selectedPhotoUri = null
                    photoNote = ""
                },
                onConfirm = { week, note ->
                    scope.launch {
                        // Copiar imagem para armazenamento interno
                        val inputStream = context.contentResolver.openInputStream(selectedPhotoUri!!)
                        val fileName = "belly_week_${week}_${System.currentTimeMillis()}.jpg"
                        val file = File(context.filesDir, fileName)
                        inputStream?.use { input ->
                            file.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                        
                        // Deletar foto anterior da mesma semana se existir
                        database.bellyPhotoDao().deleteByWeek(week)
                        
                        // Salvar nova foto
                        database.bellyPhotoDao().insert(
                            BellyPhoto(
                                week = week,
                                photoUri = file.absolutePath,
                                note = note
                            )
                        )
                        
                        showAddDialog = false
                        selectedPhotoUri = null
                        photoNote = ""
                    }
                }
            )
        }
        
        // Photo Detail
        showPhotoDetail?.let { photo ->
            PhotoDetailDialog(
                photo = photo,
                onDismiss = { showPhotoDetail = null },
                onDelete = {
                    scope.launch {
                        // Deletar arquivo
                        File(photo.photoUri).delete()
                        database.bellyPhotoDao().delete(photo)
                        showPhotoDetail = null
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
    totalPhotos: Int,
    firstWeek: Int,
    lastWeek: Int
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
                value = "$totalPhotos",
                label = "Fotos",
                emoji = "📷"
            )
            
            // Divisor
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(50.dp)
                    .background(Color(0xFFF3F4F6))
            )
            
            StatItem(
                value = "Sem $firstWeek",
                label = "Primeira",
                emoji = "🌱"
            )
            
            // Divisor
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(50.dp)
                    .background(Color(0xFFF3F4F6))
            )
            
            StatItem(
                value = "Sem $lastWeek",
                label = "Última",
                emoji = "🤰"
            )
        }
    }
}

/**
 * Item de estatística MODERNO
 */
@Composable
private fun StatItem(
    value: String,
    label: String,
    emoji: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFE91E63).copy(alpha = 0.15f),
                            Color(0xFFE91E63).copy(alpha = 0.25f)
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color(0xFF1A1A2E)
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFF9CA3AF)
        )
    }
}

@Composable
private fun WeekSelector(
    photos: List<BellyPhoto>,
    currentWeek: Int,
    onWeekClick: (Int) -> Unit
) {
    val weeks = (4..40).toList()
    val photosWeeks = photos.map { it.week }.toSet()
    
    LazyRow(
        modifier = Modifier.padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(weeks) { week ->
            val hasPhoto = week in photosWeeks
            val isCurrent = week == currentWeek
            
            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .clickable(enabled = hasPhoto) { onWeekClick(week) },
                shape = CircleShape,
                color = when {
                    hasPhoto -> Color(0xFFE91E63)
                    isCurrent -> Color(0xFFE91E63).copy(alpha = 0.3f)
                    else -> Color.LightGray.copy(alpha = 0.3f)
                }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "$week",
                        fontWeight = if (hasPhoto || isCurrent) FontWeight.Bold else FontWeight.Normal,
                        color = if (hasPhoto) Color.White else Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

/**
 * Item de foto na grid MODERNO
 */
@Composable
private fun PhotoGridItem(
    photo: BellyPhoto,
    isCompareMode: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = if (isSelected) 8.dp else 4.dp,
        color = Color.White
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photo.photoUri)
                    .crossfade(true)
                    .build(),
                contentDescription = "Semana ${photo.week}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            
            // Gradiente inferior para legibilidade
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.5f)
                            )
                        )
                    )
            )
            
            // Week badge moderno
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFE91E63),
                                Color(0xFFEC407A)
                            )
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Sem ${photo.week}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            if (isCompareMode && isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFE91E63).copy(alpha = 0.5f),
                                    Color(0xFFE91E63).copy(alpha = 0.3f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color(0xFFE91E63),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            
            // Borda de seleção
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = 3.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFE91E63),
                                    Color(0xFFEC407A)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                )
            }
        }
    }
}

/**
 * Seção de comparação MODERNA
 */
@Composable
private fun CompareSection(
    photos: List<BellyPhoto>,
    photo1: BellyPhoto?,
    photo2: BellyPhoto?,
    onSelectPhoto1: (BellyPhoto?) -> Unit,
    onSelectPhoto2: (BellyPhoto?) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFE91E63).copy(alpha = 0.15f),
                                    Color(0xFFE91E63).copy(alpha = 0.25f)
                                )
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "📊", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Comparar Fotos",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1A1A2E)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Photo 1
                ComparePhotoSlot(
                    photo = photo1,
                    label = "Antes",
                    onClear = { onSelectPhoto1(null) }
                )
                
                // Arrow moderno
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(40.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFE91E63),
                                    Color(0xFFEC407A)
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                // Photo 2
                ComparePhotoSlot(
                    photo = photo2,
                    label = "Depois",
                    onClear = { onSelectPhoto2(null) }
                )
            }
            
            Spacer(modifier = Modifier.height(14.dp))
            
            if (photo1 != null && photo2 != null) {
                val weeksDiff = kotlin.math.abs(photo2.week - photo1.week)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF10B981).copy(alpha = 0.1f),
                                    Color(0xFF34D399).copy(alpha = 0.1f)
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🎯 Diferença de $weeksDiff semanas!",
                        fontSize = 14.sp,
                        color = Color(0xFF10B981),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else {
                Text(
                    text = "Selecione 2 fotos na galeria para comparar",
                    fontSize = 12.sp,
                    color = Color(0xFF9CA3AF),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ComparePhotoSlot(
    photo: BellyPhoto?,
    label: String,
    onClear: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            if (photo != null) {
                AsyncImage(
                    model = photo.photoUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = onClear,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Remover",
                        tint = Color.White,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .padding(2.dp)
                            .size(16.dp)
                    )
                }
            } else {
                Text(
                    text = "📷",
                    fontSize = 32.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (photo != null) "Sem ${photo.week}" else label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (photo != null) Color(0xFFE91E63) else Color.Gray
        )
    }
}

@Composable
private fun EmptyGalleryState(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "📸", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Sua galeria está vazia",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Comece a registrar a evolução da sua barriga! Tire uma foto por semana e acompanhe o crescimento.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAddClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
        ) {
            Icon(Icons.Filled.AddAPhoto, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Adicionar primeira foto")
        }
    }
}

@Composable
private fun AddPhotoDialog(
    photoUri: Uri,
    initialWeek: Int,
    currentWeek: Int,
    existingWeeks: Set<Int>,
    onDismiss: () -> Unit,
    onConfirm: (week: Int, note: String) -> Unit
) {
    var selectedWeek by remember { mutableStateOf(initialWeek) }
    var note by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "📸 Nova Foto da Barriga",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                // Preview
                AsyncImage(
                    model = photoUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Week selector
                Text(
                    text = "Semana da gestação:",
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items((4..40).toList()) { week ->
                        val hasExisting = week in existingWeeks
                        FilterChip(
                            selected = selectedWeek == week,
                            onClick = { selectedWeek = week },
                            label = { Text("$week") },
                            leadingIcon = if (hasExisting) {
                                { Icon(Icons.Filled.Refresh, null, Modifier.size(16.dp)) }
                            } else null
                        )
                    }
                }
                
                if (selectedWeek in existingWeeks) {
                    Text(
                        text = "⚠️ Já existe uma foto da semana $selectedWeek. Ela será substituída.",
                        fontSize = 12.sp,
                        color = Color(0xFFFF9800),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Note
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Nota (opcional)") },
                    placeholder = { Text("Como você está se sentindo?") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedWeek, note) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
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
private fun PhotoDetailDialog(
    photo: BellyPhoto,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📸 Semana ${photo.week}",
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { showDeleteConfirm = true }) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Deletar",
                        tint = Color(0xFFE53935)
                    )
                }
            }
        },
        text = {
            Column {
                AsyncImage(
                    model = photo.photoUri,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "📅 ${dateFormat.format(Date(photo.createdAt))}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                if (photo.note.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "💭 ${photo.note}",
                        fontSize = 14.sp,
                        color = Color(0xFF333333)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar")
            }
        }
    )
    
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Deletar foto?") },
            text = { Text("Tem certeza que deseja deletar a foto da semana ${photo.week}?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirm = false
                        onDelete()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                ) {
                    Text("Deletar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
