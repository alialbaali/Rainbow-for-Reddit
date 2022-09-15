package com.rainbow.desktop.comment

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rainbow.desktop.award.ItemAwards
import com.rainbow.desktop.components.*
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.format
import com.rainbow.domain.models.Comment

@Composable
fun PostCommentInfo(
    comment: Comment,
    postUserName: String,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
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
fun CommentInfo(
    comment: Comment,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    isSubredditNameEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        UserName(comment.userName, onUserNameClick)
        Dot()
        if (comment.flair.types.isNotEmpty()) {
            Dot()
            FlairItem(comment.flair, FlairStyle.Compact)
        }
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
                    .padding(RainbowTheme.dpDimensions.small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dpDimensions.medium)
            ) {
                Icon(
                    RainbowIcons.Forum,
                    RainbowStrings.Comments,
                    tint = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    comment.replies.count().format(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
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
                checkedContainerColor = MaterialTheme.colorScheme.background,
                hoverContentColor = RainbowTheme.colors.yellow.copy(0.5F)
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