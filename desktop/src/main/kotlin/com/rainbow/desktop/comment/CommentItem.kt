package com.rainbow.desktop.comment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.ExpandableText
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.domain.models.Comment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentItem(
    comment: Comment,
    onClick: () -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.defaultPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CommentInfo(
                comment,
                postUserName = null,
                isSubredditNameVisible = true,
                onUserNameClick,
                onSubredditNameClick,
            )
            ExpandableText(comment.body)
            CommentOptions(comment, onRepliesCountClick = null, onShowSnackbar)
        }
    }
}
