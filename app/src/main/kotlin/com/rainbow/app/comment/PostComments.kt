package com.rainbow.app.comment

import androidx.compose.animation.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import com.rainbow.app.components.RainbowProgressIndicator
import com.rainbow.app.utils.UIState
import com.rainbow.domain.models.Comment

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
        is UIState.Empty -> item { Text("No comments found.") }
        is UIState.Failure -> item { Text("Failed to load comments") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> {
            commentsState.value.forEach { comment ->
                item {
                    when (val commentType = comment.type) {
                        is Comment.Type.None -> PostCommentItem(
                            comment,
                            postUserName,
                            isRepliesVisible = commentsVisibility[comment.id] ?: true,
                            onClick = {
                                setCommentsVisibility(
                                    comment.id,
                                    commentsVisibility[comment.id]?.not() ?: false
                                )
                            },
                            onCommentUpdate,
                            onUserNameClick,
                            onSubredditNameClick,
                            modifier,
                        )
                        is Comment.Type.ViewMore -> ViewMoreCommentItem(
                            onClick = {
                                val moreComments = commentType.replies
                                onRequestMoreComments(comment.id, moreComments)
                            },
                            modifier,
                        )
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
            }
        }
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
                        modifier
                    )
                    is Comment.Type.ViewMore -> ViewMoreReplyItem(
                        onClick = { onRequestMoreComments(reply.id, replyType.replies) },
                        depth,
                        modifier
                    )
                    is Comment.Type.ContinueThread -> ContinueThreadReplyItem(
                        onClick = { onRequestThreadComments(replyType.parentId) },
                        depth,
                        modifier,
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