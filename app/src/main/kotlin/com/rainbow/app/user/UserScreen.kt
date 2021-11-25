package com.rainbow.app.user

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.rainbow.app.comment.comments
import com.rainbow.app.components.DefaultTabRow
import com.rainbow.app.post.Sorting
import com.rainbow.app.post.posts
import com.rainbow.app.profile.Header
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.composed
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

private enum class UserTab {
    Overview, Submitted, Comments,
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserScreen(
    userName: String,
    focusRequester: FocusRequester,
    onPostClick: (Post) -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableStateOf(UserTab.Submitted) }
    var postSorting by remember { mutableStateOf(UserPostSorting.Default) }
    var timeSorting by remember { mutableStateOf(TimeSorting.Default) }
    var lastPost by remember(postSorting, timeSorting) { mutableStateOf<Post?>(null) }
    val scrollingState = rememberLazyListState()
    val postLayout by Repos.Settings.postLayout.collectAsState(PostLayout.Card)
    val state by produceState<UIState<User>>(UIState.Loading) {
        Repos.User.getUser(userName)
            .toUIState()
            .also { value = it }
    }
    state.composed { user ->
        val postsState by produceState<UIState<List<Post>>>(UIState.Loading, user, postSorting, timeSorting, lastPost) {
            if (lastPost == null)
                value = UIState.Loading
            Repos.Post.getUserSubmittedPosts(user.name, postSorting, timeSorting, lastPost?.id)
                .map { it.map { it.filterNot { it.isHidden } } }
                .map { it.toUIState() }
                .collect { value = it }
        }
        val commentsState by produceState<UIState<List<Comment>>>(UIState.Loading) {
            Repos.Comment.getUserComments(user.id)
                .map { it.toUIState() }
                .collect { value = it }
        }

        LazyColumn(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item { Header(user) }
            item {
                DefaultTabRow(
                    selectedTab = selectedTab,
                    onTabClick = { selectedTab = it }
                )
            }
            item {
                Sorting(
                    postsSorting = postSorting,
                    onSortingUpdate = { postSorting = it },
                    timeSorting = timeSorting,
                    onTimeSortingUpdate = { timeSorting = it }
                )
            }
            when (selectedTab) {
                UserTab.Overview -> {
                }
                UserTab.Submitted -> posts(
                    postsState,
                    postLayout,
                    focusRequester,
                    onPostClick,
                    onUserNameClick,
                    onSubredditNameClick,
                    onShowSnackbar,
                    onLoadMore = { lastPost = it }
                )
                UserTab.Comments -> comments(commentsState, onUserNameClick, onSubredditNameClick)
            }
        }
        VerticalScrollbar(rememberScrollbarAdapter(scrollingState))
    }
}