package com.rainbow.desktop.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.*
import java.awt.Dimension

private val MinWindowSize = Dimension(1920, 1080)

@Composable
fun ApplicationScope.RainbowWindow(
    title: String,
    onRefreshPressed: () -> Unit,
    state: WindowState = rememberWindowState(
        placement = WindowPlacement.Maximized,
        position = WindowPosition(Alignment.Center),
    ),
    content: @Composable FrameWindowScope.() -> Unit,
) = Window(
    onCloseRequest = ::exitApplication,
    state = state,
    title = title,
    onPreviewKeyEvent = { event ->
        if (event.isRefreshPressed) {
            onRefreshPressed()
            true
        } else {
            false
        }
    },
    icon = painterResource("icons/Rainbow.svg")
) {
    window.minimumSize = MinWindowSize
    content()
}

@OptIn(ExperimentalComposeUiApi::class)
private val KeyEvent.isRefreshPressed
    get() = isMetaPressed && key == Key.R