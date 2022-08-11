package com.rainbow.desktop.comment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rainbow.desktop.award.ItemAwards
import com.rainbow.desktop.components.*
import com.rainbow.desktop.utils.RainbowIcons
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
            ItemAwards(comment.awards, {})
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
            ItemAwards(comment.awards, {})
        }
    }
}

@Composable
fun CommentActions(
    comment: Comment,
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
            onUpvote = { CommentActionsStateHolder.upvoteComment(comment) },
            onDownvote = { CommentActionsStateHolder.downvoteComment(comment) },
            onUnvote = { CommentActionsStateHolder.unvoteComment(comment) }
        )

        AnimatedVisibility(comment.replies.isNotEmpty() && !isRepliesVisible) {
            Text("${comment.replies.count()} replies")
        }

        Column {
            IconButton(onClick = { isMenuExpanded = true }) {
                Icon(RainbowIcons.MoreVert, contentDescription = "More")
            }

//            RainbowMenu(
//                expanded = isMenuExpanded,
//                onDismissRequest = { isMenuExpanded = false },
//            ) {
//                RainbowMenuItem("Share", RainbowIcons.Share, onclick = {})
//                RainbowMenuItem("View User", RainbowIcons.Person, onclick = {})
//                RainbowMenuItem("Reply", RainbowIcons.Send, onclick = {})
//                RainbowMenuItem("Block User", RainbowIcons.Block, onclick = {})
//            }
        }
    }
}