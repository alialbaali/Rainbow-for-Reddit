package com.rainbow.desktop.comment

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
    onShowSnackbar: (String) -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(RainbowTheme.dpDimensions.medium)
        ) {
            PostCommentInfo(
                comment,
                postUserName,
                onUserNameClick,
                onSubredditNameClick,
                isSubredditNameEnabled = false
            )
            Text(
                comment.body,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            CommentOptions(comment, isRepliesVisible, onShowSnackbar)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewMoreCommentItem(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick,
        shape = MaterialTheme.shapes.medium,
    ) {
        Text(
            RainbowStrings.ViewMore,
            modifier
                .fillMaxWidth()
                .defaultPadding(),
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}