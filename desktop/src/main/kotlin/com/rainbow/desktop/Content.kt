package com.rainbow.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.home.HomeScreen
import com.rainbow.desktop.message.MessagesScreen
import com.rainbow.desktop.navigation.ContentScreen
import com.rainbow.desktop.navigation.Screen
import com.rainbow.desktop.navigation.Sidebar
import com.rainbow.desktop.post.PostScreen
import com.rainbow.desktop.profile.ProfileScreen
import com.rainbow.desktop.search.SearchScreen
import com.rainbow.desktop.settings.SettingsScreen
import com.rainbow.desktop.subreddit.CurrentUserSubredditsScreen
import com.rainbow.desktop.subreddit.SubredditScreen
import com.rainbow.desktop.ui.dpDimensions
import com.rainbow.desktop.user.UserScreen

@Composable
internal fun Content(
    screen: Screen,
    contentScreen: ContentScreen,
    onNavigate: (Screen) -> Unit,
    onNavigateContentScreen: (ContentScreen) -> Unit,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit,
    isBackEnabled: Boolean,
    isForwardEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }
    Box(modifier.background(MaterialTheme.colorScheme.surface)) {
        Row(Modifier.fillMaxSize()) {
            val navigateItem = remember(screen) {
                screen as? Screen.NavigationItem
                    ?: RainbowStateHolder.backStack.value
                        .lastOrNull { it is Screen.NavigationItem } as Screen.NavigationItem
            }
            StartContent(
                navigateItem,
                onNavigate,
                Modifier
                    .wrapContentWidth(unbounded = true)
                    .fillMaxHeight(),
            )
            Column(Modifier.fillMaxSize()) {
                RainbowTopAppBar(
                    screen,
                    {},
                    { },
                    onBackClick,
                    onForwardClick,
                    isBackEnabled,
                    isForwardEnabled,
                    RainbowStateHolder::refreshContent,
                )
                Row(
                    Modifier
                        .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.extraLarge)
                        .fillMaxSize()
                        .padding(horizontal = MaterialTheme.dpDimensions.extraLarge),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CenterContent(
                        screen,
                        onNavigate,
                        onNavigateContentScreen,
                        onShowSnackbar = { snackbarMessage = it },
                        Modifier.weight(1F),
                    )
                    EndContent(
                        contentScreen,
                        onNavigate,
                        onShowSnackbar = { snackbarMessage = it },
                        Modifier.weight(1F),
                    )
                }
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
private fun StartContent(
    navigationItem: Screen.NavigationItem,
    onNavigationItemClick: (Screen.NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Sidebar(
        navigationItem,
        onNavigationItemClick,
        modifier
    )
}

@Composable
private fun CenterContent(
    screen: Screen,
    onNavigate: (Screen) -> Unit,
    onNavigateContentScreen: (ContentScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (screen) {
        is Screen.NavigationItem -> when (screen) {
            Screen.NavigationItem.Profile -> ProfileScreen(
                onNavigate,
                onNavigateContentScreen,
                onShowSnackbar,
                modifier,
            )

            Screen.NavigationItem.Home -> HomeScreen(
                onNavigate,
                onNavigateContentScreen,
                onShowSnackbar,
                modifier,
            )

            Screen.NavigationItem.Subreddits -> CurrentUserSubredditsScreen(
                onNavigate,
                onShowSnackbar,
            )

            Screen.NavigationItem.Messages -> MessagesScreen(
                onNavigate,
                onNavigateContentScreen,
                onShowSnackbar,
                modifier,
            )

            Screen.NavigationItem.Settings -> SettingsScreen(Modifier.fillMaxWidth(0.5F))
        }

        is Screen.Subreddit -> SubredditScreen(
            screen.subredditName,
            onNavigate,
            onNavigateContentScreen,
            onShowSnackbar,
            modifier,
        )

        is Screen.User -> UserScreen(
            screen.userName,
            onNavigate,
            onNavigateContentScreen,
            onShowSnackbar,
            modifier,
        )

        is Screen.Search -> SearchScreen(
            screen.searchTerm,
            onNavigate,
            onNavigateContentScreen,
            onShowSnackbar,
            modifier,
        )
    }
}


@Composable
private fun EndContent(
    contentScreen: ContentScreen,
    onNavigate: (Screen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (contentScreen) {
        is ContentScreen.None -> {
            Box(modifier) {
                RainbowProgressIndicator()
            }
        }

        is ContentScreen.Post -> {
            PostScreen(
                contentScreen.postId,
                onNavigate,
                onShowSnackbar,
                modifier,
            )
        }

        is ContentScreen.Message -> TODO()
    }
//    val isMessageScreen = messageModelState
//        .map { it.message.value.type is Message.Type.Message }
//        .getOrDefault(false)
//    when {
//        screen is Screen.NavigationItem.Messages && isMessageScreen ->
//            messageModelState.composed(onShowSnackbar, modifier) { model ->
//                MessageScreen(
//                    model,
//                    onUserNameClick,
//                    onSubredditNameClick,
//                    modifier
//                )
//            }
//
//        screen is Screen.NavigationItem.Settings -> {}
//        screen is Screen.NavigationItem.Subreddits -> {}
//        else -> postScreenModelType.composed(onShowSnackbar, modifier) { type ->
//            PostScreen(
//                type,
//                onUserNameClick,
//                onSubredditNameClick,
//                onPostUpdate,
//                onCommentUpdate,
//                { model -> CommentActions(model) },
//                onShowSnackbar,
//                modifier
//            )
//        }
//    }
}