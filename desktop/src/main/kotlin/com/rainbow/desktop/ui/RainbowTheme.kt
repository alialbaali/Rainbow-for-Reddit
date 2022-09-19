package com.rainbow.desktop.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Shapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.rainbow.desktop.settings.SettingsStateHolder
import com.rainbow.domain.models.Theme

// A workaround to override DropdownMenu shape.
private val material2Shapes = Shapes(
    small = shapes.small,
    medium = shapes.medium,
    large = shapes.large,
)

@Composable
fun RainbowTheme(content: @Composable () -> Unit) {
    val theme by SettingsStateHolder.theme.collectAsState()

    val colors = when (theme) {
        Theme.Dark -> darkColors
        Theme.Light -> lightColors
        Theme.System -> if (isSystemInDarkTheme())
            darkColors
        else
            lightColors
    }

    androidx.compose.material.MaterialTheme(shapes = material2Shapes) {
        MaterialTheme(colors, shapes, typography, content)
    }
}

object RainbowTheme {

    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val dimensions: Dimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalDimensions.current

}

private val LocalDimensions = staticCompositionLocalOf { Dimensions() }
private val LocalColors = staticCompositionLocalOf { Colors() }

val MaterialTheme.dimensions: Dimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalDimensions.current