package com.rainbow.android.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.rainbow.common.settings.SettingsModel
import com.rainbow.common.ui.darkColors
import com.rainbow.common.ui.lightColors
import com.rainbow.domain.models.Theme

@Composable
fun RainbowTheme(content: @Composable () -> Unit) {
    val theme by SettingsModel.theme.collectAsState()

    val colors = when (theme) {
        Theme.Dark -> darkColors
        Theme.Light -> lightColors
        Theme.System -> if (isSystemInDarkTheme())
            darkColors
        else
            lightColors
    }

    MaterialTheme(colors, typography, content)
}