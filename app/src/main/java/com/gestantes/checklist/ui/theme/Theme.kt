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
 * Função auxiliar para criar o esquema de cores a partir de uma paleta
 */
private fun createColorScheme(colors: Any) = when(colors) {
    is GirlThemeColors -> lightColorScheme(
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
    else -> lightColorScheme() // Fallback
}

/**
 * Cria o esquema de cores baseado no tema selecionado
 */
private fun getColorScheme(appTheme: AppTheme) = lightColorScheme(
    primary = getPrimary(appTheme),
    primaryContainer = getPrimaryContainer(appTheme),
    onPrimary = getOnPrimary(appTheme),
    onPrimaryContainer = getOnPrimaryContainer(appTheme),
    secondary = getSecondary(appTheme),
    secondaryContainer = getSecondaryContainer(appTheme),
    onSecondary = getOnSecondary(appTheme),
    onSecondaryContainer = getOnSecondaryContainer(appTheme),
    tertiary = getTertiary(appTheme),
    tertiaryContainer = getTertiaryContainer(appTheme),
    onTertiary = getOnTertiary(appTheme),
    onTertiaryContainer = getOnTertiaryContainer(appTheme),
    background = getBackground(appTheme),
    onBackground = OnBackground,
    surface = getSurface(appTheme),
    onSurface = OnSurface,
    surfaceVariant = getSurfaceVariant(appTheme),
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant,
    error = Error,
    onError = OnError
)

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
    val statusBarColor = getPrimary(appTheme)
    
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
