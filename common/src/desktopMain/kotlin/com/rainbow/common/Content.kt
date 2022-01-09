package com.rainbow.common

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
import com.rainbow.common.home.HomeScreen
import com.rainbow.common.message.MessageScreen
import com.rainbow.common.message.MessageScreenModel
import com.rainbow.common.message.MessagesScreen
import com.rainbow.common.model.ListModel
import com.rainbow.common.navigation.Screen
import com.rainbow.common.navigation.Sidebar
import com.rainbow.common.post.PostScreen
import com.rainbow.common.post.PostScreenModel
import com.rainbow.common.profile.ProfileScreen
import com.rainbow.common.search.SearchScreen
import com.rainbow.common.settings.SettingsScreen
import com.rainbow.common.subreddit.CurrentUserSubredditsScreen
import com.rainbow.common.subreddit.SubredditScreen
import com.rainbow.common.user.UserScreen
import com.rainbow.common.utils.*
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
    val focusRequester = remember { FocusRequester() }
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
    Box(modifier.background(MaterialTheme.colors.background)) {
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
                Row(Modifier.fillMaxSize().defaultPadding(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    CenterContent(
                        screen,
                        focusRequester,
                        onUserNameClick,
                        onSubredditNameClick,
                        RainbowModel::selectMessageOrPost,
                        onShowSnackbar = {snackbarMessage = it},
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
                        focusRequester,
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
    focusRequester: FocusRequester,
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
                    focusRequester,
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
                    focusRequester,
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
            focusRequester,
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
            focusRequester,
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
        is Screen.Search -> SearchScreen(
            screen.searchTerm,
            focusRequester,
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
    focusRequester: FocusRequester,
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
                focusRequester,
                onUserNameClick,
                onSubredditNameClick,
                onPostUpdate,
                onCommentUpdate,
                onShowSnackbar,
                modifier
            )
        }
    }
}