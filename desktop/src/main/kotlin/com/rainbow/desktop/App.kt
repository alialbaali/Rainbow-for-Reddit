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
    val mainScreen by RainbowStateHolder.mainScreen.collectAsState()
    val detailsScreen by RainbowStateHolder.detailsScreen.collectAsState()
    val backStack by RainbowStateHolder.backStack.collectAsState()
    val forwardStack by RainbowStateHolder.forwardStack.collectAsState()
    val stateHolder = rememberSaveableStateHolder()
    Crossfade(mainScreen) { animatedScreen ->
        stateHolder.SaveableStateProvider(animatedScreen) {
            Content(
                mainScreen = animatedScreen,
                detailsScreen = detailsScreen,
                onNavigateMainScreen = RainbowStateHolder::navigateToMainScreen,
                onNavigateDetailsScreen = RainbowStateHolder::navigateToDetailsScreen,
                onBackClick = RainbowStateHolder::navigateBack,
                onForwardClick = RainbowStateHolder::navigateForward,
                isBackEnabled = backStack.isNotEmpty(),
                isForwardEnabled = forwardStack.isNotEmpty(),
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}