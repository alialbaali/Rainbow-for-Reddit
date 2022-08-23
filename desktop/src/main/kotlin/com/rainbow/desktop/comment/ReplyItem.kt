package com.rainbow.desktop.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.BoxLine
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.domain.models.Comment

@Composable
inline fun ReplyItem(
    reply: Comment,
    postUserName: String,
    isRepliesVisible: Boolean,
    depth: Int,
    noinline onClick: () -> Unit,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .height(IntrinsicSize.Min)
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp)
    ) {
        repeat(depth) {
            BoxLine(it)
        }
        Column(
            Modifier
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PostCommentInfo(reply, postUserName, onUserNameClick, onSubredditNameClick, isSubredditNameEnabled = false)
            Text(reply.body, color = MaterialTheme.colorScheme.onBackground)
            CommentActions(reply, isRepliesVisible)
        }
    }
}

@Composable
fun ViewMoreReplyItem(onClick: () -> Unit, depth: Int, modifier: Modifier = Modifier) {
    Row(
        modifier
            .height(IntrinsicSize.Min)
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp)
    ) {
        repeat(depth) {
            BoxLine(it)
        }
        Text(
            RainbowStrings.ViewMore,
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun ContinueThreadReplyItem(onClick: () -> Unit, depth: Int, modifier: Modifier = Modifier) {
    Row(
        modifier
            .height(IntrinsicSize.Min)
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp)
    ) {
        repeat(depth) {
            BoxLine(it)
        }
        Text(
            RainbowStrings.ContinueThread,
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}