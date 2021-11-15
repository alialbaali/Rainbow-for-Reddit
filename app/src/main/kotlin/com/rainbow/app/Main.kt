package com.rainbow.app

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.application
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.router.push
import com.rainbow.app.components.RainbowWindow
import com.rainbow.app.login.LoginScreen
import com.rainbow.app.navigation.Screen
import com.rainbow.app.navigation.rememberRouter
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.data.Repos

fun main() = application {

    val router = rememberRouter(
        initialConfiguration = { Screen.SidebarItem.Home },
        configurationClass = Screen::class,
    )
    val isUserLoggedIn by Repos.User.isUserLoggedIn.collectAsState(false)
    val forwardStack by remember { mutableStateOf<MutableList<Screen>>(mutableListOf()) }

    RainbowWindow(RainbowStrings.Rainbow) {
        if (isUserLoggedIn)
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
                        router.push(Screen.User(userName))
                        forwardStack.clear()
                    },
                    onSubredditNameClick = { subredditName ->
                        router.push(Screen.Subreddit(subredditName))
                        forwardStack.clear()
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
        else
            LoginScreen()
    }
}