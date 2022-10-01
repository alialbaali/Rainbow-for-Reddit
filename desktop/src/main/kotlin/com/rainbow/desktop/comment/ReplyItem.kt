package com.rainbow.desktop.comment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.BoxLine
import com.rainbow.desktop.components.EditionDate
import com.rainbow.desktop.components.ExpandableText
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.domain.models.Comment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyItem(
    reply: Comment,
    postUserName: String,
    depth: Int,
    onClick: () -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(onClick) {
        Row(
            modifier
                .height(IntrinsicSize.Min)
                .padding(horizontal = 16.dp)
        ) {
            repeat(depth) {
                BoxLine(it)
            }
            Column(
                Modifier.padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.medium)
            ) {
                CommentInfo(
                    reply,
                    postUserName,
                    isSubredditNameVisible = false,
                    onUserNameClick,
                    onSubredditNameClick,
                )
                SelectionContainer {
                    ExpandableText(reply.body, style = MaterialTheme.typography.bodyLarge)
                }
                reply.editionDate?.let { editionDate ->
                    EditionDate(editionDate)
                }
                CommentOptions(reply, onClick, onShowSnackbar)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreRepliesItem(type: Comment.Type.MoreComments, onClick: () -> Unit, depth: Int, modifier: Modifier = Modifier) {
    val count = remember { type.replies.count() }

    Surface(onClick) {
        Row(
            modifier
                .height(IntrinsicSize.Min)
                .padding(horizontal = 16.dp)
        ) {
            repeat(depth) {
                BoxLine(it)
            }
            Text(
                RainbowStrings.MoreReplies(count),
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadItem(onClick: () -> Unit, depth: Int, modifier: Modifier = Modifier) {
    Surface(onClick) {
        Row(
            modifier
                .height(IntrinsicSize.Min)
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
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
