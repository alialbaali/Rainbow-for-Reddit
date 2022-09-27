package com.rainbow.desktop.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.defaultPadding

private val LayerDialogWidth = 500.dp

// TODO Replace with layer dialog when it's released.
@Composable
fun LayerDialog(
    onCloseRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    state: WindowState = rememberWindowState(WindowPlacement.Maximized),
    visible: Boolean = true,
    enabled: Boolean = true,
    focusable: Boolean = true,
    alwaysOnTop: Boolean = false,
    onPreviewKeyEvent: ((KeyEvent) -> Boolean) = { false },
    onKeyEvent: ((KeyEvent) -> Boolean) = { false },
    content: @Composable ColumnScope.() -> Unit,
) {
    val scrimColor by animateColorAsState(
        if (visible) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
    )

    Window(
        onCloseRequest,
        state,
        visible,
        title,
        icon = null,
        undecorated = true,
        transparent = true,
        resizable = false,
        enabled,
        focusable,
        alwaysOnTop,
        onPreviewKeyEvent,
        onKeyEvent,
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(scrimColor),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .width(LayerDialogWidth)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surface)
                    .defaultPadding(),
                verticalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.medium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title(title, onCloseRequest)
                content()
            }
        }
    }
}

@Composable
private fun Title(title: String, onCloseRequest: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
        RainbowIconButton(onCloseRequest) {
            Icon(RainbowIcons.Close, RainbowStrings.Close)
        }
    }
}
