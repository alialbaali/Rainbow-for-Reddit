package com.rainbow.app

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.window.application
import com.rainbow.app.components.RainbowWindow
import com.rainbow.app.login.LoginScreen
import com.rainbow.app.navigation.Screen
import com.rainbow.app.settings.SettingsModel
import com.rainbow.app.utils.RainbowStrings

fun main() = application {
    val isUserLoggedIn by SettingsModel.isUserLoggedIn.collectAsState()
    RainbowWindow(RainbowStrings.Rainbow) {
        if (isUserLoggedIn)
            ContentScreen()
        else
            LoginScreen()
    }
}

@Composable
private fun ContentScreen() {
    var screen by remember { mutableStateOf<Screen>(Screen.SidebarItem.Home) }
    val backStack by remember { mutableStateOf<MutableList<Screen>>(mutableListOf()) }
    val forwardStack by remember { mutableStateOf<MutableList<Screen>>(mutableListOf()) }
    val stateHolder = rememberSaveableStateHolder()
    Crossfade(screen) { animatedScreen ->
        stateHolder.SaveableStateProvider(animatedScreen) {
            Rainbow(
                screen = animatedScreen,
                backStack = backStack,
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
        }
    }
}