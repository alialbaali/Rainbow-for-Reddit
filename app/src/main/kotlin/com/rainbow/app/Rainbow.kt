package com.rainbow.app

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.rainbow.app.components.RainbowTopAppBar
import com.rainbow.app.message.MessageScreen
import com.rainbow.app.message.MessagesScreen
import com.rainbow.app.navigation.Screen
import com.rainbow.app.post.PostScreen
import com.rainbow.app.profile.ProfileScreen
import com.rainbow.app.search.SearchScreen
import com.rainbow.app.settings.SettingsScreen
import com.rainbow.app.sidebar.Sidebar
import com.rainbow.app.subreddit.CurrentUserSubredditsScreen
import com.rainbow.app.subreddit.SubredditScreen
import com.rainbow.app.user.UserScreen
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.composed
import com.rainbow.data.Repos
import com.rainbow.domain.models.Message
import com.rainbow.domain.models.Post
import kotlinx.coroutines.launch

@Composable
fun Rainbow(
    screen: Screen,
    backStack: List<Screen>,
    onSidebarClick: (Screen.SidebarItem) -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onSearchClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit,
    isBackEnabled: Boolean,
    isForwardEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    var post by remember { mutableStateOf<UIState<Post>>(UIState.Loading) }
    var message by remember { mutableStateOf<UIState<Message>>(UIState.Loading) }
    val scope = rememberCoroutineScope()
    val isSidebarExpanded by Repos.Settings.isSidebarExpanded.collectAsState(true)
    val focusRequester = remember { FocusRequester() }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }
    Box(modifier.background(MaterialTheme.colors.background)) {
        Column {
            RainbowTopAppBar(
                screen,
                onSearchClick,
                onSidebarClick = {
                    scope.launch {
                        Repos.Settings.setIsSidebarExpanded(!isSidebarExpanded)
                    }
                },
                onSubredditNameClick,
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
                    focusRequester,
                    onPostClick = {
                        post = UIState.Success(it)
                        scope.launch {
                            Repos.Post.readPost(it.id)
                        }
                    },
                    onUserNameClick,
                    onSubredditNameClick,
                    onMessageClick = { message = UIState.Success(it) },
                    onShowSnackbar = { snackbarMessage = it },
                    Modifier.weight(1F),
                )
                EndContent(
                    screen,
                    post,
                    message,
                    focusRequester,
                    onUserNameClick,
                    onSubredditNameClick,
                    onShowSnackbar = { snackbarMessage = it },
                    Modifier.weight(1F),
                )
            }
        }
        SnackbarHost(
            snackbarHostState,
            Modifier.align(Alignment.BottomCenter),
        ) {
            Snackbar(it, Modifier.fillMaxWidth(0.5F))
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
    focusRequester: FocusRequester,
    onPostClick: (Post) -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onMessageClick: (Message) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (screen) {
        is Screen.SidebarItem -> {
            when (screen) {
                Screen.SidebarItem.Profile -> ProfileScreen(
                    focusRequester,
                    onPostClick,
                    onUserNameClick,
                    onSubredditNameClick,
                    onShowSnackbar,
                    modifier,
                )
                Screen.SidebarItem.Home -> HomeScreen(
                    focusRequester,
                    onPostClick,
                    onUserNameClick,
                    onSubredditNameClick,
                    onShowSnackbar,
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
                focusRequester,
                onPostClick,
                onUserNameClick,
                onSubredditNameClick,
                onShowSnackbar,
                modifier,
            )
        }
        is Screen.User -> {
            UserScreen(
                userName = screen.userName,
                focusRequester,
                onPostClick,
                onUserNameClick,
                onSubredditNameClick,
                onShowSnackbar,
                modifier,
            )
        }
        is Screen.Search -> SearchScreen(
            screen.searchTerm,
            focusRequester,
            onPostClick,
            onUserNameClick,
            onSubredditNameClick,
            onShowSnackbar,
            modifier
        )
    }
}


@Composable
private fun RowScope.EndContent(
    screen: Screen,
    post: UIState<Post>,
    message: UIState<Message>,
    focusRequester: FocusRequester,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (screen) {
        Screen.SidebarItem.Messages -> message.composed(modifier) { MessageScreen(it, modifier) }
        else -> post.composed(modifier) {
            PostScreen(
                it,
                focusRequester,
                onUserNameClick,
                onSubredditNameClick,
                onShowSnackbar,
                modifier
            )
        }
    }
}