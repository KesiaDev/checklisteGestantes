package com.gestantes.checklist.ui.tools

import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Tela de Tamanho do Bebê
 * 
 * Mostra comparativos visuais do tamanho do bebê semana a semana
 * usando frutas e objetos do dia a dia.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabySizeScreen(
    onBackClick: () -> Unit,
    currentWeek: Int = 20
) {
    var selectedWeek by remember { mutableStateOf(currentWeek.coerceIn(4, 40)) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF8E1),
                        Color(0xFFFFECB3),
                        Color(0xFFFFE082)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFFF9800),
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
                            text = "Tamanho do Bebê 🍎",
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
                // Seletor de semana
                item {
                    WeekSelector(
                        selectedWeek = selectedWeek,
                        onWeekSelected = { selectedWeek = it }
                    )
                }
                
                // Card principal do tamanho
                item {
                    val sizeInfo = getBabySizeInfo(selectedWeek)
                    MainSizeCard(sizeInfo = sizeInfo, week = selectedWeek)
                }
                
                // Medidas
                item {
                    val sizeInfo = getBabySizeInfo(selectedWeek)
                    MeasurementsCard(sizeInfo = sizeInfo)
                }
                
                // Desenvolvimento
                item {
                    val sizeInfo = getBabySizeInfo(selectedWeek)
                    DevelopmentCard(sizeInfo = sizeInfo, week = selectedWeek)
                }
                
                // Curiosidade
                item {
                    val sizeInfo = getBabySizeInfo(selectedWeek)
                    FunFactCard(sizeInfo = sizeInfo)
                }
                
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun WeekSelector(
    selectedWeek: Int,
    onWeekSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "📅 Selecione a semana",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items((4..40).toList()) { week ->
                    Surface(
                        onClick = { onWeekSelected(week) },
                        shape = CircleShape,
                        color = if (week == selectedWeek) {
                            Color(0xFFFF9800)
                        } else {
                            Color(0xFFF5F5F5)
                        }
                    ) {
                        Box(
                            modifier = Modifier.size(44.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$week",
                                fontSize = 14.sp,
                                fontWeight = if (week == selectedWeek) FontWeight.Bold else FontWeight.Normal,
                                color = if (week == selectedWeek) Color.White else Color(0xFF666666)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MainSizeCard(sizeInfo: BabySizeInfo, week: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFF9800).copy(alpha = 0.1f),
                            Color.White
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Semana $week",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Emoji grande
                Text(
                    text = sizeInfo.emoji,
                    fontSize = 100.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Seu bebê tem o tamanho de",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Text(
                    text = sizeInfo.comparison,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF9800),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Badge com tamanho
                Surface(
                    color = Color(0xFFFFF3E0),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "📏 ${sizeInfo.lengthCm} cm  •  ⚖️ ${sizeInfo.weightGrams}",
                        fontSize = 14.sp,
                        color = Color(0xFFE65100),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MeasurementsCard(sizeInfo: BabySizeInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "📐 Medidas Aproximadas",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MeasurementItem(
                    emoji = "📏",
                    label = "Comprimento",
                    value = sizeInfo.lengthCm,
                    color = Color(0xFF4CAF50)
                )
                
                MeasurementItem(
                    emoji = "⚖️",
                    label = "Peso",
                    value = sizeInfo.weightGrams,
                    color = Color(0xFF2196F3)
                )
            }
        }
    }
}

@Composable
private fun MeasurementItem(
    emoji: String,
    label: String,
    value: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 28.sp)
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
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
private fun DevelopmentCard(sizeInfo: BabySizeInfo, week: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🌟", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "O que está acontecendo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            sizeInfo.developments.forEach { development ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = "•", color = Color(0xFFFF9800))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = development,
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun FunFactCard(sizeInfo: BabySizeInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(text = "💡", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Curiosidade",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = sizeInfo.funFact,
                    fontSize = 14.sp,
                    color = Color(0xFF1B5E20),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

data class BabySizeInfo(
    val emoji: String,
    val comparison: String,
    val lengthCm: String,
    val weightGrams: String,
    val developments: List<String>,
    val funFact: String
)

fun getBabySizeInfo(week: Int): BabySizeInfo {
    return when (week) {
        4 -> BabySizeInfo("🌱", "uma semente de papoula", "0,1 cm", "< 1g",
            listOf("O embrião está se implantando no útero", "As células estão se dividindo rapidamente"),
            "Nesta fase, o bebê é menor que um grão de arroz!")
        5 -> BabySizeInfo("🫘", "uma semente de gergelim", "0,2 cm", "< 1g",
            listOf("O coração começa a se formar", "O tubo neural está se desenvolvendo"),
            "O coração do bebê baterá pela primeira vez esta semana!")
        6 -> BabySizeInfo("🫐", "uma lentilha", "0,5 cm", "< 1g",
            listOf("O coração já está batendo!", "Braços e pernas começam a brotar"),
            "O coração bate cerca de 150 vezes por minuto!")
        7 -> BabySizeInfo("🫐", "um mirtilo", "1 cm", "< 1g",
            listOf("O cérebro está crescendo rapidamente", "Os olhos estão se formando"),
            "O bebê já tem pequenas mãos em forma de pás!")
        8 -> BabySizeInfo("🫒", "uma framboesa", "1,6 cm", "1g",
            listOf("Os dedos começam a se formar", "O bebê pode fazer pequenos movimentos"),
            "Apesar de pequeno, já tem quase todos os órgãos formados!")
        9 -> BabySizeInfo("🍇", "uma uva", "2,3 cm", "2g",
            listOf("Os olhos estão mais desenvolvidos", "A cauda embrionária desaparece"),
            "O bebê agora é oficialmente um feto!")
        10 -> BabySizeInfo("🍒", "uma azeitona", "3,1 cm", "4g",
            listOf("Os órgãos vitais estão funcionando", "Os dedos estão separados"),
            "As unhas começam a crescer!")
        11 -> BabySizeInfo("🍓", "um figo", "4,1 cm", "7g",
            listOf("O bebê pode abrir e fechar as mãos", "Os ossos começam a endurecer"),
            "O bebê já pode soluçar!")
        12 -> BabySizeInfo("🍋", "um limão", "5,4 cm", "14g",
            listOf("Os reflexos estão se desenvolvendo", "O rosto está mais definido"),
            "Fim do primeiro trimestre! Os órgãos estão formados.")
        13 -> BabySizeInfo("🥝", "um kiwi", "7,4 cm", "23g",
            listOf("As impressões digitais estão se formando", "O bebê pode chupar o dedo"),
            "Cada bebê tem impressões digitais únicas!")
        14 -> BabySizeInfo("🍊", "um pêssego", "8,7 cm", "43g",
            listOf("O bebê pode fazer expressões faciais", "Os pelos do corpo começam a crescer"),
            "O bebê pode franzir a testa e fazer caretas!")
        15 -> BabySizeInfo("🍎", "uma maçã", "10,1 cm", "70g",
            listOf("Os ossos estão ficando mais fortes", "O bebê pode sentir luz"),
            "Mesmo com os olhos fechados, o bebê percebe luz!")
        16 -> BabySizeInfo("🥑", "um abacate", "11,6 cm", "100g",
            listOf("O bebê pode ouvir sons", "Os músculos estão se fortalecendo"),
            "Seu bebê já pode ouvir sua voz!")
        17 -> BabySizeInfo("🍐", "uma pera", "13 cm", "140g",
            listOf("A gordura começa a se formar", "O cordão umbilical está mais forte"),
            "O bebê está desenvolvendo seu próprio sistema imunológico!")
        18 -> BabySizeInfo("🫑", "uma batata doce", "14,2 cm", "190g",
            listOf("O bebê pode bocejar", "As orelhas estão na posição final"),
            "Se for menina, os óvulos já estão se formando!")
        19 -> BabySizeInfo("🥭", "uma manga", "15,3 cm", "240g",
            listOf("A pele está desenvolvendo uma camada protetora", "Os sentidos estão mais aguçados"),
            "O bebê está coberto por um vernix protetor!")
        20 -> BabySizeInfo("🍌", "uma banana", "16,4 cm", "300g",
            listOf("Metade da gestação!", "O bebê engole líquido amniótico"),
            "Parabéns! Você está na metade da gravidez!")
        21 -> BabySizeInfo("🥕", "uma cenoura", "26,7 cm", "360g",
            listOf("O bebê tem ciclos de sono", "As sobrancelhas estão se formando"),
            "A partir de agora medimos da cabeça aos pés!")
        22 -> BabySizeInfo("🥒", "um pepino", "27,8 cm", "430g",
            listOf("Os olhos estão formados mas sem cor", "O bebê pode sentir o toque"),
            "O bebê responde ao toque na barriga!")
        23 -> BabySizeInfo("🌽", "uma espiga de milho", "28,9 cm", "500g",
            listOf("A audição está mais desenvolvida", "O bebê reconhece sua voz"),
            "O bebê pode reconhecer músicas que você ouve!")
        24 -> BabySizeInfo("🍆", "uma berinjela", "30 cm", "600g",
            listOf("Os pulmões estão amadurecendo", "O bebê tem padrões de sono"),
            "O rosto do bebê está quase completamente formado!")
        25 -> BabySizeInfo("🥦", "um brócolis", "34,6 cm", "660g",
            listOf("O cabelo está crescendo", "O bebê pode ter soluços"),
            "Você pode sentir os soluços do bebê!")
        26 -> BabySizeInfo("🥬", "uma alface", "35,6 cm", "760g",
            listOf("Os olhos podem abrir", "O bebê responde a estímulos"),
            "Os olhos do bebê estão abrindo pela primeira vez!")
        27 -> BabySizeInfo("🥗", "uma couve-flor", "36,6 cm", "875g",
            listOf("O cérebro está muito ativo", "O bebê pode ter sonhos"),
            "Os cientistas acreditam que bebês sonham no útero!")
        28 -> BabySizeInfo("🍈", "um melão", "37,6 cm", "1kg",
            listOf("Terceiro trimestre!", "O bebê pode piscar"),
            "Início do terceiro trimestre! Reta final!")
        29 -> BabySizeInfo("🎃", "uma abóbora pequena", "38,6 cm", "1,1kg",
            listOf("Os ossos estão mais fortes", "O bebê está mais ativo"),
            "O bebê está ganhando peso rapidamente!")
        30 -> BabySizeInfo("🥥", "um coco", "39,9 cm", "1,3kg",
            listOf("O bebê está ficando mais gordinho", "Os pulmões estão praticando respirar"),
            "O bebê está praticando a respiração!")
        31 -> BabySizeInfo("🍍", "um abacaxi", "41,1 cm", "1,5kg",
            listOf("O bebê processa informações", "Todos os sentidos funcionam"),
            "Os cinco sentidos do bebê estão funcionando!")
        32 -> BabySizeInfo("🥬", "um repolho", "42,4 cm", "1,7kg",
            listOf("As unhas chegaram às pontas dos dedos", "O bebê dorme mais"),
            "O bebê dorme de 90% a 95% do tempo!")
        33 -> BabySizeInfo("🍏", "um abacaxi grande", "43,7 cm", "1,9kg",
            listOf("O cérebro está crescendo muito", "Os ossos do crânio são flexíveis"),
            "O crânio permanece flexível para o parto!")
        34 -> BabySizeInfo("🎃", "uma abóbora", "45 cm", "2,1kg",
            listOf("O sistema imunológico amadurece", "O bebê está se posicionando"),
            "O bebê pode estar de cabeça para baixo!")
        35 -> BabySizeInfo("🍉", "um melão honeydew", "46,2 cm", "2,4kg",
            listOf("Os rins estão totalmente desenvolvidos", "O fígado processa resíduos"),
            "Os órgãos estão quase todos maduros!")
        36 -> BabySizeInfo("🥬", "uma alface romana", "47,4 cm", "2,6kg",
            listOf("O bebê está descendo", "A gordura continua acumulando"),
            "As bochechas do bebê estão mais fofinhas!")
        37 -> BabySizeInfo("🥬", "uma acelga", "48,6 cm", "2,9kg",
            listOf("Bebê a termo!", "Pronto para nascer"),
            "Seu bebê agora é considerado a termo!")
        38 -> BabySizeInfo("🥬", "um alho-poró", "49,8 cm", "3,1kg",
            listOf("O vérnix está diminuindo", "O bebê está praticando sugar"),
            "O bebê está perdendo a penugem (lanugo)!")
        39 -> BabySizeInfo("🍈", "uma mini melancia", "50,7 cm", "3,3kg",
            listOf("O cérebro continua se desenvolvendo", "O bebê está pronto!"),
            "O cérebro crescerá muito no primeiro ano de vida!")
        40 -> BabySizeInfo("🍉", "uma melancia", "51,2 cm", "3,5kg",
            listOf("Data prevista do parto!", "O bebê está totalmente desenvolvido"),
            "Parabéns! Seu bebê está pronto para conhecer o mundo!")
        else -> BabySizeInfo("👶", "um bebê", "~50 cm", "~3,5kg",
            listOf("Seu bebê está se desenvolvendo", "Cada bebê é único"),
            "Cada gestação é uma jornada única e especial!")
    }
}
