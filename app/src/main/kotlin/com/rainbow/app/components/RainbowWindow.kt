package com.rainbow.app.components

import androidx.compose.desktop.AppManager
import androidx.compose.desktop.Window
import androidx.compose.desktop.WindowEvents
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntSize
import com.rainbow.app.ui.RainbowTheme
import java.awt.image.BufferedImage

private val WindowSize = IntSize(1330, 870)

fun RainbowWindow(
    title: String,
    size: IntSize = WindowSize,
    icon: BufferedImage? = null,
    content: @Composable () -> Unit,
) = Window(
    title,
    size,
    icon = icon,
    events = WindowEvents(onFocusGet = { AppManager.focusedWindow?.maximize() })
) {
    RainbowTheme(content = content)
}