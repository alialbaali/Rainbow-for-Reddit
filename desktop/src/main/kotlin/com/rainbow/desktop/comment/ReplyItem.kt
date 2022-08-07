package com.rainbow.desktop.comment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
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
    noinline onCommentUpdate: (Comment) -> Unit,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var contentSize by remember { mutableStateOf(IntSize.Zero) }
    Row(
        modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp)
    ) {
        repeat(depth) {
            BoxLine(it, contentSize.height.dp)
        }
        Column(
            Modifier
                .onSizeChanged { contentSize = it }
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PostCommentInfo(reply, postUserName, onUserNameClick, onSubredditNameClick, isSubredditNameEnabled = false)
            Text(reply.body, color = MaterialTheme.colorScheme.onBackground)
            CommentActions(reply, onCommentUpdate, isRepliesVisible)
        }
    }
}

@Composable
fun ViewMoreReplyItem(onClick: () -> Unit, depth: Int, modifier: Modifier = Modifier) {
    var contentSize by remember { mutableStateOf(IntSize.Zero) }
    Row(
        modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp)
    ) {
        repeat(depth) {
            BoxLine(it, contentSize.height.dp)
        }
        Text(
            RainbowStrings.ViewMore,
            Modifier
                .fillMaxWidth()
                .onSizeChanged { contentSize = it }
                .padding(vertical = 16.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun ContinueThreadReplyItem(onClick: () -> Unit, depth: Int, modifier: Modifier = Modifier) {
    var contentSize by remember { mutableStateOf(IntSize.Zero) }
    Row(
        modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp)
    ) {
        repeat(depth) {
            BoxLine(it, contentSize.height.dp)
        }
        Text(
            RainbowStrings.ContinueThread,
            Modifier
                .fillMaxWidth()
                .onSizeChanged { contentSize = it }
                .padding(vertical = 16.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}