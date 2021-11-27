package com.rainbow.app

import androidx.compose.runtime.*
import androidx.compose.ui.window.application
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.router.bringToFront
import com.arkivanov.decompose.router.push
import com.rainbow.app.components.RainbowWindow
import com.rainbow.app.login.LoginScreen
import com.rainbow.app.navigation.Screen
import com.rainbow.app.navigation.bringToFrontIfContentMatches
import com.rainbow.app.navigation.rememberRouter
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.data.Repos

fun main() = application {
    val isUserLoggedIn by Repos.User.isUserLoggedIn.collectAsState(false)
    RainbowWindow(RainbowStrings.Rainbow) {
        if (isUserLoggedIn)
            ContentScreen()
        else
            LoginScreen()
    }
}

@Composable
private fun ContentScreen() {
    val router = rememberRouter(
        initialConfiguration = Screen.SidebarItem.Home,
        configurationClass = Screen::class,
    )
    val forwardStack by remember { mutableStateOf<MutableList<Screen>>(mutableListOf()) }
    Children(router.state) { child ->
        Rainbow(
            screen = child.configuration,
            backStack = router.state.value.backStack.map { it.configuration },
            onSidebarClick = {
                router.bringToFront(it)
                forwardStack.clear()
            },
            onUserNameClick = { userName ->
                if ((child.configuration as? Screen.User)?.userName != userName) {
                    router.bringToFrontIfContentMatches(Screen.User(userName))
                    forwardStack.clear()
                }
            },
            onSubredditNameClick = { subredditName ->
                if ((child.configuration as? Screen.Subreddit)?.subredditName != subredditName) {
                    router.bringToFrontIfContentMatches(Screen.Subreddit(subredditName))
                    forwardStack.clear()
                }
            },
            onSearchClick = { searchTerm ->
                if ((child.configuration as? Screen.Search)?.searchTerm != searchTerm) {
                    router.bringToFrontIfContentMatches(Screen.Search(searchTerm))
                    forwardStack.clear()
                }
            },
            onBackClick = {
                router.navigate {
                    forwardStack += it.last()
                    it.dropLast(1)
                }
            },
            onForwardClick = {
                router.push(forwardStack.last())
                forwardStack.removeLast()
            },
            isBackEnabled = router.state.value.backStack.isNotEmpty(),
            isForwardEnabled = forwardStack.isNotEmpty(),
        )
    }
}