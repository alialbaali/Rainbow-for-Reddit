package com.rainbow.desktop

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
internal fun App() {
    val mainScreen by RainbowStateHolder.mainScreen.collectAsState()
    val sidebarItem by RainbowStateHolder.sidebarItem.collectAsState()
    val detailsScreen by RainbowStateHolder.detailsScreen.collectAsState()
    val backStack by RainbowStateHolder.backStack.collectAsState()
    val forwardStack by RainbowStateHolder.forwardStack.collectAsState()
    Content(
        mainScreen = mainScreen,
        detailsScreen = detailsScreen,
        sidebarItem = sidebarItem,
        onNavigateMainScreen = RainbowStateHolder::navigateToMainScreen,
        onNavigateDetailsScreen = RainbowStateHolder::navigateToDetailsScreen,
        onBackClick = RainbowStateHolder::navigateBack,
        onForwardClick = RainbowStateHolder::navigateForward,
        isBackEnabled = backStack.isNotEmpty(),
        isForwardEnabled = forwardStack.isNotEmpty(),
        modifier = Modifier.fillMaxSize(),
    )
}