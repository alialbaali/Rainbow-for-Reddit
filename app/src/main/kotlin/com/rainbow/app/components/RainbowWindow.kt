package com.rainbow.app.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import com.rainbow.app.ui.RainbowTheme

@Composable
fun ApplicationScope.RainbowWindow(
    title: String,
    state: WindowState = WindowState(),
    content: @Composable () -> Unit
) = Window(
    onCloseRequest = { exitApplication() },
    state = state,
    title = title
) {
    RainbowTheme(content = content)
}