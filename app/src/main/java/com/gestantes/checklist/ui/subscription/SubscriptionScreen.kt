@file:OptIn(ExperimentalMaterial3Api::class)

package com.gestantes.checklist.ui.subscription

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.billingclient.api.ProductDetails
import com.gestantes.checklist.billing.SubscriptionManager

@Composable
fun SubscriptionScreen(
    subscriptionManager: SubscriptionManager,
    onBackClick: () -> Unit,
    onSubscriptionSuccess: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    
    val products by subscriptionManager.subscriptionProducts.collectAsState()
    val isPremium by subscriptionManager.isPremium.collectAsState()
    val isLoading by subscriptionManager.isLoading.collectAsState()
    
    var selectedPlan by remember { mutableStateOf<String?>(null) }
    
    // Se já é premium, navegar de volta
    LaunchedEffect(isPremium) {
        if (isPremium) {
            onSubscriptionSuccess()
        }
    }
    
    val monthlyProduct = products.find { it.productId == SubscriptionManager.SUBSCRIPTION_MONTHLY }
    val yearlyProduct = products.find { it.productId == SubscriptionManager.SUBSCRIPTION_YEARLY }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF0F5),
                        Color(0xFFFFE4EC),
                        Color(0xFFFFF0F5)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botão voltar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Fechar",
                        tint = Color(0xFFE91E63)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Ícone premium
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFE91E63), Color(0xFFFF6B9D))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Torne-se Premium 💎",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE91E63)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Desbloqueie todo o conteúdo para\nacompanhar cada momento do seu bebê",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Benefícios Premium
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "O que você recebe:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFFE91E63)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    PremiumBenefit(
                        icon = Icons.Filled.Bedtime,
                        title = "Guia Completo de Sono",
                        description = "Todas as técnicas e rotinas por idade"
                    )
                    
                    PremiumBenefit(
                        icon = Icons.Filled.ChildCare,
                        title = "Fases de Desenvolvimento",
                        description = "Acompanhe cada marco do bebê até 4 anos"
                    )
                    
                    PremiumBenefit(
                        icon = Icons.Filled.HealthAndSafety,
                        title = "Guia de Cuidados",
                        description = "Dicas para febre, alimentação, pele e mais"
                    )
                    
                    PremiumBenefit(
                        icon = Icons.Filled.Notifications,
                        title = "Notificações Personalizadas",
                        description = "Lembretes e mensagens de conforto diárias"
                    )
                    
                    PremiumBenefit(
                        icon = Icons.Filled.Favorite,
                        title = "Suporte Contínuo",
                        description = "Atualizações e novo conteúdo sempre"
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Planos de assinatura
            Text(
                text = "Escolha seu plano:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF333333)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFFE91E63))
            } else {
                // Plano Anual (destacado)
                SubscriptionPlanCard(
                    title = "Anual",
                    price = getYearlyPrice(yearlyProduct),
                    originalPrice = getYearlyOriginalPrice(yearlyProduct),
                    period = "/ano",
                    savings = "Economize 33%",
                    isSelected = selectedPlan == "yearly",
                    isRecommended = true,
                    onClick = { selectedPlan = "yearly" }
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Plano Mensal
                SubscriptionPlanCard(
                    title = "Mensal",
                    price = getMonthlyPrice(monthlyProduct),
                    originalPrice = null,
                    period = "/mês",
                    savings = null,
                    isSelected = selectedPlan == "monthly",
                    isRecommended = false,
                    onClick = { selectedPlan = "monthly" }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Botão de assinar
            Button(
                onClick = {
                    activity?.let { act ->
                        when (selectedPlan) {
                            "monthly" -> monthlyProduct?.let { product ->
                                product.subscriptionOfferDetails?.firstOrNull()?.let { offer ->
                                    subscriptionManager.launchSubscriptionFlow(act, product, offer.offerToken)
                                }
                            }
                            "yearly" -> yearlyProduct?.let { product ->
                                product.subscriptionOfferDetails?.firstOrNull()?.let { offer ->
                                    subscriptionManager.launchSubscriptionFlow(act, product, offer.offerToken)
                                }
                            }
                        }
                    }
                },
                enabled = selectedPlan != null && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE91E63),
                    disabledContainerColor = Color.Gray
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = if (selectedPlan != null) "Assinar Agora" else "Selecione um plano",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Termos
            Text(
                text = "Cancele a qualquer momento. A assinatura será renovada automaticamente.",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PremiumBenefit(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFFF0F5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFE91E63),
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = Color(0xFF333333)
            )
            Text(
                text = description,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun SubscriptionPlanCard(
    title: String,
    price: String,
    originalPrice: String?,
    period: String,
    savings: String?,
    isSelected: Boolean,
    isRecommended: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Color(0xFFE91E63) else Color(0xFFE0E0E0)
    val backgroundColor = if (isSelected) Color(0xFFFFF0F5) else Color.White
    
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { onClick() },
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF333333)
                        )
                        if (isRecommended) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = Color(0xFFE91E63),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "MELHOR",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    
                    if (savings != null) {
                        Text(
                            text = savings,
                            fontSize = 13.sp,
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = price,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color(0xFFE91E63)
                        )
                        Text(
                            text = period,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                    
                    if (originalPrice != null) {
                        Text(
                            text = originalPrice,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
            }
        }
    }
}

private fun getMonthlyPrice(product: ProductDetails?): String {
    return product?.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList?.firstOrNull()?.formattedPrice
        ?: "R$ 9,90"
}

private fun getYearlyPrice(product: ProductDetails?): String {
    return product?.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList?.firstOrNull()?.formattedPrice
        ?: "R$ 79,90"
}

private fun getYearlyOriginalPrice(product: ProductDetails?): String {
    // Preço original seria 12x o mensal
    return "R$ 118,80"
}

