package com.rainbow.desktop

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier


@Composable
internal fun App() {
    val screen by RainbowStateHolder.screen.collectAsState()
    val backStack by RainbowStateHolder.backStack.collectAsState()
    val forwardStack by RainbowStateHolder.forwardStack.collectAsState()
    val contentScreen by RainbowStateHolder.contentScreen.collectAsState()
    val stateHolder = rememberSaveableStateHolder()
    Crossfade(screen) { animatedScreen ->
        stateHolder.SaveableStateProvider(animatedScreen) {
            Content(
                screen = animatedScreen,
                contentScreen = contentScreen,
                onNavigate = RainbowStateHolder::navigateToScreen,
                onNavigateContentScreen = RainbowStateHolder::navigateToContentScreen,
                onBackClick = RainbowStateHolder::navigateBack,
                onForwardClick = RainbowStateHolder::navigateForward,
                isBackEnabled = backStack.isNotEmpty(),
                isForwardEnabled = forwardStack.isNotEmpty(),
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}