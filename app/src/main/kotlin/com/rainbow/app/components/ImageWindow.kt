package com.rainbow.app.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.rememberWindowState
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.app.utils.RainbowStrings
import io.kamel.core.Resource
import io.kamel.image.KamelImage

@Composable
fun ImageWindow(
    resource: Resource<Painter>,
    isVisible: Boolean,
    onCloseRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(isVisible, modifier) {
        val windowState = rememberWindowState(WindowPlacement.Maximized)
        Window(
            onCloseRequest = { onCloseRequest() },
            state = windowState,
            transparent = true,
            undecorated = true,
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clickable { onCloseRequest() }
                    .background(Color.Black.copy(0.5F), MaterialTheme.shapes.medium)
            ) {
                KamelImage(
                    resource,
                    contentDescription = null,
                    Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = { onCloseRequest() },
                    Modifier
                        .wrapContentSize()
                        .align(Alignment.TopEnd),
                ) {
                    Icon(RainbowIcons.Close, RainbowStrings.Close, tint = Color.White)
                }
            }
        }
    }
}