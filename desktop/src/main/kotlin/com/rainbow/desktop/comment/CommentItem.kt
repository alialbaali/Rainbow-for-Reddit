package com.rainbow.desktop.comment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.domain.models.Comment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentItem(
    comment: Comment,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = { onNavigateDetailsScreen(DetailsScreen.Post(comment.postId)) },
        modifier = modifier,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.defaultPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CommentInfo(
                comment,
                onUserNameClick = { userName -> onNavigateMainScreen(MainScreen.User(userName)) },
                onSubredditNameClick = { subredditName -> onNavigateMainScreen(MainScreen.Subreddit(subredditName)) },
                isSubredditNameEnabled = true
            )
            Text(comment.body, color = MaterialTheme.colorScheme.onBackground)
            CommentOptions(comment, false, onShowSnackbar)
        }
    }
}
