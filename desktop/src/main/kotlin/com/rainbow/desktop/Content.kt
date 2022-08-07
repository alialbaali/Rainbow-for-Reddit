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
import com.rainbow.desktop.message.MessageScreenModel
import com.rainbow.desktop.model.ListModel
import com.rainbow.desktop.post.PostScreenModel
import com.rainbow.desktop.utils.*
import com.rainbow.desktop.home.HomeScreen
import com.rainbow.desktop.message.MessageScreen
import com.rainbow.desktop.message.MessagesScreen
import com.rainbow.desktop.navigation.Screen
import com.rainbow.desktop.navigation.Sidebar
import com.rainbow.desktop.post.CommentActions
import com.rainbow.desktop.post.PostScreen
import com.rainbow.desktop.profile.ProfileScreen
import com.rainbow.desktop.search.SearchScreen
import com.rainbow.desktop.settings.SettingsScreen
import com.rainbow.desktop.subreddit.CurrentUserSubredditsScreen
import com.rainbow.desktop.subreddit.SubredditScreen
import com.rainbow.desktop.user.UserScreen
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Message
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.Subreddit

@Composable
internal fun Content(
    screen: Screen,
    navigationItem: Screen.NavigationItem,
    onNavigationItemClick: (Screen.NavigationItem) -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onSearchClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit,
    isBackEnabled: Boolean,
    isForwardEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val postScreenModelType by RainbowModel.postScreenModelType.collectAsState()
    val messageScreenModel by RainbowModel.messageScreenModel.collectAsState()
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val sorting by RainbowModel.sorting.collectAsState()
    val timeSorting by RainbowModel.timeSorting.collectAsState()
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }
    Box(modifier.background(MaterialTheme.colorScheme.background)) {
        Row(Modifier.fillMaxSize()) {
            StartContent(
                navigationItem,
                onNavigationItemClick,
                Modifier
                    .wrapContentWidth(unbounded = true)
                    .fillMaxHeight(),
            )
            Column(Modifier.fillMaxSize()) {
                RainbowTopAppBar(
                    screen,
                    sorting,
                    timeSorting,
                    onSearchClick,
                    onSubredditNameClick,
                    onBackClick,
                    onForwardClick,
                    RainbowModel::setSorting,
                    RainbowModel::setTimeSorting,
                    isBackEnabled,
                    isForwardEnabled,
                    RainbowModel::refreshContent,
                )
                Row(
                    Modifier.fillMaxSize().defaultPadding(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CenterContent(
                        screen,
                        onUserNameClick,
                        onSubredditNameClick,
                        RainbowModel::selectMessageOrPost,
                        onShowSnackbar = { snackbarMessage = it },
                        RainbowModel::setListModel,
                        onPostClick = { RainbowModel.selectPost(PostScreenModel.Type.PostEntity(it)) },
                        onCommentClick = { RainbowModel.selectPost(PostScreenModel.Type.PostId(it.postId)) },
                        RainbowModel::updatePost,
                        RainbowModel::updateComment,
                        RainbowModel::updateSubreddit,
                        Modifier.weight(1F),
                    )
                    EndContent(
                        screen,
                        postScreenModelType,
                        messageScreenModel,
                        onUserNameClick,
                        onSubredditNameClick,
                        onShowSnackbar = { snackbarMessage = it },
                        RainbowModel::updatePost,
                        RainbowModel::updateComment,
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
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onMessageClick: (Message) -> Unit,
    onShowSnackbar: (String) -> Unit,
    setListModel: (ListModel<*>) -> Unit,
    onPostClick: (Post) -> Unit,
    onCommentClick: (Comment) -> Unit,
    onPostUpdate: (Post) -> Unit,
    onCommentUpdate: (Comment) -> Unit,
    onSubredditUpdate: (Subreddit) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (screen) {
        is Screen.NavigationItem -> {
            when (screen) {
                Screen.NavigationItem.Profile -> ProfileScreen(
                    onUserNameClick,
                    onSubredditNameClick,
                    onPostClick,
                    onCommentClick,
                    onPostUpdate,
                    onCommentUpdate,
                    onShowSnackbar,
                    setListModel,
                    modifier,
                )
                Screen.NavigationItem.Home -> HomeScreen(
                    onUserNameClick,
                    onSubredditNameClick,
                    onPostClick,
                    onCommentClick,
                    onPostUpdate,
                    onCommentUpdate,
                    onShowSnackbar,
                    setListModel,
                    modifier,
                )
                Screen.NavigationItem.Subreddits -> CurrentUserSubredditsScreen(
                    onSubredditNameClick,
                    onSubredditUpdate,
                    onShowSnackbar,
                    setListModel
                )
                Screen.NavigationItem.Messages -> MessagesScreen(
                    onMessageClick,
                    onUserNameClick,
                    onSubredditNameClick,
                    onShowSnackbar,
                    setListModel,
                    modifier,
                )
                Screen.NavigationItem.Settings -> SettingsScreen(Modifier.fillMaxWidth(0.5F))
            }
        }
        is Screen.Subreddit -> SubredditScreen(
            screen.subredditName,
            onUserNameClick,
            onSubredditNameClick,
            onPostClick,
            onSubredditUpdate,
            onPostUpdate,
            onShowSnackbar,
            setListModel,
            modifier,
        )
        is Screen.User -> UserScreen(
            screen.userName,
            onUserNameClick,
            onSubredditNameClick,
            onPostClick,
            onCommentClick,
            onPostUpdate,
            onCommentUpdate,
            {},
            onShowSnackbar,
            setListModel,
            modifier,
        )
        is Screen.Search -> SearchScreen(
            screen.searchTerm,
            onUserNameClick,
            onSubredditNameClick,
            onPostClick,
            onSubredditUpdate,
            onPostUpdate,
            onShowSnackbar,
            setListModel,
            modifier
        )
    }
}


@Composable
private fun EndContent(
    screen: Screen,
    postScreenModelType: UIState<PostScreenModel.Type>,
    messageModelState: UIState<MessageScreenModel>,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    onPostUpdate: (Post) -> Unit,
    onCommentUpdate: (Comment) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isMessageScreen = messageModelState
        .map { it.message.value.type is Message.Type.Message }
        .getOrDefault(false)
    when {
        screen is Screen.NavigationItem.Messages && isMessageScreen ->
            messageModelState.composed(onShowSnackbar, modifier) { model ->
                MessageScreen(
                    model,
                    onUserNameClick,
                    onSubredditNameClick,
                    modifier
                )
            }
        screen is Screen.NavigationItem.Settings -> {}
        screen is Screen.NavigationItem.Subreddits -> {}
        else -> postScreenModelType.composed(onShowSnackbar, modifier) { type ->
            PostScreen(
                type,
                onUserNameClick,
                onSubredditNameClick,
                onPostUpdate,
                onCommentUpdate,
                { model -> CommentActions(model) },
                onShowSnackbar,
                modifier
            )
        }
    }
}