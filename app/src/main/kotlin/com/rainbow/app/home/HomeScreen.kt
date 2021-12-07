package com.rainbow.app.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import com.rainbow.app.components.RainbowLazyColumn
import com.rainbow.app.post.PostModel
import com.rainbow.app.post.PostSorting
import com.rainbow.app.post.posts
import com.rainbow.domain.models.PostSorting

@Composable
inline fun HomeScreen(
    focusRequester: FocusRequester,
    refreshContent: Int,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    setPostModel: (PostModel<PostSorting>) -> Unit,
    modifier: Modifier = Modifier,
) {
    setPostModel(HomeModel.postModel as PostModel<PostSorting>)
    val postSorting by HomeModel.postModel.postSorting.collectAsState()
    val timeSorting by HomeModel.postModel.timeSorting.collectAsState()
    val postLayout by HomeModel.postModel.postLayout.collectAsState()
    val state by HomeModel.postModel.posts.collectAsState()
    RainbowLazyColumn(modifier) {
        item {
            PostSorting(
                postsSorting = postSorting,
                onSortingUpdate = { HomeModel.postModel.setPostSorting(it) },
                timeSorting = timeSorting,
                onTimeSortingUpdate = { HomeModel.postModel.setTimeSorting(it) }
            )
        }
        posts(
            state,
            HomeModel.postModel,
            postLayout,
            focusRequester,
            onUserNameClick,
            onSubredditNameClick,
            onShowSnackbar,
            onLoadMore = { HomeModel.postModel.setLastPost(it) }
        )
    }
}