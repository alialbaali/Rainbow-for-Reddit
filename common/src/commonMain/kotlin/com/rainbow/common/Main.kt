package com.rainbow.common

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import com.rainbow.common.login.LoginScreen
import com.rainbow.common.navigation.Screen
import com.rainbow.common.settings.SettingsModel

@Composable
fun App() {
    val isUserLoggedIn by SettingsModel.isUserLoggedIn.collectAsState()
    if (isUserLoggedIn)
        ContentScreen()
    else
        LoginScreen()
}

@Composable
private fun ContentScreen() {
    var screen by remember { mutableStateOf<Screen>(Screen.SidebarItem.Home) }
    val backStack by remember { mutableStateOf<MutableList<Screen>>(mutableListOf()) }
    val forwardStack by remember { mutableStateOf<MutableList<Screen>>(mutableListOf()) }
    val stateHolder = rememberSaveableStateHolder()
    Crossfade(screen) { animatedScreen ->
//        stateHolder.SaveableStateProvider(animatedScreen) {
            Rainbow(
                screen = animatedScreen,
                sidebarItem = screen as? Screen.SidebarItem
                    ?: backStack.lastOrNull { it is Screen.SidebarItem } as Screen.SidebarItem,
                onSidebarClick = { sidebarItem ->
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
            )
//        }
    }
}