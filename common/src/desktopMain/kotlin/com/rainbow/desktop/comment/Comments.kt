package com.rainbow.desktop.comment

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
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
        is UIState.Success -> itemsIndexed(commentsState.value) { index, comment ->
            CommentItem(
                comment,
                onUserNameClick,
                onSubredditNameClick,
                onCommentClick,
                onCommentUpdate,
                modifier = Modifier.fillParentMaxWidth()
            )
        }
    }
}