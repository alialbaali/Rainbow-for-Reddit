package com.rainbow.app

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rainbow.app.post.Sorting
import com.rainbow.app.post.posts
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.MainPostSorting
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.TimeSorting
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

@Composable
inline fun HomeScreen(
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    crossinline onPostClick: (Post) -> Unit,
    modifier: Modifier = Modifier
) {
    var postSorting by remember { mutableStateOf(MainPostSorting.Default) }
    var timeSorting by remember { mutableStateOf(TimeSorting.Default) }
    var lastPost by remember { mutableStateOf<Post?>(null) }
    val state by produceState<UIState<List<Post>>>(UIState.Loading, postSorting, timeSorting, lastPost) {
        Repos.Post.getHomePosts(postSorting, timeSorting, lastPost?.id)
            .map { it.toUIState() }
            .collect { value = it }
    }
    LazyColumn(modifier) {
        item {
            Sorting(
                postsSorting = postSorting,
                onSortingUpdate = { postSorting = it },
                timeSorting = timeSorting,
                onTimeSortingUpdate = { timeSorting = it }
            )
        }
        posts(
            state,
            onPostClick,
            onUserNameClick,
            onSubredditNameClick,
            onLoadMore = { lastPost = it }
        )
    }
}