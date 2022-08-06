package com.rainbow.android.comment

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import com.rainbow.common.components.RainbowProgressIndicator
import com.rainbow.common.utils.UIState
import com.rainbow.domain.models.Comment

inline fun LazyListScope.comments(
    commentsState: UIState<List<Comment>>,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    crossinline onCommentClick: (Comment) -> Unit,
    noinline onCommentUpdate: (Comment) -> Unit,
) {
    when (commentsState) {
        is UIState.Failure -> item { Text("Failed loading comments") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> items(commentsState.value) { comment ->
            CommentItem(
                comment,
                onUserNameClick,
                onSubredditNameClick,
                onCommentClick,
                onCommentUpdate,
            )
        }
    }
}