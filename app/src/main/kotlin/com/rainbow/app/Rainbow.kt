package com.rainbow.app

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
import com.rainbow.app.home.HomeScreen
import com.rainbow.app.message.MessageScreen
import com.rainbow.app.message.MessageScreenModel
import com.rainbow.app.message.MessagesScreen
import com.rainbow.app.model.ListModel
import com.rainbow.app.navigation.Screen
import com.rainbow.app.post.PostScreen
import com.rainbow.app.post.PostScreenModel
import com.rainbow.app.profile.ProfileScreen
import com.rainbow.app.search.SearchScreen
import com.rainbow.app.settings.SettingsScreen
import com.rainbow.app.sidebar.Sidebar
import com.rainbow.app.subreddit.CurrentUserSubredditsScreen
import com.rainbow.app.subreddit.SubredditScreen
import com.rainbow.app.user.UserScreen
import com.rainbow.app.utils.*
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Message
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.Subreddit

@Composable
fun Rainbow(
    screen: Screen,
    sidebarItem: Screen.SidebarItem,
    onSidebarClick: (Screen.SidebarItem) -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onSearchClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit,
    isBackEnabled: Boolean,
    isForwardEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val postScreenModel by RainbowModel.postScreenModel.collectAsState()
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
                sidebarItem,
                onSidebarClick,
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
                        postScreenModel,
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
    sidebarItem: Screen.SidebarItem,
    onSidebarClick: (Screen.SidebarItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Sidebar(
        sidebarItem,
        onSidebarClick,
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
        is Screen.SidebarItem -> {
            when (screen) {
                Screen.SidebarItem.Profile -> ProfileScreen(
                    focusRequester,
                    onUserNameClick,
                    onSubredditNameClick,
                    onShowSnackbar,
                    setListModel,
                    onPostUpdate,
                    onPostClick,
                    onCommentClick,
                    onCommentUpdate,
                    modifier,
                )
                Screen.SidebarItem.Home -> HomeScreen(
                    focusRequester,
                    onUserNameClick,
                    onSubredditNameClick,
                    onPostUpdate,
                    onCommentClick,
                    onShowSnackbar,
                    onPostClick,
                    onCommentUpdate,
                    setListModel,
                    modifier,
                )
                Screen.SidebarItem.Subreddits -> CurrentUserSubredditsScreen(
                    onSubredditNameClick,
                    onSubredditUpdate,
                    onShowSnackbar,
                    setListModel
                )
                Screen.SidebarItem.Messages -> MessagesScreen(
                    onMessageClick,
                    onUserNameClick,
                    onSubredditNameClick,
                    onShowSnackbar,
                    setListModel,
                    modifier,
                )
                Screen.SidebarItem.Settings -> SettingsScreen()
            }
        }
        is Screen.Subreddit -> SubredditScreen(
            screen.subredditName,
            onSubredditUpdate,
            focusRequester,
            onUserNameClick,
            onSubredditNameClick,
            onShowSnackbar,
            setListModel,
            onPostUpdate,
            onPostClick,
            modifier,
        )
        is Screen.User -> UserScreen(
            screen.userName,
            onPostUpdate,
            onPostClick,
            onCommentClick,
            onCommentUpdate,
            focusRequester,
            onUserNameClick,
            onSubredditNameClick,
            onShowSnackbar,
            setListModel,
            modifier,
        )
        is Screen.Search -> SearchScreen(
            screen.searchTerm,
            onPostUpdate,
            onPostClick,
            onSubredditUpdate,
            focusRequester,
            onUserNameClick,
            onSubredditNameClick,
            onShowSnackbar,
            setListModel,
            modifier
        )
    }
}


@Composable
private fun EndContent(
    screen: Screen,
    postModelState: UIState<PostScreenModel>,
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
        screen is Screen.SidebarItem.Messages && isMessageScreen ->
            messageModelState.composed(onShowSnackbar, modifier) { model ->
                MessageScreen(
                    model,
                    onUserNameClick,
                    onSubredditNameClick,
                    modifier
                )
            }
        screen is Screen.SidebarItem.Settings -> {}
        screen is Screen.SidebarItem.Subreddits -> {}
        else -> postModelState.composed(onShowSnackbar, modifier) { model ->
            PostScreen(
                model,
                focusRequester,
                onUserNameClick,
                onSubredditNameClick,
                onShowSnackbar,
                onPostUpdate,
                onCommentUpdate,
                modifier
            )
        }
    }
}