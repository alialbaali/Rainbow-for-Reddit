package com.rainbow.app.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import com.rainbow.app.RainbowModel
import com.rainbow.app.ui.RainbowTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ApplicationScope.RainbowWindow(
    title: String,
    state: WindowState = WindowState(size = DpSize(1920.dp, 1080.dp)),
    content: @Composable () -> Unit,
) = Window(
    onCloseRequest = { exitApplication() },
    state = state,
    title = title,
    onPreviewKeyEvent = {
        if (it.isRefreshPressed) {
            RainbowModel.refreshContent()
            true
        } else {
            false
        }
    }
) {
    RainbowTheme(content = content)
}

@OptIn(ExperimentalComposeUiApi::class)
private val KeyEvent.isRefreshPressed
    get() = isCtrlPressed && key == Key.R