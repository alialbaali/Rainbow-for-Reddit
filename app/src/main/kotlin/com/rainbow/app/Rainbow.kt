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
import com.rainbow.app.components.RainbowTopAppBar
import com.rainbow.app.message.MessageScreen
import com.rainbow.app.message.MessagesScreen
import com.rainbow.app.navigation.Screen
import com.rainbow.app.post.PostScreen
import com.rainbow.app.profile.ProfileScreen
import com.rainbow.app.settings.SettingsScreen
import com.rainbow.app.sidebar.Sidebar
import com.rainbow.app.subreddit.CurrentUserSubredditsScreen
import com.rainbow.app.subreddit.SubredditScreen
import com.rainbow.app.user.UserScreen
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.composed
import com.rainbow.domain.models.Message
import com.rainbow.domain.models.Post

@Composable
fun Rainbow(
    screen: Screen,
    backStack: List<Screen>,
    onSidebarClick: (Screen.SidebarItem) -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit,
    isBackEnabled: Boolean,
    isForwardEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    var post by remember { mutableStateOf<UIState<Post>>(UIState.Loading) }
    var message by remember { mutableStateOf<UIState<Message>>(UIState.Loading) }
    var isSidebarExpanded by remember { mutableStateOf(true) }
    Column(modifier.background(MaterialTheme.colors.background)) {
        RainbowTopAppBar(
            screen,
            onSidebarClick = { isSidebarExpanded = !isSidebarExpanded },
            onBackClick,
            onForwardClick,
            isBackEnabled,
            isForwardEnabled,
        )
        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StartContent(
                screen,
                backStack,
                isSidebarExpanded,
                onSidebarClick,
                Modifier
                    .wrapContentWidth(unbounded = true)
                    .fillMaxHeight(),
            )
            CenterContent(
                screen,
                onPostClick = { post = UIState.Success(it) },
                onUserNameClick,
                onSubredditNameClick,
                onMessageClick = { message = UIState.Success(it) },
                Modifier.weight(1F),
            )
            EndContent(
                screen,
                post,
                message,
                onUserNameClick,
                onSubredditNameClick,
                Modifier.weight(1F)
            )
        }
    }
}

@Composable
private fun RowScope.StartContent(
    screen: Screen,
    backStack: List<Screen>,
    isExpanded: Boolean,
    onSidebarClick: (Screen.SidebarItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Sidebar(
        screen as? Screen.SidebarItem ?: backStack.last { it is Screen.SidebarItem } as Screen.SidebarItem,
        isExpanded,
        onSidebarClick,
        modifier
            .animateContentSize(spring(stiffness = Spring.StiffnessLow)),
    )
}

@Composable
private fun RowScope.CenterContent(
    screen: Screen,
    onPostClick: (Post) -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onMessageClick: (Message) -> Unit,
    modifier: Modifier = Modifier
) {
    when (screen) {
        is Screen.SidebarItem -> {
            when (screen) {
                Screen.SidebarItem.Profile -> ProfileScreen(
                    onPostClick,
                    onUserNameClick,
                    onSubredditNameClick,
                    modifier,
                )
                Screen.SidebarItem.Home -> HomeScreen(
                    onPostClick,
                    onUserNameClick,
                    onSubredditNameClick,
                    modifier,
                )
                Screen.SidebarItem.Subreddits -> CurrentUserSubredditsScreen(onSubredditNameClick)
                Screen.SidebarItem.Messages -> MessagesScreen(
                    onMessageClick,
                    onUserNameClick,
                    modifier,
                )
                Screen.SidebarItem.Settings -> SettingsScreen()
            }
        }
        is Screen.Subreddit -> {
            SubredditScreen(
                subredditName = screen.subredditName,
                onPostClick,
                onUserNameClick,
                onSubredditNameClick,
                modifier,
            )
        }
        is Screen.User -> {
            UserScreen(
                userName = screen.userName,
                onPostClick,
                onUserNameClick,
                onSubredditNameClick,
                modifier,
            )
        }
    }
}


@Composable
private fun RowScope.EndContent(
    screen: Screen,
    post: UIState<Post>,
    message: UIState<Message>,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (screen) {
        Screen.SidebarItem.Messages -> message.composed(modifier) { MessageScreen(it, modifier) }
        else -> post.composed(modifier) { PostScreen(it, onUserNameClick, onSubredditNameClick, modifier) }
    }
}