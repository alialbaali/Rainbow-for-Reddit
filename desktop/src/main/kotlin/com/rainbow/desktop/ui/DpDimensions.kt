package com.rainbow.desktop.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class DpDimensions(
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp,
)

val DpDimensions.headerImageBorder
    get() = small