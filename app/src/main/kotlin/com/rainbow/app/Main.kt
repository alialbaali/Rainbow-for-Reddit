package com.rainbow.app

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.rainbow.app.components.RainbowTopAppBar
import com.rainbow.app.components.RainbowWindow
import com.rainbow.app.message.MessageScreen
import com.rainbow.app.message.MessagesScreen
import com.rainbow.app.navigation.Screen
import com.rainbow.app.navigation.rememberRouter
import com.rainbow.app.post.PostScreen
import com.rainbow.app.profile.ProfileScreen
import com.rainbow.app.settings.SettingsScreen
import com.rainbow.app.sidebar.Sidebar
import com.rainbow.app.subreddit.CurrentUserSubredditsScreen
import com.rainbow.app.subreddit.SubredditScreen
import com.rainbow.app.user.UserScreen
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.composed
import com.rainbow.data.Repos
import com.rainbow.domain.models.Message
import com.rainbow.domain.models.Post

fun main() = application {

    val isUserLoggedIn by Repos.User.isUserLoggedIn.collectAsState(false)

    val router = rememberRouter(
        initialConfiguration = { Screen.SidebarItem.Home },
        configurationClass = Screen::class,
    )
    var isExpanded by remember { mutableStateOf(true) }
    var backStack by remember { mutableStateOf<List<Screen>>(listOf(Screen.SidebarItem.Home)) }
    var forwardStack by remember { mutableStateOf<List<Screen>>(listOf(Screen.SidebarItem.Home)) }
    var post by remember { mutableStateOf<UIState<Post>>(UIState.Loading) }
    var message by remember { mutableStateOf<UIState<Message>>(UIState.Loading) }

//    if (isUserLoggedIn)
    RainbowWindow(RainbowStrings.Rainbow) {
        Children(router.state) { child ->
            var topAppBarTitle by remember { mutableStateOf(child.configuration::class.simpleName!!) }
            Column(Modifier.background(MaterialTheme.colors.background)) {
                RainbowTopAppBar(
                    topAppBarTitle,
                    onSidebarClick = { isExpanded = !isExpanded },
                    { router.pop() },
                    { forwardStack.firstOrNull()?.let { router.push(it) } },
                )
                Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Sidebar(
                        backStack.last { it is Screen.SidebarItem } as Screen.SidebarItem,
                        isExpanded,
                        onClick = {
                            router.navigate { screens ->
                                (screens.filterNot { it is Screen.SidebarItem } + it).also {
                                    backStack = it
                                }
                            }
                        },
                        modifier = Modifier
                            .animateContentSize(spring(stiffness = Spring.StiffnessLow))
                            .wrapContentWidth(unbounded = true)
                            .fillMaxHeight(),
                    )
                    when (val screen = child.configuration) {
                        is Screen.SidebarItem -> {
                            when (screen) {
                                Screen.SidebarItem.Profile -> ProfileScreen(
                                    onPostClick = {
                                        post = UIState.Success(it)
                                    },
                                    onUserNameClick = {
                                        router.push(Screen.User(it))
                                        topAppBarTitle = it
                                    },
                                    onSubredditNameClick = {
                                        router.push(Screen.Subreddit(it))
                                        topAppBarTitle = it
                                    },
                                    modifier = Modifier.weight(1F),
                                )
                                Screen.SidebarItem.Home -> HomeScreen(
                                    onPostClick = {
                                        post = UIState.Success(it)
                                    },
                                    onUserNameClick = { userName ->
                                        router.push(Screen.User(userName))
                                    },
                                    onSubredditNameClick = { subredditName ->
                                        router.push(Screen.Subreddit(subredditName))
                                    },
                                    modifier = Modifier.weight(1F),
                                )
                                Screen.SidebarItem.Subreddits -> CurrentUserSubredditsScreen(
                                    onClick = { subreddit ->
                                        router.push(Screen.Subreddit(subreddit.name))
                                    }
                                )
                                Screen.SidebarItem.Messages -> MessagesScreen(
                                    onMessageClick = { message = UIState.Success(it) },
                                    onUserNameClick = { userName -> router.push(Screen.User(userName)) },
                                    Modifier.weight(1F),
                                )
                                Screen.SidebarItem.Settings -> SettingsScreen()
                            }
                        }
                        is Screen.Subreddit -> {
                            topAppBarTitle = screen.subredditName
                            SubredditScreen(
                                subredditName = screen.subredditName,
                                onPostClick = {
                                    post = UIState.Success(it)
                                },
                                onUserNameClick = {
                                    router.push(Screen.User(it))
                                    topAppBarTitle = it
                                },
                                onSubredditNameClick = {
                                    router.push(Screen.Subreddit(it))
                                    topAppBarTitle = it
                                },
                                Modifier.weight(1F),
                            )
                        }
                        is Screen.User -> {
                            topAppBarTitle = screen.userName
                            UserScreen(
                                userName = screen.userName,
                                onPostClick = {
                                    post = UIState.Success(it)
                                },
                                onUserNameClick = {
                                    router.push(Screen.User(it))
                                    topAppBarTitle = it
                                },
                                onSubredditNameClick = {
                                    router.push(Screen.Subreddit(it))
                                    topAppBarTitle = it
                                },
                                Modifier.weight(1F),
                            )
                        }
                    }
                    if (child.configuration != Screen.SidebarItem.Messages)
                        post.composed(Modifier.weight(1F)) { post ->
                            PostScreen(
                                post,
                                Modifier.weight(1F),
                                onUserNameClick = {
                                    router.push(Screen.User(it))
                                    topAppBarTitle = it
                                },
                                onSubredditNameClick = {
                                    router.push(Screen.Subreddit(it))
                                    topAppBarTitle = it
                                },
                            )
                        }
                    else
                        message.composed(Modifier.weight(1F)) { message ->
                            MessageScreen(message, Modifier.weight(1F))
                        }
                }
            }
        }
    }
//    else
//        LoginScreen()
}