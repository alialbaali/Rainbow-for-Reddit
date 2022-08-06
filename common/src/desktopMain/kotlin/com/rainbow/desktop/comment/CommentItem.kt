package com.rainbow.desktop.comment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.common.utils.defaultPadding
import com.rainbow.common.utils.defaultSurfaceShape
import com.rainbow.domain.models.Comment

@Composable
inline fun CommentItem(
    comment: Comment,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    crossinline onCommentClick: (Comment) -> Unit,
    noinline onCommentUpdate: (Comment) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .defaultSurfaceShape()
            .clickable { onCommentClick(comment) }
            .defaultPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CommentInfo(comment, onUserNameClick, onSubredditNameClick, isSubredditNameEnabled = true)
        Text(comment.body, color = MaterialTheme.colorScheme.onBackground)
        CommentActions(comment, onCommentUpdate, false)
    }
}
