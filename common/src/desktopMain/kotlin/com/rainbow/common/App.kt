package com.rainbow.common

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import com.rainbow.common.navigation.Screen


@Composable
internal actual fun App() {
    var screen by remember { mutableStateOf<Screen>(Screen.NavigationItem.Home) }
    val backStack by remember { mutableStateOf<MutableList<Screen>>(mutableListOf()) }
    val forwardStack by remember { mutableStateOf<MutableList<Screen>>(mutableListOf()) }
    val stateHolder = rememberSaveableStateHolder()
    Crossfade(screen) { animatedScreen ->
//        stateHolder.SaveableStateProvider(animatedScreen) {
        Content(
            screen = animatedScreen,
            navigationItem = screen as? Screen.NavigationItem
                ?: backStack.lastOrNull { it is Screen.NavigationItem } as Screen.NavigationItem,
            onNavigationItemClick = { sidebarItem ->
                backStack += screen
                screen = sidebarItem
                forwardStack.clear()
            },
            onUserNameClick = { userName ->
                if ((screen as? Screen.User)?.userName != userName) {
                    backStack += screen
                    screen = Screen.User(userName)
                    forwardStack.clear()
                }
            },
            onSubredditNameClick = { subredditName ->
                if ((screen as? Screen.Subreddit)?.subredditName != subredditName) {
                    backStack += screen
                    screen = Screen.Subreddit(subredditName)
                    forwardStack.clear()
                }
            },
            onSearchClick = { searchTerm ->
                if ((screen as? Screen.Search)?.searchTerm != searchTerm) {
                    backStack += screen
                    screen = Screen.Search(searchTerm)
                    forwardStack.clear()
                }
            },
            onBackClick = {
                forwardStack += screen
                screen = backStack.last()
                backStack.removeLast()
            },
            onForwardClick = {
                backStack += screen
                screen = forwardStack.last()
                forwardStack.removeLast()
            },
            isBackEnabled = backStack.isNotEmpty(),
            isForwardEnabled = forwardStack.isNotEmpty(),
            modifier = Modifier.fillMaxSize()
        )
//        }
    }
}