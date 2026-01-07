package com.gestantes.checklist.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.gestantes.checklist.data.preferences.AppTheme

/**
 * Cria o esquema de cores baseado no tema selecionado
 */
private fun getColorScheme(appTheme: AppTheme) = when(appTheme) {
    AppTheme.GIRL -> lightColorScheme(
        primary = GirlThemeColors.Primary,
        primaryContainer = GirlThemeColors.PrimaryContainer,
        onPrimary = GirlThemeColors.OnPrimary,
        onPrimaryContainer = GirlThemeColors.OnPrimaryContainer,
        secondary = GirlThemeColors.Secondary,
        secondaryContainer = GirlThemeColors.SecondaryContainer,
        onSecondary = GirlThemeColors.OnSecondary,
        onSecondaryContainer = GirlThemeColors.OnSecondaryContainer,
        tertiary = GirlThemeColors.Tertiary,
        tertiaryContainer = GirlThemeColors.TertiaryContainer,
        onTertiary = GirlThemeColors.OnTertiary,
        onTertiaryContainer = GirlThemeColors.OnTertiaryContainer,
        background = GirlThemeColors.Background,
        onBackground = OnBackground,
        surface = GirlThemeColors.Surface,
        onSurface = OnSurface,
        surfaceVariant = GirlThemeColors.SurfaceVariant,
        onSurfaceVariant = OnSurfaceVariant,
        outline = Outline,
        outlineVariant = OutlineVariant,
        error = Error,
        onError = OnError
    )
    
    AppTheme.BOY -> lightColorScheme(
        primary = BoyThemeColors.Primary,
        primaryContainer = BoyThemeColors.PrimaryContainer,
        onPrimary = BoyThemeColors.OnPrimary,
        onPrimaryContainer = BoyThemeColors.OnPrimaryContainer,
        secondary = BoyThemeColors.Secondary,
        secondaryContainer = BoyThemeColors.SecondaryContainer,
        onSecondary = BoyThemeColors.OnSecondary,
        onSecondaryContainer = BoyThemeColors.OnSecondaryContainer,
        tertiary = BoyThemeColors.Tertiary,
        tertiaryContainer = BoyThemeColors.TertiaryContainer,
        onTertiary = BoyThemeColors.OnTertiary,
        onTertiaryContainer = BoyThemeColors.OnTertiaryContainer,
        background = BoyThemeColors.Background,
        onBackground = OnBackground,
        surface = BoyThemeColors.Surface,
        onSurface = OnSurface,
        surfaceVariant = BoyThemeColors.SurfaceVariant,
        onSurfaceVariant = OnSurfaceVariant,
        outline = Outline,
        outlineVariant = OutlineVariant,
        error = Error,
        onError = OnError
    )
    
    AppTheme.NEUTRAL, AppTheme.CUSTOM -> lightColorScheme(
        primary = NeutralThemeColors.Primary,
        primaryContainer = NeutralThemeColors.PrimaryContainer,
        onPrimary = NeutralThemeColors.OnPrimary,
        onPrimaryContainer = NeutralThemeColors.OnPrimaryContainer,
        secondary = NeutralThemeColors.Secondary,
        secondaryContainer = NeutralThemeColors.SecondaryContainer,
        onSecondary = NeutralThemeColors.OnSecondary,
        onSecondaryContainer = NeutralThemeColors.OnSecondaryContainer,
        tertiary = NeutralThemeColors.Tertiary,
        tertiaryContainer = NeutralThemeColors.TertiaryContainer,
        onTertiary = NeutralThemeColors.OnTertiary,
        onTertiaryContainer = NeutralThemeColors.OnTertiaryContainer,
        background = NeutralThemeColors.Background,
        onBackground = OnBackground,
        surface = NeutralThemeColors.Surface,
        onSurface = OnSurface,
        surfaceVariant = NeutralThemeColors.SurfaceVariant,
        onSurfaceVariant = OnSurfaceVariant,
        outline = Outline,
        outlineVariant = OutlineVariant,
        error = Error,
        onError = OnError
    )
}

@Composable
fun ChecklistGestantesTheme(
    appTheme: AppTheme = AppTheme.GIRL,
    content: @Composable () -> Unit
) {
    // Atualiza o tema atual global
    CurrentTheme = appTheme
    
    val colorScheme = getColorScheme(appTheme)
    val view = LocalView.current
    
    // Cor da status bar baseada no tema
    val statusBarColor = when(appTheme) {
        AppTheme.GIRL -> GirlThemeColors.Primary
        AppTheme.BOY -> BoyThemeColors.Primary
        AppTheme.NEUTRAL, AppTheme.CUSTOM -> NeutralThemeColors.Primary
    }
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = statusBarColor.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
