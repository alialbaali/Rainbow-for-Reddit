package com.rainbow.desktop.components

//import androidx.compose.ui.window.Window
//import androidx.compose.ui.window.WindowPlacement
//import androidx.compose.ui.window.rememberWindowState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import io.kamel.core.Resource

@Composable
fun ImageWindow(
    resource: Resource<Painter>,
    isVisible: Boolean,
    onCloseRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
//    AnimatedVisibility(isVisible, modifier) {
//        val windowState = rememberWindowState(WindowPlacement.Maximized)
//        Window(
//            onCloseRequest = { onCloseRequest() },
//            state = windowState,
//            transparent = true,
//            undecorated = true,
//        ) {
//            Box(
//                Modifier
//                    .fillMaxSize()
//                    .clickable { onCloseRequest() }
//                    .background(Color.Black.copy(0.5F), MaterialTheme.shapes.medium)
//            ) {
//                KamelImage(
//                    resource,
//                    contentDescription = null,
//                    Modifier.fillMaxSize()
//                )
//                IconButton(
//                    onClick = { onCloseRequest() },
//                    Modifier
//                        .wrapContentSize()
//                        .align(Alignment.TopEnd),
//                ) {
//                    Icon(RainbowIcons.Close, RainbowStrings.Close, tint = Color.White)
//                }
//            }
//        }
//    }
}