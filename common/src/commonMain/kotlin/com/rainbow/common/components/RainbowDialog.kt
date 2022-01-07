package com.rainbow.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Window
//import androidx.compose.ui.window.WindowPlacement
//import androidx.compose.ui.window.WindowState
//import androidx.compose.ui.window.rememberWindowState
import com.rainbow.common.utils.RainbowIcons
import com.rainbow.common.utils.RainbowStrings

@Composable
fun RainbowDialog(
    onCloseRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
//    state: WindowState = rememberWindowState(WindowPlacement.Maximized),
    visible: Boolean = true,
    enabled: Boolean = true,
    focusable: Boolean = true,
    alwaysOnTop: Boolean = false,
    onPreviewKeyEvent: ((KeyEvent) -> Boolean) = { false },
    onKeyEvent: ((KeyEvent) -> Boolean) = { false },
    content: @Composable ColumnScope.() -> Unit,
) {
//    Window(
//         TODO Replace with layer dialog when it's released.
//        onCloseRequest,
//        state,
//        visible,
//        title,
//        icon = null,
//        undecorated = true,
//        transparent = true,
//        resizable = false,
//        enabled,
//        focusable,
//        alwaysOnTop,
//        onPreviewKeyEvent,
//        onKeyEvent,
//    ) {
//        Box(
//            Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colors.onBackground.copy(0.5F)),
//            contentAlignment = Alignment.Center
//        ) {
//            Column(
//                modifier = Modifier
//                    .width(250.dp)
//                    .clip(MaterialTheme.shapes.large)
//                    .background(MaterialTheme.colors.surface),
//                verticalArrangement = Arrangement.SpaceBetween,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Row(
//                    Modifier
//                        .fillMaxWidth()
//                        .background(MaterialTheme.colors.background)
//                        .padding(horizontal = 16.dp, vertical = 8.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically,
//                ) {
//                    Text(title, fontWeight = FontWeight.Medium)
//                    IconButton(onCloseRequest) {
//                        Icon(RainbowIcons.Close, RainbowStrings.Close)
//                    }
//                }
//                Column(
//                    modifier = modifier.padding(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    content()
//                }
//            }
//        }
//    }
}
