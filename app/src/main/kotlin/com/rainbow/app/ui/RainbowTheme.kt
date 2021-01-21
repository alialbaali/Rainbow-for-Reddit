package com.rainbow.app.ui

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun RainbowTheme(darkTheme: Boolean = isSystemInDarkMode(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) darkColors else lightColors

    DesktopMaterialTheme(
        colors,
        typography,
        shapes,
        content
    )

}

private fun isSystemInDarkMode(): Boolean = false // TODO Get real value from the current os