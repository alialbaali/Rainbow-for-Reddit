package com.rainbow.desktop.comment

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.BoxLine
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.domain.models.Comment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyItem(
    reply: Comment,
    postUserName: String,
    isRepliesVisible: Boolean,
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
//                .height(IntrinsicSize.Min)
                .padding(horizontal = 16.dp)
        ) {
            repeat(depth) {
                BoxLine(it)
            }
            Column(
                Modifier
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(RainbowTheme.dpDimensions.medium)
            ) {
                PostCommentInfo(
                    reply,
                    postUserName,
                    onUserNameClick,
                    onSubredditNameClick,
                    isSubredditNameEnabled = false
                )
                Text(reply.body, color = MaterialTheme.colorScheme.onBackground)
                CommentOptions(reply, isRepliesVisible, onShowSnackbar)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewMoreReplyItem(onClick: () -> Unit, depth: Int, modifier: Modifier = Modifier) {
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
                RainbowStrings.ViewMore,
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContinueThreadReplyItem(onClick: () -> Unit, depth: Int, modifier: Modifier = Modifier) {
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
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
