package com.rainbow.app

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.app.post.Sorting
import com.rainbow.app.post.posts
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.MainPostSorting
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostLayout
import com.rainbow.domain.models.TimeSorting
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

@Composable
inline fun HomeScreen(
    crossinline onPostClick: (Post) -> Unit,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    noinline onCommentsClick: () -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var postSorting by remember { mutableStateOf(MainPostSorting.Default) }
    var timeSorting by remember { mutableStateOf(TimeSorting.Default) }
    var lastPost by remember(postSorting, timeSorting) { mutableStateOf<Post?>(null) }
    val scrollingState = rememberLazyListState()
    val postLayout by Repos.Settings.postLayout.collectAsState(PostLayout.Card)
    val state by produceState<UIState<List<Post>>>(UIState.Loading, postSorting, timeSorting, lastPost) {
        if (lastPost == null)
            value = UIState.Loading
        Repos.Post.getHomePosts(postSorting, timeSorting, lastPost?.id)
            .map { it.toUIState() }
            .collect { value = it }
    }
    LazyColumn(modifier, scrollingState, verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
            postLayout,
            onPostClick,
            onUserNameClick,
            onSubredditNameClick,
            onCommentsClick,
            onShowSnackbar,
            onLoadMore = { lastPost = it }
        )
    }
    VerticalScrollbar(rememberScrollbarAdapter(scrollingState))
}