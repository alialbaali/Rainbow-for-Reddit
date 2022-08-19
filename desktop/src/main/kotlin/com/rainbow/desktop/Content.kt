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
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
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
    mainScreen: MainScreen,
    detailsScreen: DetailsScreen,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
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
            val navigateItem = remember(mainScreen) {
                mainScreen as? MainScreen.NavigationItem
                    ?: RainbowStateHolder.backStack.value
                        .lastOrNull { it is MainScreen.NavigationItem } as MainScreen.NavigationItem
            }
            StartContent(
                navigateItem,
                onNavigateMainScreen,
                Modifier
                    .wrapContentWidth(unbounded = true)
                    .fillMaxHeight(),
            )
            Column(Modifier.fillMaxSize()) {
                RainbowTopAppBar(
                    mainScreen,
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
                        mainScreen,
                        onNavigateMainScreen,
                        onNavigateDetailsScreen,
                        onShowSnackbar = { snackbarMessage = it },
                        Modifier.weight(1F),
                    )
                    EndContent(
                        detailsScreen,
                        onNavigateMainScreen,
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
    navigationItem: MainScreen.NavigationItem,
    onNavigationItemClick: (MainScreen.NavigationItem) -> Unit,
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
    mainScreen: MainScreen,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (mainScreen) {
        is MainScreen.NavigationItem -> when (mainScreen) {
            MainScreen.NavigationItem.Profile -> ProfileScreen(
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                onShowSnackbar,
                modifier,
            )

            MainScreen.NavigationItem.Home -> HomeScreen(
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                onShowSnackbar,
                modifier,
            )

            MainScreen.NavigationItem.Subreddits -> CurrentUserSubredditsScreen(
                onNavigateMainScreen,
                onShowSnackbar,
            )

            MainScreen.NavigationItem.Messages -> MessagesScreen(
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                onShowSnackbar,
                modifier,
            )

            MainScreen.NavigationItem.Settings -> SettingsScreen(Modifier.fillMaxWidth(0.5F))
        }

        is MainScreen.Subreddit -> SubredditScreen(
            mainScreen.subredditName,
            onNavigateMainScreen,
            onNavigateDetailsScreen,
            onShowSnackbar,
            modifier,
        )

        is MainScreen.User -> UserScreen(
            mainScreen.userName,
            onNavigateMainScreen,
            onNavigateDetailsScreen,
            onShowSnackbar,
            modifier,
        )

        is MainScreen.Search -> SearchScreen(
            mainScreen.searchTerm,
            onNavigateMainScreen,
            onNavigateDetailsScreen,
            onShowSnackbar,
            modifier,
        )
    }
}


@Composable
private fun EndContent(
    detailsScreen: DetailsScreen,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (detailsScreen) {
        is DetailsScreen.None -> {
            Box(modifier) {
                RainbowProgressIndicator()
            }
        }

        is DetailsScreen.Post -> {
            PostScreen(
                detailsScreen.postId,
                onNavigateMainScreen,
                onShowSnackbar,
                modifier,
            )
        }

        is DetailsScreen.Message -> TODO()
    }
//    val isMessageScreen = messageModelState
//        .map { it.message.value.type is Message.Type.Message }
//        .getOrDefault(false)
//    when {
//        mainScreen is MainScreen.NavigationItem.Messages && isMessageScreen ->
//            messageModelState.composed(onShowSnackbar, modifier) { model ->
//                MessageScreen(
//                    model,
//                    onUserNameClick,
//                    onSubredditNameClick,
//                    modifier
//                )
//            }
//
//        mainScreen is MainScreen.NavigationItem.Settings -> {}
//        mainScreen is MainScreen.NavigationItem.Subreddits -> {}
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