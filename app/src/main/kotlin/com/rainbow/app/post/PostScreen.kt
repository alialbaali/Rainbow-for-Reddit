package com.rainbow.app.post

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.unit.dp
import com.rainbow.app.comment.AddComment
import com.rainbow.app.comment.postComments
import com.rainbow.app.utils.*
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
    isAddCommentFocusable: Boolean,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onCommentsClick: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
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
//        value = UIState.Loading
        Repos.Comment.getPostsComments(post.id, commentsSorting)
            .map { it.toUIState() }
            .collect { value = it }
    }
    var repliesVisibility by remember(state) { mutableStateOf(emptyMap<Comment, Boolean>()) }
    val focusRequester = remember(post) { FocusRequester() }
    SideEffect {
        if (isAddCommentFocusable)
            focusRequester.requestFocus()
    }
    LazyColumn(
        modifier
            .defaultShape()
            .defaultPadding(),
        scrollingState,
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
                    isRead = false,
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

                PostActions(
                    post,
                    onCommentsClick,
                    onShowSnackbar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }
            Spacer(Modifier.height(16.dp))
            AddComment(
                post,
                Modifier
                    .fillParentMaxWidth()
                    .focusOrder(focusRequester)
            )
            Spacer(Modifier.height(16.dp))
            Sorting(
                commentsSorting,
                onSortingUpdate = { commentsSorting = it },
                timeSorting,
                onTimeSortingUpdate = { timeSorting = it }
            )
            Spacer(Modifier.height(16.dp))
            if (state.isSuccess)
                Row(Modifier.fillParentMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    TextButton(
                        onClick = {
                            repliesVisibility = state.asSuccess().value
                                .associateWith { false }
                        }
                    ) {
                        Text("Collapse all")
                    }

                    TextButton(
                        onClick = {
                            repliesVisibility = state.asSuccess().value
                                .associateWith { true }
                        }
                    ) {
                        Text("Expand all")
                    }
                }
            Spacer(Modifier.height(16.dp))
        }
        postComments(
            state,
            repliesVisibility,
            setRepliesVisibility = { id, isVisible ->
                repliesVisibility = repliesVisibility.toMutableMap().apply { this[id] = isVisible }
            },
            onLoadMore = { lastComment = it },
            onUserNameClick,
            onSubredditNameClick
        )
    }
    VerticalScrollbar(rememberScrollbarAdapter(scrollingState))
}