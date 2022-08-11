package com.rainbow.desktop.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.defaultPadding
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
            .clickable { onClick() }
            .shadow(1.dp)
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .defaultPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PostCommentInfo(comment, postUserName, onUserNameClick, onSubredditNameClick, isSubredditNameEnabled = false)
        Text(comment.body, color = MaterialTheme.colorScheme.onBackground)
        CommentActions(comment, isRepliesVisible)
    }
}

@Composable
fun ViewMoreCommentItem(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Text(
        RainbowStrings.ViewMore,
        modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() }
            .fillMaxWidth()
            .defaultPadding(),
        color = MaterialTheme.colorScheme.onBackground
    )
}