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
    repliesVisibility: Map<Comment, Boolean>,
    noinline setRepliesVisibility: (Comment, Boolean) -> Unit,
    crossinline onLoadMore: (Comment) -> Unit,
    noinline onUserNameClick: (String) -> Unit,
    noinline onSubredditNameClick: (String) -> Unit,
) {
    when (commentsState) {
        is UIState.Empty -> item { Text("No comments found.") }
        is UIState.Failure -> item { Text("Failed to load comments") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> {
            val comments = commentsState.value
            comments.withIndex().forEach { indexedComment ->
                item {
                    if (indexedComment.value.subredditId.isNotBlank()) {
                        PostCommentItem(
                            indexedComment.value,
                            onClick = {
                                setRepliesVisibility(
                                    indexedComment.value,
                                    repliesVisibility[indexedComment.value]?.not() ?: false
                                )
                            },
                            onUserNameClick,
                            onSubredditNameClick,
                        )
                        PagingEffect(comments, indexedComment.index, onLoadMore)
                    } else {
                        ViewMoreCommentItem(onClick = {})
                    }
                }
                replies(
                    indexedComment.value.replies,
                    isVisible = repliesVisibility[indexedComment.value] ?: true,
                    isRepliesVisible = { repliesVisibility[it] ?: true },
                    setIsRepliesVisible = { reply, isVisible -> setRepliesVisibility(reply, isVisible) },
                    onUserNameClick,
                    onSubredditNameClick,
                )
            }
        }
    }
}

fun LazyListScope.replies(
    replies: List<Comment>,
    isVisible: Boolean,
    isRepliesVisible: (Comment) -> Boolean,
    setIsRepliesVisible: (Comment, Boolean) -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    depth: Int = 1,
    modifier: Modifier = Modifier
) {
    replies.forEach { reply ->
        item {
            AnimatedVisibility(
                isVisible,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                if (reply.subredditId.isNotBlank())
                    ReplyItem(
                        reply,
                        depth,
                        onClick = { setIsRepliesVisible(reply, !isRepliesVisible(reply)) },
                        onUserNameClick,
                        onSubredditNameClick,
                        modifier
                    )
                else
                    ViewMoreReplyItem(
                        onClick = {},
                        depth,
                        modifier
                    )
            }
        }
        replies(
            reply.replies,
            isVisible = isRepliesVisible(reply) && isVisible,
            isRepliesVisible,
            setIsRepliesVisible,
            onUserNameClick,
            onSubredditNameClick,
            depth = depth + 1,
            modifier
        )
    }
}