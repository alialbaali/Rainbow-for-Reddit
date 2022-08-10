package com.rainbow.desktop.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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