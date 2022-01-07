package com.rainbow.common.ui

//import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.rainbow.common.settings.SettingsModel
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

    MaterialTheme(colors, typography, shapes, content)
}

//val DefaultScrollbarStyle
//    @Composable
//    get() = ScrollbarStyle(
//        minimalHeight = 100.dp,
//        thickness = 8.dp,
//        shape = CircleShape,
//        hoverDurationMillis = 300,
//        unhoverColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
//        hoverColor = MaterialTheme.colors.onSurface.copy(alpha = 0.50f)
//    )