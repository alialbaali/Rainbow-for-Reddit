package com.rainbow.app.comment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.defaultPadding
import com.rainbow.domain.models.Comment

@Composable
inline fun PostCommentItem(
    comment: Comment,
    postUserName: String,
    isRepliesVisible: Boolean,
    crossinline onClick: () -> Unit,
    noinline onUpdate: (Comment) -> Unit,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .defaultPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PostCommentInfo(comment, postUserName, onUserNameClick, onSubredditNameClick, isSubredditNameEnabled = false)
        Text(comment.body, color = MaterialTheme.colors.onBackground)
        CommentActions(comment, onUpdate, isRepliesVisible)
    }
}

@Composable
fun ViewMoreCommentItem(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Text(
        RainbowStrings.ViewMore,
        modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .defaultPadding(),
        color = MaterialTheme.colors.onBackground
    )
}