package com.rahul.clearwalls.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class ThemeMode {
    SYSTEM, LIGHT, DARK, AMOLED
}

private val DarkColorScheme = darkColorScheme(
    primary = ClearWallsPrimary,
    secondary = ClearWallsSecondary,
    tertiary = ClearWallsTertiary,
    background = ClearWallsBackground,
    surface = ClearWallsSurface,
    surfaceVariant = ClearWallsSurfaceVariant,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = Color(0xFFCAC4D0)
)

private val AmoledColorScheme = darkColorScheme(
    primary = ClearWallsPrimary,
    secondary = ClearWallsSecondary,
    tertiary = ClearWallsTertiary,
    background = AmoledBlack,
    surface = AmoledSurface,
    surfaceVariant = AmoledSurfaceVariant,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = Color(0xFFCAC4D0)
)

private val LightColorScheme = lightColorScheme(
    primary = ClearWallsPrimaryDark,
    secondary = ClearWallsSecondary,
    tertiary = ClearWallsTertiary,
    background = ClearWallsBackgroundLight,
    surface = ClearWallsSurfaceLight,
    surfaceVariant = Color(0xFFE7E0EC),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

@Composable
fun ClearWallsTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        ThemeMode.LIGHT -> LightColorScheme
        ThemeMode.DARK -> DarkColorScheme
        ThemeMode.AMOLED -> AmoledColorScheme
        ThemeMode.SYSTEM -> if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ClearWallsTypography,
        content = content
    )
}
