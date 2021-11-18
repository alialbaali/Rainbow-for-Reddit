package com.rainbow.app

import androidx.compose.runtime.*
import androidx.compose.ui.window.application
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.replaceCurrent
import com.rainbow.app.components.RainbowWindow
import com.rainbow.app.login.LoginScreen
import com.rainbow.app.navigation.Screen
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
                router.navigate { screens ->
                    screens.filterNot { it is Screen.SidebarItem } + it
                }
            },
            onUserNameClick = { userName ->
                if ((child.configuration as? Screen.User)?.userName != userName) {
                    router.push(Screen.User(userName))
                    forwardStack.clear()
                }
            },
            onSubredditNameClick = { subredditName ->
                if ((child.configuration as? Screen.Subreddit)?.subredditName != subredditName) {
                    router.push(Screen.Subreddit(subredditName))
                    forwardStack.clear()
                }
            },
            onSearchClick = {
                if ((child.configuration as? Screen.Search) == null) {
                    router.push(Screen.Search(it))
                    forwardStack.clear()
                } else {
                    router.replaceCurrent(Screen.Search(it))
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