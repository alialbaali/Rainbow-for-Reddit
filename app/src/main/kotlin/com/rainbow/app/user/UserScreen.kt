package com.rainbow.app.user

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
import com.rainbow.app.post.PostModel
import com.rainbow.app.post.PostSorting
import com.rainbow.app.post.posts
import com.rainbow.app.profile.Header
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.composed
import com.rainbow.data.Repos
import com.rainbow.domain.models.*

private enum class UserTab {
    Overview, Submitted, Comments,
}

@Composable
fun UserScreen(
    userName: String,
    focusRequester: FocusRequester,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    setPostModel: (PostModel<PostSorting>) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableStateOf(UserTab.Submitted) }
    var postSorting by remember { mutableStateOf(UserPostSorting.Default) }
    var timeSorting by remember { mutableStateOf(TimeSorting.Default) }
    val scrollingState = rememberLazyListState()
    val model = remember { UserModel(userName) }
    setPostModel(model.postModel as PostModel<PostSorting>)
    val postLayout by Repos.Settings.postLayout.collectAsState(PostLayout.Card)
    val userState by model.user.collectAsState()
    val postsState by model.postModel.posts.collectAsState()
    val commentsState by produceState<UIState<List<Comment>>>(UIState.Loading) {
//            Repos.Comment.getUserComments(user.id)
    }
    userState.composed(onShowSnackbar) { user ->
        LazyColumn(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item { Header(user) }
            item {
                DefaultTabRow(
                    selectedTab = selectedTab,
                    onTabClick = { selectedTab = it }
                )
            }
            item {
                PostSorting(
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
                    model.postModel,
                    postLayout,
                    focusRequester,
                    onUserNameClick,
                    onSubredditNameClick,
                    onShowSnackbar,
                    onLoadMore = { model.postModel.selectPost(it.id) }
                )
                UserTab.Comments -> comments(commentsState, onUserNameClick, onSubredditNameClick)
            }
        }
        VerticalScrollbar(rememberScrollbarAdapter(scrollingState))
    }
}