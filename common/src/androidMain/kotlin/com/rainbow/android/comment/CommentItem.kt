package com.rainbow.android.comment

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rainbow.domain.models.Comment

@Composable
inline fun CommentItem(
    comment: Comment,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    crossinline onCommentClick: (Comment) -> Unit,
    noinline onCommentUpdate: (Comment) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {

    }
}