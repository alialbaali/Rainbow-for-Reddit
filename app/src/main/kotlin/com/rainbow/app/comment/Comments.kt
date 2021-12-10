package com.rainbow.app.comment

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import com.rainbow.app.components.RainbowProgressIndicator
import com.rainbow.app.utils.UIState
import com.rainbow.domain.models.Comment

inline fun LazyListScope.comments(
    commentsState: UIState<List<Comment>>,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    crossinline onCommentClick: (Comment) -> Unit,
) {
    when (commentsState) {
        is UIState.Empty -> item { Text("No comments found") }
        is UIState.Failure -> item { Text("Failed loading comments") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> itemsIndexed(commentsState.value) { index, comment ->
            CommentItem(
                comment,
                onUserNameClick,
                onSubredditNameClick,
                onCommentClick,
                modifier = Modifier.fillParentMaxWidth()
            )
        }
    }
}