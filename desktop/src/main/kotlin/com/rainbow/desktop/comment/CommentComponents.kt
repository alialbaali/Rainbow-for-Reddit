package com.rainbow.desktop.comment

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rainbow.desktop.award.ItemAwards
import com.rainbow.desktop.components.*
import com.rainbow.desktop.post.PostActionsStateHolder
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.format
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

@OptIn(ExperimentalAnimationApi::class)
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
            Row(
                Modifier
                    .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium)
                    .padding(RainbowTheme.dpDimensions.medium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dpDimensions.medium)
            ) {
                Icon(RainbowIcons.Forum, RainbowStrings.Comments)
                Text(comment.replies.count().format(), style = MaterialTheme.typography.labelLarge)
            }
        }

        Row(
            Modifier.background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RainbowIconToggleButton(
                checked = comment.isSaved,
                onCheckedChange = {
                    if (comment.isSaved) {
                        CommentActionsStateHolder.unSaveComment(comment)
                    } else {
                        CommentActionsStateHolder.saveComment(comment)
                    }
                },
                checkedContentColor = RainbowTheme.colors.yellow,
            ) {
                AnimatedContent(comment.isSaved) { isSaved ->
                    if (isSaved) {
                        Icon(RainbowIcons.Star, RainbowStrings.Unsave)
                    } else {
                        Icon(RainbowIcons.StarBorder, RainbowStrings.Save)
                    }
                }
            }

            RainbowIconButton(
                onClick = {},
            ) {
                Icon(RainbowIcons.MoreVert, RainbowStrings.MoreActions)
            }
        }
    }
}