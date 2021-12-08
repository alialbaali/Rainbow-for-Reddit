package com.rainbow.app.comment

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.app.components.RainbowMenu
import com.rainbow.app.components.RainbowMenuItem
import com.rainbow.app.components.VoteActions
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.data.Repos
import com.rainbow.domain.models.Comment
import kotlinx.coroutines.launch

@Composable
inline fun UserCommentItem(
    comment: Comment,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        CommentInfo(comment, onUserNameClick, onSubredditNameClick, isSubredditNameEnabled = true)
        Text(comment.body, color = MaterialTheme.colors.onBackground)
        Row {
            VoteActions(
                vote = comment.vote,
                votesCount = comment.upvotesCount.toLong(),
                onUpvote = {
                    scope.launch {
                        Repos.Comment.upvoteComment(comment.id)
                    }
                },
                onDownvote = {
                    scope.launch {
                        Repos.Comment.downvoteComment(comment.id)
                    }
                },
                onUnvote = {
                    scope.launch {
                        Repos.Comment.unvoteComment(comment.id)
                    }
                }
            )
            Spacer(Modifier.width(16.dp))
            RainbowMenu(
                expanded = false,
                onDismissRequest = {},
            ) {
                RainbowMenuItem("Share", RainbowIcons.Share, onclick = {})
                RainbowMenuItem("View User", RainbowIcons.Person, onclick = {})
                RainbowMenuItem("Reply", RainbowIcons.Send, onclick = {})
                RainbowMenuItem("Block User", RainbowIcons.Block, onclick = {})
            }
        }
    }
}
