package com.rainbow.app.ui

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.rainbow.data.Repos
import com.rainbow.domain.models.Theme

@Composable
fun RainbowTheme(content: @Composable () -> Unit) {
    val theme by Repos.Settings.theme.collectAsState(Theme.System)

    val colors = when (theme) {
        Theme.Dark -> darkColors
        Theme.Light -> lightColors
        Theme.System -> if (isSystemInDarkTheme())
            lightColors
        else
            darkColors
    }

    DesktopMaterialTheme(
        colors,
        typography,
        shapes,
        content
    )
}