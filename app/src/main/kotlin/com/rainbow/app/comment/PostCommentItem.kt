package com.rainbow.app.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.domain.models.Comment

@Composable
inline fun PostCommentItem(
    comment: Comment,
    postUserName: String,
    isRepliesVisible: Boolean,
    crossinline onClick: () -> Unit,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .clickable { onClick() }
            .padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PostCommentInfo(comment, postUserName, onUserNameClick, onSubredditNameClick, isSubredditNameEnabled = false)
        Text(comment.body, color = MaterialTheme.colors.onBackground)
        CommentActions(comment, isRepliesVisible)
    }
}

fun main() = application {
    Window({ exitApplication() }) {
        LazyColumn {
            item {
                Row {
                    Box(
                        Modifier
                            .background(Color.Blue)
                            .width(5.dp)
                            .fillMaxHeight(),
                    )
                    Text("Hello world!")
                }
            }
        }
    }
}

@Composable
fun ViewMoreCommentItem(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Text(
        RainbowStrings.ViewMore,
        modifier
            .clip(MaterialTheme.shapes.large)
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        color = MaterialTheme.colors.onBackground
    )
}