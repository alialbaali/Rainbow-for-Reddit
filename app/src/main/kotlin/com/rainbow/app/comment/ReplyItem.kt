package com.rainbow.app.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.domain.models.Comment

@Composable
inline fun ReplyItem(
    reply: Comment,
    depth: Int,
    noinline onClick: () -> Unit,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var contentSize by remember { mutableStateOf(IntSize.Zero) }
    Row(modifier) {
        repeat(depth) {
            val shape = if (it == depth)
                RoundedCornerShape(topStartPercent = 50, topEndPercent = 50)
            else
                RectangleShape
            val color = when (it) {
                0 -> Color.Gray
                1 -> Color.Blue
                2 -> Color.Red
                3 -> Color.Cyan
                4 -> Color.Magenta
                5 -> Color.Yellow
                else -> Color.Green
            }
            Box(
                Modifier
                    .background(color, shape)
                    .width(5.dp)
                    .height(contentSize.height.dp)
            )
            Spacer(Modifier.width(16.dp))
        }

        Column(
            Modifier
                .clickable(onClick = onClick)
                .onSizeChanged { contentSize = it }
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CommentInfo(reply, onUserNameClick, onSubredditNameClick, isSubredditNameEnabled = false)
            Text(reply.body, color = MaterialTheme.colors.onBackground)
            CommentCommands(reply)
        }
    }
}

@Composable
fun ViewMoreReplyItem(onClick: () -> Unit, depth: Int, modifier: Modifier = Modifier) {
    var contentSize by remember { mutableStateOf(IntSize.Zero) }
    Row(modifier) {
        repeat(depth) {
            val shape = when (it) {
                0 -> RoundedCornerShape(topStartPercent = 50, topEndPercent = 50)
                depth - 1 -> RoundedCornerShape(bottomStartPercent = 50, bottomEndPercent = 50)
                else -> RectangleShape
            }
            val color = when (it) {
                0 -> Color.Gray
                1 -> Color.Blue
                2 -> Color.Red
                3 -> Color.Cyan
                4 -> Color.Magenta
                5 -> Color.Yellow
                else -> Color.Green
            }
            Box(
                Modifier
                    .background(color, shape)
                    .width(5.dp)
                    .height(contentSize.height.dp),
            )
            Spacer(Modifier.width(16.dp))
        }
        Text(
            RainbowStrings.ViewMore,
            Modifier
                .clip(MaterialTheme.shapes.large)
                .fillMaxWidth()
                .clickable { onClick() }
                .onSizeChanged { contentSize = it }
                .padding(bottom = 16.dp),
            color = MaterialTheme.colors.onBackground
        )
    }
}
