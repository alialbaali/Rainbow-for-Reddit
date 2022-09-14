package com.rainbow.desktop

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
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
import com.rainbow.desktop.subreddit.SubredditScreen
import com.rainbow.desktop.subreddit.SubredditsScreen
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
    val saveableStateHolder = rememberSaveableStateHolder()
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
                        .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium)
                        .fillMaxSize()
                        .padding(horizontal = MaterialTheme.dpDimensions.medium),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val mainScreenModifier = remember(mainScreen) {
                        when (mainScreen) {
                            MainScreen.SidebarItem.Subreddits, MainScreen.SidebarItem.Settings -> Modifier.fillMaxSize()
                            else -> Modifier.weight(1F)
                        }
                    }
                    MainScreenContent(
                        mainScreen,
                        saveableStateHolder,
                        onNavigateMainScreen,
                        onNavigateDetailsScreen,
                        onShowSnackbar = { snackbarMessage = it },
                        mainScreenModifier,
                    )
                    DetailsScreenContent(
                        detailsScreen,
                        saveableStateHolder,
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
    saveableStateHolder: SaveableStateHolder,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Crossfade(mainScreen, modifier) { animatedMainScreen ->
        saveableStateHolder.SaveableStateProvider(animatedMainScreen) {
            when (animatedMainScreen) {
                is MainScreen.SidebarItem -> when (animatedMainScreen) {
                    MainScreen.SidebarItem.Profile -> ProfileScreen(
                        onNavigateMainScreen,
                        onNavigateDetailsScreen,
                        onShowSnackbar,
                        Modifier.fillMaxSize(),
                    )

                    MainScreen.SidebarItem.Home -> HomeScreen(
                        onNavigateMainScreen,
                        onNavigateDetailsScreen,
                        onShowSnackbar,
                        Modifier.fillMaxSize(),
                    )

                    MainScreen.SidebarItem.Subreddits -> SubredditsScreen(
                        onNavigateMainScreen,
                        onShowSnackbar,
                        Modifier.fillMaxSize(),
                    )

                    MainScreen.SidebarItem.Messages -> MessagesScreen(
                        onNavigateMainScreen,
                        onNavigateDetailsScreen,
                        onShowSnackbar,
                        Modifier.fillMaxSize(),
                    )

                    MainScreen.SidebarItem.Settings -> SettingsScreen(
                        Modifier.fillMaxSize()
                    )
                }

                is MainScreen.Subreddit -> SubredditScreen(
                    animatedMainScreen.subredditName,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen,
                    onShowSnackbar,
                    Modifier.fillMaxSize(),
                )

                is MainScreen.User -> UserScreen(
                    animatedMainScreen.userName,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen,
                    onShowSnackbar,
                    Modifier.fillMaxSize(),
                )

                is MainScreen.Search -> SearchScreen(
                    animatedMainScreen.searchTerm,
                    onNavigateMainScreen,
                    onNavigateDetailsScreen,
                    onShowSnackbar,
                    Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun DetailsScreenContent(
    detailsScreen: DetailsScreen,
    saveableStateHolder: SaveableStateHolder,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Crossfade(detailsScreen, modifier) { animatedDetailsScreen ->
        saveableStateHolder.SaveableStateProvider(animatedDetailsScreen) {
            when (animatedDetailsScreen) {
                is DetailsScreen.None -> {
                    Box(Modifier.fillMaxSize()) {
                        RainbowProgressIndicator()
                    }
                }

                is DetailsScreen.Post -> {
                    PostScreen(
                        animatedDetailsScreen.postId,
                        onNavigateMainScreen,
                        onShowSnackbar,
                        Modifier.fillMaxSize(),
                    )
                }

                is DetailsScreen.Message -> {
                    MessageScreen(
                        animatedDetailsScreen.messageId,
                        onNavigateMainScreen,
                        Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}