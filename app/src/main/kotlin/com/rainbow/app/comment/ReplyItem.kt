package com.rainbow.app.comment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.rainbow.app.components.BoxLine
import com.rainbow.app.utils.RainbowStrings
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
    modifier: Modifier = Modifier
) {
    var contentSize by remember { mutableStateOf(IntSize.Zero) }
    Row(modifier) {
        repeat(depth) {
            BoxLine(it, contentSize.height.dp)
        }
        Column(
            Modifier
                .clickable(onClick = onClick)
                .onSizeChanged { contentSize = it }
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PostCommentInfo(reply, postUserName, onUserNameClick, onSubredditNameClick, isSubredditNameEnabled = false)
            Text(reply.body, color = MaterialTheme.colors.onBackground)
            CommentActions(reply, isRepliesVisible)
        }
    }
}

@Composable
fun ViewMoreReplyItem(onClick: () -> Unit, depth: Int, modifier: Modifier = Modifier) {
    var contentSize by remember { mutableStateOf(IntSize.Zero) }
    Row(modifier) {
        repeat(depth) {
            BoxLine(it, contentSize.height.dp)
        }
        Text(
            RainbowStrings.ViewMore,
            Modifier
                .clip(MaterialTheme.shapes.large)
                .fillMaxWidth()
                .clickable { onClick() }
                .onSizeChanged { contentSize = it }
                .padding(vertical = 16.dp),
            color = MaterialTheme.colors.onBackground
        )
    }
}
