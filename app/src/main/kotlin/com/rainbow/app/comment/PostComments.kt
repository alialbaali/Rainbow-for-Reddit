package com.rainbow.app.comment

import androidx.compose.animation.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import com.rainbow.app.components.RainbowProgressIndicator
import com.rainbow.app.utils.PagingEffect
import com.rainbow.app.utils.UIState
import com.rainbow.domain.models.Comment

inline fun LazyListScope.postComments(
    commentsState: UIState<List<Comment>>,
    postUserName: String,
    commentsVisibility: Map<String, Boolean>,
    noinline setCommentsVisibility: (String, Boolean) -> Unit,
    crossinline onLoadMore: (Comment) -> Unit,
    noinline onUserNameClick: (String) -> Unit,
    noinline onSubredditNameClick: (String) -> Unit,
    noinline onCommentUpdate: (Comment) -> Unit,
    noinline onRequestMoreComments: (String, List<String>) -> Unit,
) {
    when (commentsState) {
        is UIState.Empty -> item { Text("No comments found.") }
        is UIState.Failure -> item { Text("Failed to load comments") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> {
            val comments = commentsState.value
            comments.withIndex().forEach { indexedComment ->
                item {
                    if (indexedComment.value.moreReplies.isEmpty()) {
                        PostCommentItem(
                            indexedComment.value,
                            postUserName,
                            isRepliesVisible = commentsVisibility[indexedComment.value.id] ?: true,
                            onClick = {
                                setCommentsVisibility(
                                    indexedComment.value.id,
                                    commentsVisibility[indexedComment.value.id]?.not() ?: false
                                )
                            },
                            onCommentUpdate,
                            onUserNameClick,
                            onSubredditNameClick,
                        )
                        PagingEffect(comments, indexedComment.index, onLoadMore)
                    } else {
                        ViewMoreCommentItem(
                            onClick = {
                                onRequestMoreComments(indexedComment.value.id, indexedComment.value.moreReplies)
                            }
                        )
                    }
                }
                replies(
                    indexedComment.value.replies,
                    postUserName,
                    isVisible = commentsVisibility[indexedComment.value.id] ?: true,
                    isRepliesVisible = { commentsVisibility[it.id] ?: true },
                    setIsRepliesVisible = { reply, isVisible -> setCommentsVisibility(reply.id, isVisible) },
                    onUserNameClick,
                    onSubredditNameClick,
                    onRequestMoreComments,
                    onCommentUpdate,
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
    depth: Int = 1,
    modifier: Modifier = Modifier,
) {
    replies.forEach { reply ->
        item {
            AnimatedVisibility(
                isVisible,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                if (reply.moreReplies.isEmpty())
                    ReplyItem(
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
                else
                    ViewMoreReplyItem(
                        onClick = { onRequestMoreComments(reply.id, reply.moreReplies) },
                        depth,
                        modifier
                    )
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
            depth = depth + 1,
            modifier
        )
    }
}