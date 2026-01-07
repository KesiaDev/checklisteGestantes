package com.gestantes.checklist.ui.theme

import androidx.compose.ui.graphics.Color
import com.gestantes.checklist.data.preferences.AppTheme

/**
 * Paleta de cores para o tema de MENINA (Rosa)
 */
object GirlThemeColors {
    val Primary = Color(0xFFE8A0BF)           // Rosa principal
    val PrimaryContainer = Color(0xFFFDF2F8)
    val OnPrimary = Color(0xFFFFFFFF)
    val OnPrimaryContainer = Color(0xFF4A1942)
    
    val Secondary = Color(0xFFB4D4C4)         // Verde claro
    val SecondaryContainer = Color(0xFFE8F5EE)
    val OnSecondary = Color(0xFF1A3A2A)
    val OnSecondaryContainer = Color(0xFF1A3A2A)
    
    val Tertiary = Color(0xFFF5D5C8)          // Pêssego
    val TertiaryContainer = Color(0xFFFFF5F0)
    val OnTertiary = Color(0xFF5A3A30)
    val OnTertiaryContainer = Color(0xFF5A3A30)
    
    val Background = Color(0xFFFFFBFE)
    val Surface = Color(0xFFFFFBFE)
    val SurfaceVariant = Color(0xFFF5E9EF)
    
    val CardMaternidade = Color(0xFFFADDE1)   // Rosa claro
    val CardPrenatal = Color(0xFFD4E8DC)      // Verde claro
    val CardPosparto = Color(0xFFFDE2D4)      // Pêssego claro
    
    val IconMaternidade = Color(0xFFE8A0BF)
    val IconPrenatal = Color(0xFF7CB899)
    val IconPosparto = Color(0xFFE8B49A)
    
    val Accent = Color(0xFFE91E63)            // Rosa forte
}

/**
 * Paleta de cores para o tema de MENINO (Azul)
 */
object BoyThemeColors {
    val Primary = Color(0xFF64B5F6)           // Azul principal
    val PrimaryContainer = Color(0xFFE3F2FD)
    val OnPrimary = Color(0xFFFFFFFF)
    val OnPrimaryContainer = Color(0xFF0D47A1)
    
    val Secondary = Color(0xFF81C784)         // Verde
    val SecondaryContainer = Color(0xFFE8F5E9)
    val OnSecondary = Color(0xFF1B5E20)
    val OnSecondaryContainer = Color(0xFF1B5E20)
    
    val Tertiary = Color(0xFF4DD0E1)          // Ciano
    val TertiaryContainer = Color(0xFFE0F7FA)
    val OnTertiary = Color(0xFF006064)
    val OnTertiaryContainer = Color(0xFF006064)
    
    val Background = Color(0xFFFAFCFF)
    val Surface = Color(0xFFFAFCFF)
    val SurfaceVariant = Color(0xFFE8EEF5)
    
    val CardMaternidade = Color(0xFFBBDEFB)   // Azul claro
    val CardPrenatal = Color(0xFFC8E6C9)      // Verde claro
    val CardPosparto = Color(0xFFB2EBF2)      // Ciano claro
    
    val IconMaternidade = Color(0xFF42A5F5)
    val IconPrenatal = Color(0xFF66BB6A)
    val IconPosparto = Color(0xFF26C6DA)
    
    val Accent = Color(0xFF2196F3)            // Azul forte
}

/**
 * Paleta de cores NEUTRA (Verde/Amarelo) - para quando tem menino e menina
 */
object NeutralThemeColors {
    val Primary = Color(0xFF81C784)           // Verde principal
    val PrimaryContainer = Color(0xFFE8F5E9)
    val OnPrimary = Color(0xFFFFFFFF)
    val OnPrimaryContainer = Color(0xFF1B5E20)
    
    val Secondary = Color(0xFFFFD54F)         // Amarelo
    val SecondaryContainer = Color(0xFFFFF8E1)
    val OnSecondary = Color(0xFF5D4037)
    val OnSecondaryContainer = Color(0xFF5D4037)
    
    val Tertiary = Color(0xFFFFB74D)          // Laranja claro
    val TertiaryContainer = Color(0xFFFFF3E0)
    val OnTertiary = Color(0xFF5D4037)
    val OnTertiaryContainer = Color(0xFF5D4037)
    
    val Background = Color(0xFFFFFDF7)
    val Surface = Color(0xFFFFFDF7)
    val SurfaceVariant = Color(0xFFF1F5E9)
    
    val CardMaternidade = Color(0xFFC8E6C9)   // Verde claro
    val CardPrenatal = Color(0xFFFFF9C4)      // Amarelo claro
    val CardPosparto = Color(0xFFFFE0B2)      // Laranja claro
    
    val IconMaternidade = Color(0xFF66BB6A)
    val IconPrenatal = Color(0xFFFFCA28)
    val IconPosparto = Color(0xFFFFA726)
    
    val Accent = Color(0xFF4CAF50)            // Verde forte
}

// ============ CORES DINÂMICAS BASEADAS NO TEMA ============

// Cores atuais (serão atualizadas pelo tema)
var CurrentTheme: AppTheme = AppTheme.GIRL

// Funções para obter cores baseadas no tema atual
fun getPrimary(theme: AppTheme = CurrentTheme): Color = when(theme) {
    AppTheme.GIRL -> GirlThemeColors.Primary
    AppTheme.BOY -> BoyThemeColors.Primary
    AppTheme.NEUTRAL, AppTheme.CUSTOM -> NeutralThemeColors.Primary
}

fun getPrimaryContainer(theme: AppTheme = CurrentTheme): Color = when(theme) {
    AppTheme.GIRL -> GirlThemeColors.PrimaryContainer
    AppTheme.BOY -> BoyThemeColors.PrimaryContainer
    AppTheme.NEUTRAL, AppTheme.CUSTOM -> NeutralThemeColors.PrimaryContainer
}

fun getSecondary(theme: AppTheme = CurrentTheme): Color = when(theme) {
    AppTheme.GIRL -> GirlThemeColors.Secondary
    AppTheme.BOY -> BoyThemeColors.Secondary
    AppTheme.NEUTRAL, AppTheme.CUSTOM -> NeutralThemeColors.Secondary
}

fun getAccent(theme: AppTheme = CurrentTheme): Color = when(theme) {
    AppTheme.GIRL -> GirlThemeColors.Accent
    AppTheme.BOY -> BoyThemeColors.Accent
    AppTheme.NEUTRAL, AppTheme.CUSTOM -> NeutralThemeColors.Accent
}

fun getCardMaternidade(theme: AppTheme = CurrentTheme): Color = when(theme) {
    AppTheme.GIRL -> GirlThemeColors.CardMaternidade
    AppTheme.BOY -> BoyThemeColors.CardMaternidade
    AppTheme.NEUTRAL, AppTheme.CUSTOM -> NeutralThemeColors.CardMaternidade
}

fun getCardPrenatal(theme: AppTheme = CurrentTheme): Color = when(theme) {
    AppTheme.GIRL -> GirlThemeColors.CardPrenatal
    AppTheme.BOY -> BoyThemeColors.CardPrenatal
    AppTheme.NEUTRAL, AppTheme.CUSTOM -> NeutralThemeColors.CardPrenatal
}

fun getCardPosparto(theme: AppTheme = CurrentTheme): Color = when(theme) {
    AppTheme.GIRL -> GirlThemeColors.CardPosparto
    AppTheme.BOY -> BoyThemeColors.CardPosparto
    AppTheme.NEUTRAL, AppTheme.CUSTOM -> NeutralThemeColors.CardPosparto
}

fun getIconMaternidade(theme: AppTheme = CurrentTheme): Color = when(theme) {
    AppTheme.GIRL -> GirlThemeColors.IconMaternidade
    AppTheme.BOY -> BoyThemeColors.IconMaternidade
    AppTheme.NEUTRAL, AppTheme.CUSTOM -> NeutralThemeColors.IconMaternidade
}

fun getIconPrenatal(theme: AppTheme = CurrentTheme): Color = when(theme) {
    AppTheme.GIRL -> GirlThemeColors.IconPrenatal
    AppTheme.BOY -> BoyThemeColors.IconPrenatal
    AppTheme.NEUTRAL, AppTheme.CUSTOM -> NeutralThemeColors.IconPrenatal
}

fun getIconPosparto(theme: AppTheme = CurrentTheme): Color = when(theme) {
    AppTheme.GIRL -> GirlThemeColors.IconPosparto
    AppTheme.BOY -> BoyThemeColors.IconPosparto
    AppTheme.NEUTRAL, AppTheme.CUSTOM -> NeutralThemeColors.IconPosparto
}

// ============ CORES LEGADAS (para compatibilidade) ============
// Essas são usadas como padrão quando não há tema definido

val Primary = GirlThemeColors.Primary
val PrimaryContainer = GirlThemeColors.PrimaryContainer
val OnPrimary = GirlThemeColors.OnPrimary
val OnPrimaryContainer = GirlThemeColors.OnPrimaryContainer

val Secondary = GirlThemeColors.Secondary
val SecondaryContainer = GirlThemeColors.SecondaryContainer
val OnSecondary = GirlThemeColors.OnSecondary
val OnSecondaryContainer = GirlThemeColors.OnSecondaryContainer

val Tertiary = GirlThemeColors.Tertiary
val TertiaryContainer = GirlThemeColors.TertiaryContainer
val OnTertiary = GirlThemeColors.OnTertiary
val OnTertiaryContainer = GirlThemeColors.OnTertiaryContainer

val Background = Color(0xFFFFFBFE)
val OnBackground = Color(0xFF1C1B1F)
val Surface = Color(0xFFFFFBFE)
val OnSurface = Color(0xFF1C1B1F)
val SurfaceVariant = Color(0xFFF5E9EF)
val OnSurfaceVariant = Color(0xFF4A4458)

val Outline = Color(0xFFE0D4DA)
val OutlineVariant = Color(0xFFCAC4D0)

val Error = Color(0xFFB3261E)
val OnError = Color(0xFFFFFFFF)

// Cores específicas dos cards (legado)
val CardMaternidade = GirlThemeColors.CardMaternidade
val CardPrenatal = GirlThemeColors.CardPrenatal
val CardPosparto = GirlThemeColors.CardPosparto

// Cores de ícones (legado)
val IconMaternidade = GirlThemeColors.IconMaternidade
val IconPrenatal = GirlThemeColors.IconPrenatal
val IconPosparto = GirlThemeColors.IconPosparto
