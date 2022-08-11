package com.rainbow.desktop.ui

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color


val lightColors = lightColorScheme(
    primary = Color(0xFF006D77),
    primaryContainer = Color(0xFF83C5BE),
    secondary = Color(0xFFE29578),
    secondaryContainer = Color(0xFFFFDDD2),
    surface = Color(0xFFFFFFFF),
    background = Color(0xFFF5F5F5),
    surfaceVariant = Color.Black.copy(0.5F),
)

val darkColors = darkColorScheme(
    primary = Color(0xFF006D77),
    primaryContainer = Color(0xFF83C5BE),
    secondary = Color(0xFFE29578),
    secondaryContainer = Color(0xFFFFDDD2),
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
