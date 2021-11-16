package com.rainbow.app.comment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rainbow.app.award.Awards
import com.rainbow.app.components.*
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.data.Repos
import com.rainbow.domain.models.Comment
import kotlinx.coroutines.launch

@Composable
inline fun CommentInfo(
    comment: Comment,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    isSubredditNameEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        if (isSubredditNameEnabled) {
            SubredditName(comment.subredditName, onSubredditNameClick)
            Dot()
        }
        UserName(comment.userName, onUserNameClick)
        Dot()
        CreationDate(comment.creationDate)
        if (comment.awards.isNotEmpty()) {
            Dot()
            Awards(comment.awards)
        }
    }
}

@Composable
fun CommentActions(comment: Comment, isRepliesVisible: Boolean, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    var isMenuExpanded by remember { mutableStateOf(false) }
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
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

        AnimatedVisibility(comment.replies.isNotEmpty() && !isRepliesVisible) {
            Text("${comment.replies.count()} replies")
        }

        Column {
            IconButton(onClick = { isMenuExpanded = true }) {
                Icon(RainbowIcons.MoreVert, contentDescription = "More")
            }

            RainbowMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false },
            ) {
                RainbowMenuItem("Share", RainbowIcons.Share, onclick = {})
                RainbowMenuItem("View User", RainbowIcons.Person, onclick = {})
                RainbowMenuItem("Reply", RainbowIcons.Send, onclick = {})
                RainbowMenuItem("Block User", RainbowIcons.Block, onclick = {})
            }
        }
    }
}