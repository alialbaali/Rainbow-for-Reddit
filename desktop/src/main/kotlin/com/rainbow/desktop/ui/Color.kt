package com.rainbow.desktop.ui

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color


val lightColors = lightColorScheme(
    primary = Color(0xFF26A69A),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFFFF7043),
    onSecondary = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF000000),
    background = Color(0xFFF5F5F5),
    onBackground = Color(0xFF000000),
    surfaceVariant = Color.Black.copy(0.5F),
    inverseSurface = Color(0xFF000000)
)

val darkColors = darkColorScheme(
    primary = Color(0xFF26A69A),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFFFF7043),
    onSecondary = Color(0xFFFFFFFF),
    surface = Color(0xFF000000),
    onSurface = Color(0xFFFFFFFF),
    background = Color(0xFF121212),
    onBackground = Color(0xFFFFFFFF),
    surfaceVariant = Color.White.copy(0.5F),
    inverseSurface = Color(0xFFFFFFFF),
)

val RainbowColors = listOf(
    Color(0xFF9C27B0), // Violet
    Color(0xFF3F51B5), // Indigo
    Color(0xFF2196F3), // Blue
    Color(0xFF4CAF50), // Green
    Color(0xFFFFEB3B), // Yellow
    Color(0xFFFF9800), // Orange
    Color(0xFFF44336), // Red
)

data class Colors(
    val yellow: Color = Color(0xFFFFBC02D),
)