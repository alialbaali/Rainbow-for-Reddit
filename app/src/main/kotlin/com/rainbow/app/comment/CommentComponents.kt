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
import com.rainbow.domain.models.Comment

@Composable
inline fun PostCommentInfo(
    comment: Comment,
    postUserName: String,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    isSubredditNameEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        CommentUserName(comment.userName, postUserName, onUserNameClick)
        Dot()
        if (isSubredditNameEnabled) {
            SubredditName(comment.subredditName, onSubredditNameClick)
            Dot()
        }
        CreationDate(comment.creationDate)
        if (comment.awards.isNotEmpty()) {
            Dot()
            Awards(comment.awards)
        }
    }
}

@Composable
inline fun CommentInfo(
    comment: Comment,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    isSubredditNameEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        UserName(comment.userName, onUserNameClick)
        Dot()
        if (isSubredditNameEnabled) {
            SubredditName(comment.subredditName, onSubredditNameClick)
            Dot()
        }
        CreationDate(comment.creationDate)
        if (comment.flair.types.isNotEmpty()) {
            Dot()
            FlairItem(comment.flair)
        }
        if (comment.awards.isNotEmpty()) {
            Dot()
            Awards(comment.awards)
        }
    }
}

@Composable
fun CommentActions(
    comment: Comment,
    onUpdate: (Comment) -> Unit,
    isRepliesVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        VoteActions(
            vote = comment.vote,
            votesCount = comment.votesCount,
            onUpvote = { CommentActionsModel.upvoteComment(comment, onUpdate) },
            onDownvote = { CommentActionsModel.downvoteComment(comment, onUpdate) },
            onUnvote = { CommentActionsModel.unvoteComment(comment, onUpdate) }
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