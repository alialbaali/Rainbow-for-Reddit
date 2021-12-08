package com.rainbow.app.comment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import com.rainbow.app.components.RainbowProgressIndicator
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.defaultPadding
import com.rainbow.app.utils.defaultSurfaceShape
import com.rainbow.domain.models.Comment


inline fun LazyListScope.userComments(
    commentsState: UIState<List<Comment>>,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
) {
    when (commentsState) {
        is UIState.Empty -> item { Text("No comments found") }
        is UIState.Failure -> item { Text("Failed loading comments") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> itemsIndexed(commentsState.value) { index, comment ->
            UserCommentItem(
                comment = comment,
                onUserNameClick,
                onSubredditNameClick,
                modifier = Modifier
                    .defaultSurfaceShape()
                    .fillParentMaxWidth()
                    .clickable {}
                    .defaultPadding(),
            )
        }
    }
}