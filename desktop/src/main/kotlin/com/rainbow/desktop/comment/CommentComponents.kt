package com.rainbow.desktop.comment

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.*
import com.rainbow.desktop.item.CommentInfo
import com.rainbow.desktop.item.ItemInfo
import com.rainbow.desktop.item.SubredditInfo
import com.rainbow.desktop.item.UserInfo
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.Saved
import com.rainbow.desktop.utils.countRecursively
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.isCakeDay

@Composable
fun CommentInfo(
    comment: Comment,
    postUserName: String?,
    isSubredditNameVisible: Boolean,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isOP = remember(comment.userName, postUserName) { comment.userName == postUserName }
    val userInfo = remember(comment, onUserNameClick) {
        UserInfo(
            comment.userName,
            comment.user?.imageUrl,
            isCakeDay = comment.user?.isCakeDay == true,
            onUserNameClick,
        )
    }
    val subredditInfo = remember(comment, onSubredditNameClick) {
        SubredditInfo(
            comment.subredditName,
            comment.subreddit?.imageUrl,
            onSubredditNameClick,
        )
    }
    val commentInfo = remember(isOP) { CommentInfo(isOP) }

    ItemInfo(
        userInfo,
        if (isSubredditNameVisible) subredditInfo else null,
        postInfo = null,
        commentInfo,
        comment.flair,
        comment.creationDate,
        comment.awards,
        modifier,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CommentOptions(
    comment: Comment,
    isRepliesVisible: Boolean,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val commentsCount = remember(comment.replies) { comment.replies.countRecursively() }

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

        AnimatedVisibility(
            visible = comment.replies.isNotEmpty() && !isRepliesVisible,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
        ) {
            CommentsCount(commentsCount)
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
                        Icon(RainbowIcons.Saved, RainbowStrings.Unsave)
                    } else {
                        Icon(RainbowIcons.StarBorder, RainbowStrings.Save)
                    }
                }
            }

            RainbowDropdownMenuHolder(
                icon = { Icon(RainbowIcons.MoreVert, RainbowStrings.PostOptions) }
            ) { handler ->
                OpenInBrowserDropdownMenuItem(comment.url, handler)
                CopyLinkDropdownMenuItem(comment.url, handler, onShowSnackbar)
            }
        }
    }
}