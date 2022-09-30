package com.rainbow.desktop.comment

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrDefault
import com.rainbow.domain.models.Comment

fun LazyListScope.postComments(
    commentsState: UIState<List<Comment>>,
    postUserName: String,
    commentsVisibility: Map<String, Boolean>,
    onCommentVisibilityChanged: (String, Boolean) -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onRequestMoreComments: (String, List<String>) -> Unit,
    onRequestThreadComments: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val comments = commentsState.getOrDefault(emptyList())
    comments.forEach { comment ->
        item {
            val isRepliesVisible = commentsVisibility[comment.id] ?: true
            when (val commentType = comment.type) {
                is Comment.Type.None -> PostCommentItem(
                    comment,
                    postUserName,
                    onClick = {
                        onCommentVisibilityChanged(
                            comment.id,
                            commentsVisibility[comment.id]?.not() ?: false
                        )
                    },
                    onUserNameClick,
                    onSubredditNameClick,
                    onShowSnackbar,
                    modifier.clip(comment.replies.isEmpty(), isRepliesVisible),
                )

//                is Comment.Type.MoreComments -> MoreCommentsItem(
//                    onClick = {
//                        val moreComments = commentType.replies
//                        onRequestMoreComments(comment.id, moreComments)
//                    },
//                    modifier.clip(isEmpty = true, isRepliesVisible = false)
//                )

                is Comment.Type.MoreComments -> DisposableEffect(Unit) {
                    onRequestMoreComments(comment.id, commentType.replies)
                    onDispose { }
                }

                is Comment.Type.Thread -> {}
            }
        }
        replies(
            comment.replies,
            postUserName,
            isVisible = commentsVisibility[comment.id] ?: true,
            isRepliesVisible = {
                commentsVisibility[it.id] ?: true
            },
            setIsRepliesVisible = { reply, isVisible -> onCommentVisibilityChanged(reply.id, isVisible) },
            onUserNameClick,
            onSubredditNameClick,
            onRequestMoreComments,
            onRequestThreadComments,
            hasMoreCommentsAfterIndex = { parentId ->
                val parentIndex = comments.indexOfFirst { it.id == parentId }
                comments.getOrNull(parentIndex + 1) != null
            },
            onShowSnackbar,
        )
        item { Spacer(Modifier.height(16.dp)) }
    }

    if (commentsState.isLoading) {
        item { RainbowProgressIndicator() }
    }

}

fun LazyListScope.replies(
    replies: List<Comment>,
    postUserName: String,
    isVisible: Boolean,
    isRepliesVisible: (Comment) -> Boolean,
    setIsRepliesVisible: (Comment, Boolean) -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onRequestMoreComments: (String, List<String>) -> Unit,
    onRequestThreadComments: (String) -> Unit,
    hasMoreCommentsAfterIndex: (String) -> Boolean,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
    depth: Int = 1,
) {
    replies.forEachIndexed { index, reply ->
        item {
            AnimatedVisibility(
                isVisible,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                val isLastItem = reply.replies.isEmpty()
                        && index == replies.lastIndex && !hasMoreCommentsAfterIndex(reply.parentId)
                when (val replyType = reply.type) {
                    is Comment.Type.None -> ReplyItem(
                        reply,
                        postUserName,
                        depth,
                        onClick = { setIsRepliesVisible(reply, !isRepliesVisible(reply)) },
                        onUserNameClick,
                        onSubredditNameClick,
                        onShowSnackbar,
                        if (isLastItem)
                            Modifier.clip(
                                MaterialTheme.shapes.medium.copy(
                                    topStart = CornerSize(0),
                                    topEnd = CornerSize(0)
                                )
                            )
                        else
                            Modifier
                    )

                    is Comment.Type.MoreComments -> MoreRepliesItem(
                        replyType,
                        onClick = { onRequestMoreComments(reply.id, replyType.replies) },
                        depth,
                        if (isLastItem)
                            Modifier.clip(
                                MaterialTheme.shapes.medium.copy(
                                    topStart = CornerSize(0),
                                    topEnd = CornerSize(0)
                                )
                            )
                        else
                            Modifier
                    )

                    is Comment.Type.Thread -> ThreadItem(
                        onClick = { onRequestThreadComments(replyType.parentId) },
                        depth,
                        if (isLastItem)
                            Modifier.clip(
                                MaterialTheme.shapes.medium.copy(
                                    topStart = CornerSize(0),
                                    topEnd = CornerSize(0)
                                )
                            )
                        else
                            Modifier
                    )
                }
            }
        }
        replies(
            reply.replies,
            postUserName,
            isVisible = isRepliesVisible(reply) && isVisible,
            isRepliesVisible,
            setIsRepliesVisible,
            onUserNameClick,
            onSubredditNameClick,
            onRequestMoreComments,
            onRequestThreadComments,
            hasMoreCommentsAfterIndex = { parentId ->
                val parentIndex = replies.indexOfFirst { it.id == parentId }
                replies.getOrNull(parentIndex + 1) != null && hasMoreCommentsAfterIndex(parentId)
            },
            onShowSnackbar,
            modifier,
            depth = depth + 1,
        )
    }
}

private fun Modifier.clip(isEmpty: Boolean, isRepliesVisible: Boolean) = composed {
    val dp by animateDpAsState(if (!isEmpty && isRepliesVisible) 0.dp else 16.dp)
    val cornerSize = remember(dp) { CornerSize(dp) }

    if (!isEmpty && isRepliesVisible) {
        Modifier.clip(
            MaterialTheme.shapes.medium.copy(
                bottomEnd = cornerSize,
                bottomStart = cornerSize
            )
        )
    } else {
        Modifier.clip(MaterialTheme.shapes.medium.copy(cornerSize))
    }
}