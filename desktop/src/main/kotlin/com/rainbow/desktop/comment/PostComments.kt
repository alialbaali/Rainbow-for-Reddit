package com.rainbow.desktop.comment

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.utils.UIState
import com.rainbow.domain.models.Comment

private fun Modifier.clipReplies(isEmpty: Boolean, isVisible: Boolean) = composed {
    if (!isEmpty && isVisible) {
        val cornerSize = CornerSize(0.dp)
        Modifier.clip(
            MaterialTheme.shapes.medium.copy(
                bottomEnd = cornerSize,
                bottomStart = cornerSize
            )
        )
    } else {
        Modifier.clip(MaterialTheme.shapes.medium)
    }
}

fun LazyListScope.postComments(
    commentsState: UIState<List<Comment>>,
    postUserName: String,
    commentsVisibility: Map<String, Boolean>,
    setCommentsVisibility: (String, Boolean) -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onCommentUpdate: (Comment) -> Unit,
    onRequestMoreComments: (String, List<String>) -> Unit,
    onRequestThreadComments: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (commentsState) {
        is UIState.Failure -> item { Text("Failed to load comments") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> {
            commentsState.value.forEach { comment ->
                item {
                    val isRepliesVisible = commentsVisibility[comment.id] ?: true
                    when (val commentType = comment.type) {
                        is Comment.Type.None -> PostCommentItem(
                            comment,
                            postUserName,
                            isRepliesVisible = isRepliesVisible,
                            onClick = {
                                setCommentsVisibility(
                                    comment.id,
                                    commentsVisibility[comment.id]?.not() ?: false
                                )
                            },
                            onCommentUpdate,
                            onUserNameClick,
                            onSubredditNameClick,
                            modifier.clipReplies(comment.replies.isEmpty(), isRepliesVisible),
                        )

                        is Comment.Type.ViewMore -> ViewMoreCommentItem(
                            onClick = {
                                val moreComments = commentType.replies
                                onRequestMoreComments(comment.id, moreComments)
                            },
                            modifier.clipReplies(isEmpty = true, isVisible = false)
                        )

                        is Comment.Type.ContinueThread -> {}
                    }
                }
                replies(
                    comment.replies,
                    postUserName,
                    isVisible = commentsVisibility[comment.id] ?: true,
                    isRepliesVisible = { commentsVisibility[it.id] ?: true },
                    setIsRepliesVisible = { reply, isVisible -> setCommentsVisibility(reply.id, isVisible) },
                    onUserNameClick,
                    onSubredditNameClick,
                    onRequestMoreComments,
                    onCommentUpdate,
                    onRequestThreadComments,
                )
                item { Spacer(Modifier.height(16.dp)) }
            }
        }

        UIState.Empty -> {}
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
    onCommentUpdate: (Comment) -> Unit,
    onRequestThreadComments: (String) -> Unit,
    modifier: Modifier = Modifier,
    depth: Int = 1,
) {
    replies.forEach { reply ->
        item {
            AnimatedVisibility(
                isVisible,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                when (val replyType = reply.type) {
                    is Comment.Type.None -> ReplyItem(
                        reply,
                        postUserName,
                        isRepliesVisible(reply),
                        depth,
                        onClick = { setIsRepliesVisible(reply, !isRepliesVisible(reply)) },
                        onCommentUpdate,
                        onUserNameClick,
                        onSubredditNameClick,
                        modifier.clipReplies(reply.replies.isEmpty(), isRepliesVisible(reply))
                    )

                    is Comment.Type.ViewMore -> ViewMoreReplyItem(
                        onClick = { onRequestMoreComments(reply.id, replyType.replies) },
                        depth,
                        modifier.clipReplies(reply.replies.isEmpty(), isRepliesVisible(reply))
                    )

                    is Comment.Type.ContinueThread -> ContinueThreadReplyItem(
                        onClick = { onRequestThreadComments(replyType.parentId) },
                        depth,
                        modifier.clipReplies(reply.replies.isEmpty(), isRepliesVisible(reply))
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
            onCommentUpdate,
            onRequestThreadComments,
            modifier,
            depth = depth + 1,
        )
    }
}