package com.rainbow.app.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableContract
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    val extraSmall: Dp = 2.dp,
    val small: Dp = 4.dp,
    val medium: Dp = 8.dp,
    val large: Dp = 16.dp,
    val extraLarge: Dp = 24.dp,
)

private val AmbientDimensions = staticAmbientOf { Dimensions() }

val MaterialTheme.dimensions: Dimensions
    @Composable
    @ComposableContract(readonly = true)
    get() = AmbientDimensions.current