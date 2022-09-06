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
import com.rainbow.desktop.message.MessageScreen
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
    sidebarItem: MainScreen.SidebarItem,
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
            Sidebar(
                sidebarItem,
                onNavigateMainScreen,
                Modifier
                    .wrapContentWidth(unbounded = true)
                    .fillMaxHeight()
            )
            Column(Modifier.fillMaxSize()) {
                RainbowTopAppBar(
                    mainScreen,
                    onNavigateMainScreen,
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
                    MainScreenContent(
                        mainScreen,
                        onNavigateMainScreen,
                        onNavigateDetailsScreen,
                        onShowSnackbar = { snackbarMessage = it },
                        Modifier.weight(1F),
                    )
                    DetailsScreenContent(
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
private fun MainScreenContent(
    mainScreen: MainScreen,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (mainScreen) {
        is MainScreen.SidebarItem -> when (mainScreen) {
            MainScreen.SidebarItem.Profile -> ProfileScreen(
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                onShowSnackbar,
                modifier,
            )

            MainScreen.SidebarItem.Home -> HomeScreen(
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                onShowSnackbar,
                modifier,
            )

            MainScreen.SidebarItem.Subreddits -> CurrentUserSubredditsScreen(
                onNavigateMainScreen,
                onShowSnackbar,
            )

            MainScreen.SidebarItem.Messages -> MessagesScreen(
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                onShowSnackbar,
                modifier,
            )

            MainScreen.SidebarItem.Settings -> SettingsScreen(Modifier.fillMaxWidth(0.5F))
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
private fun DetailsScreenContent(
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

        is DetailsScreen.Message -> {
            MessageScreen(
                detailsScreen.messageId,
                onNavigateMainScreen,
                modifier,
            )
        }
    }
}