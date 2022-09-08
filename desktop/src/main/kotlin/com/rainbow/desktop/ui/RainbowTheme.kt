package com.rainbow.desktop.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.rainbow.desktop.settings.SettingsStateHolder
import com.rainbow.domain.models.Theme

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

    MaterialTheme(colors, typography = typography, content = content)
}

object RainbowTheme {

    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val dpDimensions: DpDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalDpDimensions.current

}

private val LocalDpDimensions = staticCompositionLocalOf { DpDimensions() }
private val LocalColors = staticCompositionLocalOf { Colors() }

val MaterialTheme.dpDimensions: DpDimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalDpDimensions.current