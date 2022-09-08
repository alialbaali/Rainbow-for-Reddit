@file:OptIn(ExperimentalMaterial3Api::class)

package com.rainbow.desktop.comment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.domain.models.Comment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCommentItem(
    comment: Comment,
    postUserName: String,
    isRepliesVisible: Boolean,
    onClick: () -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .defaultPadding(),
            verticalArrangement = Arrangement.spacedBy(RainbowTheme.dpDimensions.large)
        ) {
            PostCommentInfo(
                comment,
                postUserName,
                onUserNameClick,
                onSubredditNameClick,
                isSubredditNameEnabled = false
            )
            Text(comment.body, style = MaterialTheme.typography.titleMedium)
            CommentActions(comment, isRepliesVisible)
        }
    }
}

@Composable
fun ViewMoreCommentItem(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Text(
        RainbowStrings.ViewMore,
        modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() }
            .fillMaxWidth()
            .defaultPadding(),
        color = MaterialTheme.colorScheme.onBackground
    )
}