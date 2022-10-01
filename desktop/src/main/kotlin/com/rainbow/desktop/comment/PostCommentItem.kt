package com.rainbow.desktop.comment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.EditionDate
import com.rainbow.desktop.components.ExpandableText
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.domain.models.Comment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCommentItem(
    comment: Comment,
    postUserName: String,
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
            verticalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.medium)
        ) {
            CommentInfo(
                comment,
                postUserName,
                isSubredditNameVisible = false,
                onUserNameClick,
                onSubredditNameClick,
            )
            SelectionContainer {
                ExpandableText(comment.body, style = MaterialTheme.typography.bodyLarge)
            }
            comment.editionDate?.let { editionDate ->
                EditionDate(editionDate)
            }
            CommentOptions(comment, onClick, onShowSnackbar)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreCommentsItem(onClick: () -> Unit, modifier: Modifier = Modifier) {
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
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}