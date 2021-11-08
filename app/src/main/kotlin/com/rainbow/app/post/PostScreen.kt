package com.rainbow.app.post

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.app.utils.PagingEffect
import com.rainbow.app.comment.PostCommentItem
import com.rainbow.app.components.RainbowProgressIndicator
import com.rainbow.app.utils.ShapeModifier
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.defaultPadding
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.Post
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

@Composable
fun PostScreen(
    post: Post,
    modifier: Modifier = Modifier,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
) {
    var lastComment by remember { mutableStateOf<Comment?>(null) }
    val scrollingState = rememberLazyListState()
    val state by produceState<UIState<List<Comment>>>(UIState.Loading, post.id) {
        Repos.Comment.getPostsComments(post.id)
            .map { it.toUIState() }
            .collect { value = it }
    }
    LazyColumn(
        modifier
            .then(ShapeModifier)
            .defaultPadding(),
        scrollingState
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                PostInfo(
                    post = post,
                    onUserNameClick,
                    onSubredditNameClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )

                Spacer(Modifier.height(8.dp))

                PostTitle(
                    title = post.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )

                Spacer(Modifier.height(8.dp))

                PostContent(
                    post = post,
                    modifier = Modifier
                        .heightIn(max = 600.dp)
                        .fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                PostCommands(
                    post,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }
        }
        item { Divider(Modifier.padding(vertical = 16.dp)) }
        comments(state, onLoadMore = { lastComment = it })
    }
    VerticalScrollbar(rememberScrollbarAdapter(scrollingState))
}

private fun LazyListScope.comments(
    commentsState: UIState<List<Comment>>,
    onLoadMore: (Comment) -> Unit,
) {
    when (commentsState) {
        is UIState.Empty -> item { Text("No comments found.") }
        is UIState.Failure -> item { Text("Failed to load comments") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> {
            val comments = commentsState.value
            itemsIndexed(comments) { index, comment ->
                PostCommentItem(comment)
                PagingEffect(comments, index, onLoadMore)
                if (comment.replies.isNotEmpty())
                    this@comments.comments(UIState.Success(comment.replies), onLoadMore)
            }
        }
    }
}