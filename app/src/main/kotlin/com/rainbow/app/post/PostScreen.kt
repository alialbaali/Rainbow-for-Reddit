package com.rainbow.app.post

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.app.comment.AddComment
import com.rainbow.app.comment.postComments
import com.rainbow.app.utils.ShapeModifier
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.defaultPadding
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostCommentSorting
import com.rainbow.domain.models.TimeSorting
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

@Composable
fun PostScreen(
    post: Post,
    modifier: Modifier = Modifier,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
) {
    var commentsSorting by remember { mutableStateOf(PostCommentSorting.Default) }
    var timeSorting by remember { mutableStateOf(TimeSorting.Default) }
    var lastComment by remember(commentsSorting, timeSorting) { mutableStateOf<Comment?>(null) }
    val scrollingState = rememberLazyListState()
    val state by produceState<UIState<List<Comment>>>(
        UIState.Loading,
        post.id,
        commentsSorting,
        timeSorting,
        lastComment
    ) {
        Repos.Comment.getPostsComments(post.id, commentsSorting)
            .map { it.toUIState() }
            .collect { value = it }
    }
    LazyColumn(
        modifier
            .then(ShapeModifier)
            .defaultPadding(),
        scrollingState,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PostInfo(
                    post = post,
                    onUserNameClick,
                    onSubredditNameClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )

                PostTitle(
                    title = post.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )

                PostContent(
                    post = post,
                    modifier = Modifier
                        .heightIn(max = 600.dp)
                        .fillMaxWidth()
                )

                PostCommands(
                    post,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }
        }
        item {
            Sorting(
                commentsSorting,
                onSortingUpdate = { commentsSorting = it },
                timeSorting,
                onTimeSortingUpdate = { timeSorting = it }
            )
        }
        item {
            AddComment(post, Modifier.fillParentMaxWidth())
        }
        postComments(state, onLoadMore = { lastComment = it }, onUserNameClick, onSubredditNameClick)
    }
    VerticalScrollbar(rememberScrollbarAdapter(scrollingState))
}