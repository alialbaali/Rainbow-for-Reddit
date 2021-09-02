package com.rainbow.app.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class DpDimensions(
    val extraSmall: Dp = 2.dp,
    val small: Dp = 4.dp,
    val medium: Dp = 8.dp,
    val large: Dp = 16.dp,
    val extraLarge: Dp = 24.dp,
)

private val LocalDpDimensions = staticCompositionLocalOf { DpDimensions() }

val MaterialTheme.dpDimensions: DpDimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalDpDimensions.current

data class SpDimensions(
    val extraTiny: TextUnit = 10.sp,
    val tiny: TextUnit = 12.sp,
    val extraSmall: TextUnit = 14.sp,
    val small: TextUnit = 16.sp,
    val medium: TextUnit = 18.sp,
    val large: TextUnit = 20.sp,
    val extraLarge: TextUnit = 22.sp,
    val huge: TextUnit = 24.sp,
    val extraHuge: TextUnit = 26.sp,
)

private val LocalSpDimensions = staticCompositionLocalOf { SpDimensions() }

val MaterialTheme.spDimensions: SpDimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalSpDimensions.current