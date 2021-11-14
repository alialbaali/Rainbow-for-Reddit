package com.rainbow.app.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import com.rainbow.app.ui.RainbowTheme

@Composable
fun ApplicationScope.RainbowWindow(
    title: String,
    state: WindowState = WindowState(size = DpSize(1920.dp, 1080.dp)),
    content: @Composable () -> Unit
) = Window(
    onCloseRequest = { exitApplication() },
    state = state,
    title = title,
) {
    RainbowTheme(content = content)
}